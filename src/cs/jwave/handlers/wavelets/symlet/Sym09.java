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
 * Ingrid Daubechies' orthonormal Symlet wavelet of eigtheen coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym09 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet09 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym09( ) {

    _waveLength = 18; // minimal array size for transform

    double[ ] scales = {
        0.0010694900329086053,
        -0.00047315449868008311,
        -0.010264064027633142,
        0.0088592674934004842,
        0.06207778930288603,
        -0.018233770779395985,
        -0.19155083129728512,
        0.035272488035271894,
        0.61733844914093583,
        0.717897082764412,
        0.238760914607303,
        -0.054568958430834071,
        0.00058346274612580684,
        0.03022487885827568,
        -0.01152821020767923,
        -0.013271967781817119,
        0.00061978088898558676,
        0.0014009155259146807
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

  } // Sym09

} // class
