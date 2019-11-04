/**
 * JWave - Java implementation of wavelet transform algorithms
 *
 * Copyright 2010 Christian Scheiblich
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * This file Wavelet.java is part of JWave.
 *
 * @author Christian Scheiblich
 * date 23.02.2010 05:42:23
 * contact source@linux23.de
 */
package cs.jwave.handlers.wavelets;

/**
 * Basic class for one wavelet keeping coefficients of the wavelet function, the
 * scaling function, the base wavelength, the forward transform method, and the
 * reverse transform method.
 * 
 * @date 10.02.2010 08:54:48
 * @author Christian Scheiblich
 */
public abstract class Wavelet {

  /* This variables set the mode how the wavelet convolution
   * will be done. This regards the border distortion effect.
   * See dwtmode help in MatLab for more information.
   * This variables affect only wavelet and wavelet pack transform.
   */
  public static final int WCONV_SYM   = 0;
  public static final int WCONV_SYMH  = 0;
  public static final int WCONV_SYMW  = 1;
  public static final int WCONV_ASYM  = 2;
  public static final int WCONV_ASYMH = 2;
  public static final int WCONV_ASYMW = 3;
  public static final int WCONV_ZPD   = 4;
  public static final int WCONV_SPD   = 5;
  public static final int WCONV_SP1   = 5;
  public static final int WCONV_SP0   = 6;
  public static final int WCONV_PPD   = 7;
  public static final int WCONV_PER   = 8;
  public static final int WCONV_NONE  = 9;
  
  private int wconv_curr = WCONV_NONE; 
  
  public static final String[] bName = new String[]{
    "SYMH", "SYMW", "ASYMH", "ASYMW",
    "ZPD", "SP1", "SP0", "PPD", "PER", "NONE"
  };
  /**
   * minimal wavelength of the used wavelet and scaling coefficients
   */
  protected int _waveLength;

  /**
   * coefficients of the wavelet; wavelet function
   */
  protected double[ ] _coeffs;

  /**
   * coefficients of the scales; scaling function
   */
  protected double[ ] _scales;

  /**
   * Constructor; predefine members to init values
   * 
   * @date 10.02.2010 08:54:48
   * @author Christian Scheiblich
   */
  public Wavelet( ) {
    _waveLength = 0;
    _coeffs = null;
    _scales = null;
  } // Wavelet

  /**
   * Performs the forward transform for the given array from time domain to
   * Hilbert domain and returns a new array of the same size keeping
   * coefficients of Hilbert domain and should be of length 2 to the power of p
   * -- length = 2^p where p is a positive integer.
   * 
   * @date 10.02.2010 08:18:02
   * @author Christian Scheiblich
   * @param arrTime
   *          array keeping time domain coefficients
   * @return coefficients represented by frequency domain
   */
  public double[ ] forward( double[ ] arrTime ) {
    
    if(wconv_curr != Wavelet.WCONV_NONE)
      return forward_ext( arrTime );

    double[ ] arrHilb = new double[ arrTime.length ];

    int k = 0;
    int h = arrTime.length >> 1;

    for( int i = 0; i < h; i++ ) {

      for( int j = 0; j < _waveLength; j++ ) {

        k = ( i << 1 ) + j;
        if( k >= arrTime.length )
          //k -= arrTime.length;
          continue;
          
        arrHilb[ i ] += arrTime[ k ] * _scales[ j ]; // low pass filter - energy (approximation)
        arrHilb[ i + h ] += arrTime[ k ] * _coeffs[ j ]; // high pass filter - details 

      } // wavelet

    } // h

    return arrHilb;
  } // forward

  public double[ ] forward_ext( double[ ] arrTime_ ) {
    
    double[ ] arrTime = prepareArrTime(arrTime_);
    double[ ] arrHilb = new double[ 2 * ((arrTime_.length-1)/2 + _waveLength/2) ];

    int k = 0;
    int h = arrHilb.length >> 1;

    for( int i = 0; i < h; i++ ) {
      
      k = ((i+1)<<1)-1;
      
      for( int j = _waveLength-1; j > -1; j-- ) {
        
        arrHilb[ i ]     += arrTime[ k+j ] * _scales[ j ]; // low pass filter - energy (approximation)
        arrHilb[ i + h ] += arrTime[ k+j ] * _coeffs[ j ]; // high pass filter - details

      } // wavelet

    } // h

    return arrHilb;

  }

  private double[ ] prepareArrTime( double[ ] arrTime_ ) {
    
    if(wconv_curr == Wavelet.WCONV_NONE) return arrTime_;
    
    double[][] padding = null;
    
    switch(wconv_curr){
      
      case Wavelet.WCONV_ZPD   : padding = zpd  (arrTime_); break;
      case Wavelet.WCONV_SYMH  : padding = symh (arrTime_); break;
      case Wavelet.WCONV_SYMW  : padding = symw (arrTime_); break;
      case Wavelet.WCONV_ASYMH : padding = asymh(arrTime_); break;
      case Wavelet.WCONV_ASYMW : padding = asymw(arrTime_); break;
      case Wavelet.WCONV_SP1   : padding = spd  (arrTime_); break;
      case Wavelet.WCONV_SP0   : padding = sp0  (arrTime_); break;
      case Wavelet.WCONV_PPD   : padding = ppd  (arrTime_); break;
      case Wavelet.WCONV_PER   : padding = per  (arrTime_); break;

      default : {
        System.err.println("Boundary type not yet implemented, changing to ZPD");
        padding = zpd( arrTime_ );
      }
    }
    
    int padd = _waveLength-1;
    int size = arrTime_.length;
    double[] result  = new double[ size + 2*padd ];

    System.arraycopy(padding[0], 0, result, 0, padd);
    System.arraycopy(arrTime_, 0, result, padd, arrTime_.length);
    System.arraycopy(padding[1], 0, result, size+padd, padd);
    
    return result;
  }

  private double[ ][ ] per( double[ ] arrTime_ ) {
    
    // if arrTime_ size is not even, add one cell at right and
    // repeat the last value, and padding is done as ppd.
    
    if(arrTime_.length % 2 != 0){
      double[] temp = new double[arrTime_.length+1];
      System.arraycopy( arrTime_, 0, temp, 0, arrTime_.length );
      temp[arrTime_.length] = arrTime_[arrTime_.length-1];
      arrTime_ = temp;
    }
    
    return ppd( arrTime_ );
    
  }

  private double[ ][ ] ppd( double[ ] arrTime_ ) {
    
    // before -   1 | 2 | 3
    // after  -   1 | 2 | 3 | 1 | 2 | 3 | 1 | 2 | 3 
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    for( int i = 0; i < ps; i++ ) {
      padding[0][ps-1-i] = arrTime_[ts-i];
      padding[1][i] = arrTime_[i];
    }
    
    return padding;
  }

  private double[ ][ ] sp0( double[ ] arrTime_ ) {
    
    // before -   1 | 2 | 3
    // after  -   1 | 1 | 1 | 1 | 2 | 3 | 3 | 3 | 3 
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    for( int i = 0; i < ps; i++ ) {
      padding[0][i] = arrTime_[0];
      padding[1][i] = arrTime_[ts];
    }
    
    return padding;
  }

  private double[ ][ ] spd( double[ ] arrTime_ ) {
    
    // before -   1.1 | 1.2 | 2
    // after  -   0.7 | 0.8 | 0.9 | 1.1 | 1.2 | 2 | 2.8 | 3.6 | 4.4  
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    double l = arrTime_[0]-arrTime_[1];
    double r = arrTime_[ts]-arrTime_[ts-1];
    
    padding[0][ps-1] = arrTime_[0]+l;
    padding[1][0] = arrTime_[ts]+r;
    
    for( int i = 1; i < ps; i++ ) {
      padding[0][ps-1-i] = padding[0][ps-i]+l;
      padding[1][i] = padding[1][i-1]+r;
    }
    
    return padding;
  }

  private double[ ][ ] asymw( double[ ] arrTime_ ) {
    
    // before -   1.1 | 1.2 | 2
    // after  -   -0.6 | 0.2 | 1.0 | 1.1 | 1.2 | 2 | 2.8 | 2.9 | 3.0 
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    for( int i = 0; i < ps; i++ ) {
      padding[0][ps-1-i] = 2*arrTime_[0]-arrTime_[i+1];
      padding[1][i] = 2*arrTime_[ts] - arrTime_[ts-i-1];
    }
    
    return padding;
  }

  private double[ ][ ] asymh( double[ ] arrTime_ ) {
    
    // before -   1 | 2 | 3
    // after  -   -3 | -2 | -1 | 1 | 2 | 3 | -3 | -2 | -1 
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    for( int i = 0; i < ps; i++ ) {
      padding[0][ps-1-i] = -arrTime_[i];
      padding[1][i] = -arrTime_[ts-i];
    }
    
    return padding;
  }

  private double[ ][ ] symw( double[ ] arrTime_ ) {
    
    // before -   1 | 2 | 3
    // after  -   1 | 3 | 2 | 1 | 2 | 3 | 2 | 1 | 3 
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    for( int i = 0; i < ps; i++ ) {
      padding[0][ps-1-i] = arrTime_[i+1];
      padding[1][i] = arrTime_[ts-i-1];
    }
    
    return padding;
  }

  private double[ ][ ] symh( double[ ] arrTime_ ) {
    
    // before -   1 | 2 | 3
    // after  -   3 | 2 | 1 | 1 | 2 | 3 | 3 | 2 | 1 
    
    int ps = _waveLength-1;     //Padding size
    int ts = arrTime_.length-1; //Time size
    double[][] padding = new double[2][ps];
    
    for( int i = 0; i < ps; i++ ) {
      padding[0][ps-1-i] = arrTime_[i];
      padding[1][i] = arrTime_[ts-i];
    }
    
    return padding;
  }

  private double[ ][ ] zpd( double[ ] arrTime_ ) {
    
    // before -   1 | 2 | 3
    // after  - 0 | 0 | 0 | 1 | 2 | 3 | 0 | 0 | 0
    
    double[][] padding = new double[2][_waveLength-1];
    
    for( int i = 0; i < 2; i++ ) {
      for( int j = 0; j < padding[0].length; j++ ) {
        padding[i][j] = 0;
      }
    }
    
    return padding;
  }

  /**
   * Performs the reverse transform for the given array from Hilbert domain to
   * time domain and returns a new array of the same size keeping coefficients
   * of time domain and should be of length 2 to the power of p -- length = 2^p
   * where p is a positive integer.
   * 
   * @date 10.02.2010 08:19:24
   * @author Christian Scheiblich
   * @param arrHilb
   *          array keeping frequency domain coefficients
   * @return coefficients represented by time domain
   */
  public double[ ] reverse( double[ ] arrHilb ) {

    double[ ] arrTime = new double[ arrHilb.length ];

    int k = 0;
    int h = arrHilb.length >> 1;
    for( int i = 0; i < h; i++ ) {

      for( int j = 0; j < _waveLength; j++ ) {

        k = ( i << 1 ) + j;
        if( k >= arrHilb.length )
          k -= arrHilb.length;

        arrTime[ k ] += ( arrHilb[ i ] * _scales[ j ] + arrHilb[ i + h ]
            * _coeffs[ j ] ); // adding up details times energy (approximation)

      } // wavelet

    } //  h

    return arrTime;
  } // reverse

  /**
   * Returns the minimal wavelength for the used wavelet.
   * 
   * @date 10.02.2010 08:13:59
   * @author Christian Scheiblich
   * @return the minimal wavelength for this basic wave
   */
  public int getWaveLength( ) {
    return _waveLength;
  } // getWaveLength

  /**
   * Returns the number of coeffs (and scales).
   * 
   * @date 08.02.2010 13:11:47
   * @author Christian Scheiblich
   * @return integer representing the number of coeffs.
   */
  public int getLength( ) {
    return _coeffs.length;
  } // getLength

  /**
   * Returns a double array with the coeffs.
   * 
   * @date 08.02.2010 13:14:54
   * @author Christian Scheiblich
   * @return double array keeping the coeffs.
   */
  public double[ ] getCoeffs( ) {
    
    double[ ] coeffs = new double[ _coeffs.length ];
    System.arraycopy( _coeffs, 0, coeffs, 0, _coeffs.length );
    return coeffs;
    
  } // getCoeffs

  /**
   * Returns a double array with the scales (of a wavelet).
   * 
   * @date 01.07.2011 11:26
   * @author Edras Pacola
   * @return double array keeping the scales.
   */
  public double[ ] getScales( ) {
    
      double[ ] scales = new double[ _scales.length ];
      System.arraycopy( _scales, 0, scales, 0, _scales.length );
      return scales;
      
  } // getScales

  public double[ ][ ] wtstnst( double[ ] arrTime, double threshold ) {
    
    double[ ][ ] arrHilb = new double[3][ arrTime.length ];

    double mean = 0;
    double mean_dev = 0;
    int k = 0;
    int h = arrTime.length >> 1;

    for( int i = 0; i < h; i++ ) {
      for( int j = 0; j < _waveLength; j++ ) {
        k = getK(i,j,arrTime.length);
        arrHilb[ 2 ][ i ]   = arrTime[ k ] * _scales[ j ]; // low pass filter - energy (approximation)
        arrHilb[ 2 ][ i+h ] = arrTime[ k ] * _coeffs[ j ]; // high pass filter - details
      } // wavelet
      mean += arrHilb[2][i];
    } // h

    for(int i = 0; i < arrHilb[2].length; i++){
      arrHilb[0][i] = arrHilb[2][i];
      arrHilb[1][i] = arrHilb[2][i];
    }
    
    mean = mean / h; 
    for( int i = 0; i < h; i++ ) 
      mean_dev += Math.pow((arrHilb[2][i]-mean),2);
    mean_dev = Math.sqrt(mean_dev/(h-1));

    for( int i = 0; i < h; i++ ) {
      
        if(arrHilb[ 2 ][ i ] < mean_dev*threshold) 
          arrHilb[0][ i + h ] = 0;
        else
          arrHilb[1][ i + h ] = 0;
        
    }
    return arrHilb;
  }
  
  protected int getK( int i, int j, int len ) {
    int k = ( i << 1 ) + j;
    if( k >= len )
      k -= len;
    return k;
  }
  
  public void setBoundary(int bound){
    wconv_curr = bound;
  }
  
  public int getBoundary(){
    return wconv_curr;
  }
  
} // class
