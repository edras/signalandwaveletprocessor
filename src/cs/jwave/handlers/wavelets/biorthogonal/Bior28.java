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
 * Ingrid Daubechies' orthonormal Biorthogonal wavelet of eighteen coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Bior28 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Bior28 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Bior28( ) {

    _waveLength = 18; // minimal array size for transform

    double[ ] scales = {
        0.0,
        0.0015105430506304422,
        -0.0030210861012608843,
        -0.012947511862546647,
        0.028916109826354178,
        0.052998481890690945,
        -0.13491307360773608,
        -0.16382918343409025,
        0.46257144047591658,
        0.95164212189717856,
        0.46257144047591658,
        -0.16382918343409025,
        -0.13491307360773608,
        0.052998481890690945,
        0.028916109826354178,
        -0.012947511862546647,
        -0.0030210861012608843,
        0.0015105430506304422
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
        0.0,
        0.35355339059327379,
        -0.70710678118654757,
        0.35355339059327379,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
    };

    for( int i = _waveLength-1; i > -1; i-- )
      _coeffs[ _waveLength-1-i ] = coefs[ i ];

  } // Bior28

} // class
