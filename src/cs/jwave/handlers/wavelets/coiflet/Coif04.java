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
 * Ingrid Daubechies' orthonormal Coiflet wavelet of twenty four coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Coif04 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Coiflet3 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Coif04( ) {

    _waveLength = 24; // minimal array size for transform

    double[ ] scales = {
        -1.7849850030882614e-06,
        -3.2596802368833675e-06,
        3.1229875865345646e-05,
        6.2339034461007128e-05,
        -0.00025997455248771324,
        -0.00058902075624433831,
        0.0012665619292989445,
        0.0037514361572784571,
        -0.0056582866866107199,
        -0.015211731527946259,
        0.025082261844864097,
        0.039334427123337491,
        -0.096220442033987982,
        -0.066627474263425038,
        0.4343860564914685,
        0.78223893092049901,
        0.41530840703043026,
        -0.056077313316754807,
        -0.081266699680878754,
        0.026682300156053072,
        0.016068943964776348,
        -0.0073461663276420935,
        -0.0016294920126017326,
        0.00089231366858231456
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

  } // Coif04

} // class
