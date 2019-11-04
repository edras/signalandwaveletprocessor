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
 * Ingrid Daubechies' orthonormal Symlet wavelet of thirty-two coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym16 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet16 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym16( ) {

    _waveLength = 32; // minimal array size for transform

    double[ ] scales = {
        -1.0797982104319795e-05,
        -5.3964831793152419e-06,
        0.00016545679579108483,
        3.656592483348223e-05,
        -0.0013387206066921965,
        -0.00022211647621176323,
        0.0069377611308027096,
        0.001359844742484172,
        -0.024952758046290123,
        -0.0035102750683740089,
        0.078037852903419913,
        0.03072113906330156,
        -0.15959219218520598,
        -0.054040601387606135,
        0.47534280601152273,
        0.75652498787569711,
        0.39712293362064416,
        -0.034574228416972504,
        -0.066983049070217779,
        0.032333091610663785,
        0.0048692744049046071,
        -0.031051202843553064,
        -0.0031265171722710075,
        0.012666731659857348,
        0.00071821197883178923,
        -0.0038809122526038786,
        -0.0001084456223089688,
        0.00085235471080470952,
        2.8078582128442894e-05,
        -0.00010943147929529757,
        -3.1135564076219692e-06,
        6.2300067012207606e-06
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

  } // Sym16

} // class
