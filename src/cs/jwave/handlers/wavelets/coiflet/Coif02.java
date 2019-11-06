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
package cs.jwave.handlers.wavelets.coiflet;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Ingrid Daubechies' orthonormal Coiflet wavelet of twelve coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Coif02 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Coiflet2 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Coif02( ) {

    _waveLength = 12; // minimal array size for transform

    double[ ] scales = {
        -0.00072054944536451221,
        -0.0018232088707029932,
        0.0056114348193944995,
        0.023680171946334084,
        -0.059434418646456898,
        -0.076488599078306393,
        0.41700518442169254,
        0.81272363544554227,
        0.38611006682116222,
        -0.067372554721963018,
        -0.041464936781759151,
        0.016387336463522112    
    };
    
    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = _waveLength-1; i > -1; i-- )
      _scales[ _waveLength-1-i ] = scales[ i ];
    
    _coeffs = new double[ _waveLength ]; 

    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _coeffs[ i ] = _scales[ ( _waveLength - 1 ) - i ];
      } else {
        _coeffs[ i ] = -_scales[ ( _waveLength - 1 ) - i ];
      } // if

  } // Coif02

} // class
