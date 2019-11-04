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
 * Ingrid Daubechies' orthonormal Symlet wavelet of thirty-eight coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym19 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet19 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym19( ) {

    _waveLength = 38; // minimal array size for transform

    double[ ] scales = {
        1.7509367995348687e-06,
        2.0623170632395688e-06,
        -2.8151138661550245e-05,
        -1.6821387029373716e-05,
        0.00027621877685734072,
        0.00012930767650701415,
        -0.0017049602611649971,
        -0.00061792232779831076,
        0.0082622369555282547,
        0.0043193518748949689,
        -0.027709896931311252,
        -0.016908234861345205,
        0.084072676279245043,
        0.093630843415897141,
        -0.11624173010739675,
        -0.17659686625203097,
        0.25826616923728363,
        0.71955552571639425,
        0.57814494533860505,
        0.10902582508127781,
        -0.067525058040294086,
        0.0089545911730436242,
        0.0070155738571741596,
        -0.046635983534938946,
        -0.022651993378245951,
        0.015797439295674631,
        0.0079684383206133063,
        -0.005122205002583014,
        -0.0011607032572062486,
        0.0021214250281823303,
        0.00015915804768084938,
        -0.00063576451500433403,
        -4.6120396002105868e-05,
        0.0001155392333357879,
        8.8733121737292863e-06,
        -1.1880518269823984e-05,
        -6.4636513033459633e-07,
        5.4877327682158382e-07
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

  } // Sym19

} // class
