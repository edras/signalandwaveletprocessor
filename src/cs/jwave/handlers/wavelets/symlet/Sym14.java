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
 * This file Coif06.java is part of JWave.
 *
 * @author Christian Scheiblich
 * date 23.02.2010 05:42:23
 * contact source@linux23.de
 */
package cs.jwave.handlers.wavelets.symlet;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Ingrid Daubechies' orthonormal Symlet wavelet of twenty-eight coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym14 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet14 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym14( ) {

    _waveLength = 28; // minimal array size for transform

    double[ ] scales = {
        4.4618977991475265e-05,
        1.9329016965523917e-05,
        -0.00060576018246643346,
        -7.3214213567023991e-05,
        0.0045326774719456481,
        0.0010131419871842082,
        -0.019439314263626713,
        -0.0023650488367403851,
        0.069827616361807551,
        0.025898587531046669,
        -0.15999741114652205,
        -0.058111823317717831,
        0.47533576263420663,
        0.75997624196109093,
        0.39320152196208885,
        -0.035318112114979733,
        -0.057634498351326995,
        0.037433088362853452,
        0.0042805204990193782,
        -0.029196217764038187,
        -0.0027537747912240711,
        0.010037693717672269,
        0.00036647657366011829,
        -0.002579441725933078,
        -6.2865424814776362e-05,
        0.00039843567297594335,
        1.1210865808890361e-05,
        -2.5879090265397886e-05
    };
    
    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = 0; i < _waveLength; i++ )
      _scales[ i ] = scales[ i ];
    
    _coeffs = new double[ _waveLength ]; 

    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _coeffs[ i ] = _scales[ ( _waveLength - 1 ) - i ];
      } else {
        _coeffs[ i ] = -_scales[ ( _waveLength - 1 ) - i ];
      } // if

  } // Sym14

} // class
