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
 * Ingrid Daubechies' orthonormal Symlet wavelet of thirty-six coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym18 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet18 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym18( ) {

    _waveLength = 36; // minimal array size for transform

    double[ ] scales = {
        -1.5131530692371587e-06,
        7.8472980558317646e-07,
        2.9557437620930811e-05,
        -9.858816030140058e-06,
        -0.00026583011024241041,
        4.7416145183736671e-05,
        0.0014280863270832796,
        -0.00018877623940755607,
        -0.0052397896830266083,
        0.0010877847895956929,
        0.015012356344250213,
        -0.0032607442000749834,
        -0.031712684731814537,
        0.0062779445543116943,
        0.028529597039037808,
        -0.073799207290607169,
        -0.032480573290138676,
        0.40148386057061813,
        0.75362914010179283,
        0.47396905989393956,
        -0.052029158983952786,
        -0.15993814866932407,
        0.033995667103947358,
        0.084219929970386548,
        -0.0050770851607570529,
        -0.030325091089369604,
        0.0016429863972782159,
        0.0095021643909623654,
        -0.00041152110923597756,
        -0.0023138718145060992,
        7.0212734590362685e-05,
        0.00039616840638254753,
        -1.4020992577726755e-05,
        -4.5246757874949856e-05,
        1.354915761832114e-06,
        2.6126125564836423e-06
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

  } // Sym18

} // class
