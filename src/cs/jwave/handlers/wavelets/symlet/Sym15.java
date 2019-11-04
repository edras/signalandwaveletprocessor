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
 * Ingrid Daubechies' orthonormal Symlet wavelet of thirty coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym15 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet15 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym15( ) {

    _waveLength = 30; // minimal array size for transform

    double[ ] scales = {
        2.8660708525318081e-05,
        2.1717890150778919e-05,
        -0.00040216853760293483,
        -0.00010815440168545525,
        0.003481028737064895,
        0.0015261382781819983,
        -0.017171252781638731,
        -0.0087447888864779517,
        0.067969829044879179,
        0.068393310060480245,
        -0.13405629845625389,
        -0.1966263587662373,
        0.2439627054321663,
        0.72184302963618119,
        0.57864041521503451,
        0.11153369514261872,
        -0.04108266663538248,
        0.040735479696810677,
        0.021937642719753955,
        -0.038876716876833493,
        -0.019405011430934468,
        0.010079977087905669,
        0.003423450736351241,
        -0.0035901654473726417,
        -0.00026731644647180568,
        0.0010705672194623959,
        5.5122547855586653e-05,
        -0.00016066186637495343,
        -7.3596667989194696e-06,
        9.7124197379633478e-06
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

  } // Sym15

} // class
