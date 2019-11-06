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
 * Ingrid Daubechies' orthonormal Biorthogonal wavelet of ten coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Bior44 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Bior44 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Bior44( ) {

    _waveLength = 10; // minimal array size for transform

    double[ ] scales = {
        0.0,
        0.03782845550726404,
        -0.023849465019556843,
        -0.11062440441843718,
        0.37740285561283066,
        0.85269867900889385,
        0.37740285561283066,
        -0.11062440441843718,
        -0.023849465019556843,
        0.03782845550726404
    };
    
    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = _waveLength-1; i > -1; i-- )
      _scales[ _waveLength-1-i ] = scales[ i ];
    
    _coeffs = new double[ _waveLength ]; 

    double[ ] coefs = {
        0.0,
        -0.064538882628697058,
        0.040689417609164058,
        0.41809227322161724,
        -0.7884856164055829,
        0.41809227322161724,
        0.040689417609164058,
        -0.064538882628697058,
        0.0,
        0.0
    };

    for( int i = _waveLength-1; i > -1; i-- )
      _coeffs[ _waveLength-1-i ] = coefs[ i ];

  } // Bior44

} // class
