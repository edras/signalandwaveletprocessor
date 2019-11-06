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
 * Ingrid Daubechies' orthonormal Symlet wavelet of twenty-four coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym12 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet12 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym12( ) {

    _waveLength = 24; // minimal array size for transform

    double[ ] scales = {
        -0.00017906658697508691,
        -1.8158078862617515e-05,
        0.0023502976141834648,
        0.00030764779631059454,
        -0.014589836449234145,
        -0.0026043910313322326,
        0.057804179445505657,
        0.01530174062247884,
        -0.17037069723886492,
        -0.07833262231634322,
        0.46274103121927235,
        0.76347909778365719,
        0.39888597239022,
        -0.022162306170337816,
        -0.035848830736954392,
        0.049179318299660837,
        0.0075537806116804775,
        -0.024220722675013445,
        -0.0014089092443297553,
        0.007414965517654251,
        0.00018021409008538188,
        -0.0013497557555715387,
        -1.1353928041541452e-05,
        0.00011196719424656033
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

  } // Sym12

} // class
