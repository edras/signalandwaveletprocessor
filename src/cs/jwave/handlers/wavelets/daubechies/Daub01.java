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
 * This file Daub02.java is part of JWave.
 *
 * @author Christian Scheiblich
 * date 23.02.2010 05:42:23
 * contact source@linux23.de
 */
package cs.jwave.handlers.wavelets.daubechies;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Ingrid Daubechies' orthonormal wavelet of two coefficients and the scales;
 * normed, due to ||*||2 - euclidean norm.
 * 
 * @date 29.04.2011 21:48:00
 * @author Edras Pacola
 */
public class Daub01 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Daubechies2 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 29.04.2011 21:48:00
   * @author Edras Pacola
   */
  public Daub01( ) {

    _waveLength = 2;

    _scales = new double[ _waveLength ]; // can be done in static way also; faster?

    _scales[ 0 ] = 7.071067811865475244008443621048490392848359376884740365883398e-01;
    _scales[ 1 ] = 7.071067811865475244008443621048490392848359376884740365883398e-01;

    _coeffs = new double[ _waveLength ]; // can be done in static way also; faster?

    _coeffs[ 0 ] = -_scales[ 1 ]; //     h1
    _coeffs[ 1 ] =  _scales[ 0 ]; //     h0

  } // Daub01

} // class
