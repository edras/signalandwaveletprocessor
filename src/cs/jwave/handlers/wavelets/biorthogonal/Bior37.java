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
package cs.jwave.handlers.wavelets.biorthogonal;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Ingrid Daubechies' orthonormal Biorthogonal wavelet of sixteen coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Bior37 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Bior37 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Bior37( ) {

    _waveLength = 16; // minimal array size for transform

    double[ ] scales = {
        0.0030210861012608843,
        -0.0090632583037826529,
        -0.016831765421310641,
        0.074663985074019001,
        0.031332978707362888,
        -0.301159125922835,
        -0.026499240945345472,
        0.95164212189717856,
        0.95164212189717856,
        -0.026499240945345472,
        -0.301159125922835,
        0.031332978707362888,
        0.074663985074019001,
        -0.016831765421310641,
        -0.0090632583037826529,
        0.0030210861012608843
    };
    
    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = _waveLength-1; i > -1; i-- )
      _scales[ _waveLength-1-i ] = scales[ i ];
    
    _coeffs = new double[ _waveLength ]; 

    double[ ] coefs = {
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        -0.17677669529663689,
        0.53033008588991071,
        -0.53033008588991071,
        0.17677669529663689,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
    };

    for( int i = _waveLength-1; i > -1; i-- )
      _coeffs[ _waveLength-1-i ] = coefs[ i ];

  } // Bior37

} // class
