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
 * This file FastWaveletTransform.java is part of JWave.
 *
 * @author Christian Scheiblich
 * date 23.02.2010 05:42:23
 * contact source@linux23.de
 */
package cs.jwave.handlers;

import java.util.HashMap;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Base class for the forward and reverse Fast Wavelet Transform in 1-D, 2-D,
 * and 3-D using a specified Wavelet by inheriting class.
 * 
 * @date 10.02.2010 08:10:42
 * @author Christian Scheiblich
 */
public class FastWaveletTransform extends BasicTransform {

  /**
   * The selected wavelet for the specified transform algorithm.
   */
  protected Wavelet _wavelet;

  /**
   * Constructor receiving a Wavelet object.
   * 
   * @date 10.02.2010 08:10:42
   * @author Christian Scheiblich
   * @param wavelet
   *          object of type Wavelet; Haar02, Daub02, Coif06, ...
   */
  public FastWaveletTransform( Wavelet wavelet ) {
    _wavelet = wavelet;
  } // FastWaveletTransform

  /**
   * Performs the 1-D forward transform for arrays of dim N from time domain to
   * Hilbert domain for the given array using the Fast Wavelet Transform (FWT)
   * algorithm.
   * 
   * @date 10.02.2010 08:23:24
   * @author Christian Scheiblich
   * @see cs.jwave.handlers.BasicTransform#forward(double[])
   */
  @Override
  public double[ ] forward( double[ ] arrTime ) {

    double[ ] arrHilb = new double[ arrTime.length ];
    for( int i = 0; i < arrTime.length; i++ )
      arrHilb[ i ] = arrTime[ i ];

    //int level = 0;
    int h = arrTime.length;
    int minWaveLength = _wavelet.getWaveLength( );
    if( h >= minWaveLength ) {

      while( h >= minWaveLength ) {

        double[ ] iBuf = new double[ h ];

        for( int i = 0; i < h; i++ )
          iBuf[ i ] = arrHilb[ i ];

        double[ ] oBuf = _wavelet.forward( iBuf );

        for( int i = 0; i < h; i++ )
          arrHilb[ i ] = oBuf[ i ];

        h = h >> 1;

      //  level++;

      } // levels

    } // if

    return arrHilb;
  } // forward

  /**
   * Performs the 1-D reverse transform for arrays of dim N from Hilbert domain
   * to time domain for the given array using the Fast Wavelet Transform (FWT)
   * algorithm and the selected wavelet.
   * 
   * @date 10.02.2010 08:23:24
   * @author Christian Scheiblich
   * @see cs.jwave.handlers.BasicTransform#reverse(double[])
   */
  @Override
  public double[ ] reverse( double[ ] arrHilb ) {

    double[ ] arrTime = new double[ arrHilb.length ];

    for( int i = 0; i < arrHilb.length; i++ )
      arrTime[ i ] = arrHilb[ i ];

    //int level = 0;
    int minWaveLength = _wavelet.getWaveLength( );
    int h = minWaveLength;
    if( arrHilb.length >= minWaveLength ) {

      while( h <= arrTime.length && h >= minWaveLength ) {

        double[ ] iBuf = new double[ h ];

        for( int i = 0; i < h; i++ )
          iBuf[ i ] = arrTime[ i ];

        double[ ] oBuf = _wavelet.reverse( iBuf );

        for( int i = 0; i < h; i++ )
          arrTime[ i ] = oBuf[ i ];

        h = h << 1;

      //  level++;

      } // levels

    } // if

    return arrTime;
  } // reverse

  /**
   * Performs the 1-D forward transform for arrays of dim N from time domain to
   * Hilbert domain for the given array using the Fast Wavelet Transform (FWT)
   * algorithm. The number of transformation levels applied is limited by
   * threshold.
   * 
   * @date 15.07.2010 13:26:26
   * @author Thomas Haider
   * @date 15.08.2010 00:31:36
   * @author Christian Scheiblich
   * @see cs.jwave.handlers.BasicTransform#forward(double[], int)
   */
  @Override
  public double[ ] forward( double[ ] arrTime, int toLevel ) {

    double[ ] arrHilb = new double[ arrTime.length ];
    for( int i = 0; i < arrTime.length; i++ )
      arrHilb[ i ] = arrTime[ i ];

    int level = 0;
    int h = arrTime.length;
    int minWaveLength = _wavelet.getWaveLength( );
    if( h >= minWaveLength ) {

      while( h >= minWaveLength && level < toLevel ) {

        double[ ] iBuf = new double[ h ];

        for( int i = 0; i < h; i++ )
          iBuf[ i ] = arrHilb[ i ];

        double[ ] oBuf = _wavelet.forward( iBuf );

        for( int i = 0; i < h; i++ )
          arrHilb[ i ] = oBuf[ i ];

        h = h >> 1;

        level++;

      } // levels

    } // if

    return arrHilb;
  } // forward

  /**
   * Performs the 1-D reverse transform for arrays of dim N from Hilbert domain
   * to time domain for the given array using the Fast Wavelet Transform (FWT)
   * algorithm and the selected wavelet. The number of transformation levels
   * applied is limited by threshold.
   * 
   * @date 15.07.2010 13:28:06
   * @author Thomas Haider
   * @date 15.08.2010 00:31:09
   * @author Christian Scheiblich
   * @see cs.jwave.handlers.BasicTransform#reverse(double[], int)
   */
  @Override
  public double[ ] reverse( double[ ] arrHilb, int fromLevel ) {

    double[ ] arrTime = new double[ arrHilb.length ];

    for( int i = 0; i < arrHilb.length; i++ )
      arrTime[ i ] = arrHilb[ i ];

    int level = 0;
    int minWaveLength = _wavelet.getWaveLength( );
    int h = minWaveLength;
    if( arrHilb.length >= minWaveLength ) {

      while( h <= arrTime.length && h >= minWaveLength && level < fromLevel ) {

        double[ ] iBuf = new double[ h ];

        for( int i = 0; i < h; i++ )
          iBuf[ i ] = arrTime[ i ];

        double[ ] oBuf = _wavelet.reverse( iBuf );

        for( int i = 0; i < h; i++ )
          arrTime[ i ] = oBuf[ i ];

        h = h << 1;

        level++;

      } // levels

    } // if

    return arrTime;
  } // reverse

  @Override
  public double[ ][ ] wtstnst( double[ ] arrTime, double threshold ) {
    
    double[ ][ ] arrHilb = new double[3][ arrTime.length ];
    for( int i = 0; i < arrTime.length; i++ ){
      //arrHilb[0][ i ] = arrTime[ i ]; // < threshold
      //arrHilb[1][ i ] = arrTime[ i ]; // > threshold
      arrHilb[2][ i ] = arrTime[ i ]; // WT     
    }

    //int level = 0;
    int h = arrTime.length;
    int minWaveLength = _wavelet.getWaveLength( );
    if( h >= minWaveLength ) {

      while( h >= minWaveLength ) {

        double[] iBuf = new double[ h ];

        for( int i = 0; i < h; i++ ){
          iBuf[ i ] = arrHilb[2][ i ];
        }

        double[ ][ ] oBuf = _wavelet.wtstnst( iBuf , threshold );

        for( int i = 0; i < h; i++ ){
          arrHilb[0][ i ] = oBuf[0][ i ]; // < threshold
          arrHilb[1][ i ] = oBuf[1][ i ]; // > threshold
          arrHilb[2][ i ] = oBuf[2][ i ]; // WT     
        }

        h = h >> 1;

       // level++;

      } // levels

    } // if
    return arrHilb;
  }

  public HashMap< String, double[ ] > forward_ext( double[ ] arrTime, int toLevel ) {

    HashMap< String, double[ ] > result = new HashMap< String, double[ ] >( );

    int level = 0;
    int h = arrTime.length;
    int minWaveLength = _wavelet.getWaveLength( );
    if( h >= minWaveLength ) {

      double[ ] iBuf;
      double[ ] oBuf;

      iBuf = arrTime;

      while( h >= minWaveLength && level < toLevel ) {

        oBuf = _wavelet.forward_ext( iBuf );

        h = oBuf.length / 2;
        iBuf = new double[ h ];

        for( int i = 0; i < h; i++ )
          iBuf[ i ] = oBuf[ i + h ];
        
        result.put( "D" + (level+1), iBuf );

        iBuf = new double[ h ];
        for( int i = 0; i < h; i++ )
          iBuf[ i ] = oBuf[ i ];

        result.put( "A" + (level+1), iBuf );

        level++;

      } // levels      
      
    } // if
    
    if(level!=toLevel){
      System.err.println("It was not possible to compute all levels due");
      System.err.println("the data samples are smaller than the wavelet length.");
      System.err.print("Wavelet:" + _wavelet.getClass( ).getName( ));
      System.err.print(" / Wavelet length:" + _wavelet.getWaveLength( ));
      System.err.println(" / Data size:"+ h + " at level:" + (level+1));
    }
    return result;
  }
  
  public void setBoundary(int boundary){
    _wavelet.setBoundary( boundary );
  }

  public int getBoundary( ){
    return _wavelet.getBoundary( );
  }
} // class
