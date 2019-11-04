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
 * Ingrid Daubechies' orthonormal Symlet wavelet of twenty-two coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym11 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet11 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym11( ) {

    _waveLength = 22; // minimal array size for transform

    double[ ] scales = {
        0.00048926361026192387,
        0.00011053509764272153,
        -0.0063896036664548919,
        -0.0020034719001093887,
        0.043000190681552281,
        0.035266759564466552,
        -0.14460234370531561,
        -0.2046547944958006,
        0.23768990904924897,
        0.73034354908839572,
        0.57202297801008706,
        0.097198394458909473,
        -0.022832651022562687,
        0.069976799610734136,
        0.0370374159788594,
        -0.024080841595864003,
        -0.0098579348287897942,
        0.0065124956747714497,
        0.00058835273539699145,
        -0.0017343662672978692,
        -3.8795655736158566e-05,
        0.00017172195069934854
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

  } // Sym11

} // class
