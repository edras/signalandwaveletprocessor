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
 * Ingrid Daubechies' orthonormal Symlet wavelet of thirty-four coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym17 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet17 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym17( ) {

    _waveLength = 34; // minimal array size for transform

    double[ ] scales = {
        3.7912531943321266e-06,
        -2.4527163425832999e-06,
        -7.6071244056051285e-05,
        2.5207933140828779e-05,
        0.0007198270642148971,
        5.8400428694052584e-05,
        -0.0039323252797979023,
        -0.0019054076898526659,
        0.012396988366648726,
        0.0099529825235095976,
        -0.01803889724191924,
        -0.0072616347509287674,
        0.016158808725919346,
        -0.086070874720733381,
        -0.15507600534974825,
        0.18053958458111286,
        0.68148899534492502,
        0.65071662920454565,
        0.14239835041467819,
        -0.11856693261143636,
        0.0172711782105185,
        0.10475461484223211,
        0.017903952214341119,
        -0.033291383492359328,
        -0.0048192128031761478,
        0.010482366933031529,
        0.0008567700701915741,
        -0.0027416759756816018,
        -0.00013864230268045499,
        0.0004759963802638669,
        -1.3506383399901165e-05,
        -6.2937025975541919e-05,
        2.7801266938414138e-06,
        4.297343327345983e-06
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

  } // Sym17

} // class
