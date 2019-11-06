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
 * Ingrid Daubechies' orthonormal Symlet wavelet of twenty-six coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym13 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet13 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym13( ) {

    _waveLength = 26; // minimal array size for transform

    double[ ] scales = {
        7.0429866906944016e-05,
        3.6905373423196241e-05,
        -0.0007213643851362283,
        0.00041326119884196064,
        0.0056748537601224395,
        -0.0014924472742598532,
        -0.020749686325515677,
        0.017618296880653084,
        0.092926030899137119,
        0.0088197576704205465,
        -0.14049009311363403,
        0.11023022302137217,
        0.64456438390118564,
        0.69573915056149638,
        0.19770481877117801,
        -0.12436246075153011,
        -0.059750627717943698,
        0.013862497435849205,
        -0.017211642726299048,
        -0.02021676813338983,
        0.0052963597387250252,
        0.0075262253899680996,
        -0.00017094285853022211,
        -0.0011360634389281183,
        -3.5738623648689009e-05,
        6.8203252630753188e-05
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

  } // Sym13

} // class
