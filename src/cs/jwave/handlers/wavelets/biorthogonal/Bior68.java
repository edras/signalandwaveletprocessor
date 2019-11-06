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
public class Bior68 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Bior68 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Bior68( ) {

    _waveLength = 18; // minimal array size for transform

    double[ ] scales = {
        0.0,
        0.0019088317364812906,
        -0.0019142861290887667,
        -0.016990639867602342,
        0.01193456527972926,
        0.04973290349094079,
        -0.077263173167204144,
        -0.09405920349573646,
        0.42079628460982682,
        0.82592299745840225,
        0.42079628460982682,
        -0.09405920349573646,
        -0.077263173167204144,
        0.04973290349094079,
        0.01193456527972926,
        -0.016990639867602342,
        -0.0019142861290887667,
        0.0019088317364812906
    };
    
    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = _waveLength-1; i > -1; i-- )
      _scales[ _waveLength-1-i ] = scales[ i ];
    
    _coeffs = new double[ _waveLength ]; 

    double[ ] coefs = {
        0.0,
        0.0,
        0.0,
        0.014426282505624435,
        -0.014467504896790148,
        -0.078722001062628819,
        0.040367979030339923,
        0.41784910915027457,
        -0.75890772945365415,
        0.41784910915027457,
        0.040367979030339923,
        -0.078722001062628819,
        -0.014467504896790148,
        0.014426282505624435,
        0.0,
        0.0,
        0.0,
        0.0
    };

    for( int i = _waveLength-1; i > -1; i-- )
      _coeffs[ _waveLength-1-i ] = coefs[ i ];

  } // Bior68
  
} // class
