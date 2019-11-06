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
 * Ingrid Daubechies' orthonormal Symlet wavelet of fourthy coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Sym20 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Symlet20 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Sym20( ) {

    _waveLength = 40; // minimal array size for transform

    double[ ] scales = {
        -6.3291290447763946e-07,
        -3.2567026420174407e-07,
        1.22872527779612e-05,
        4.5254222091516362e-06,
        -0.00011739133516291466,
        -2.6615550335516086e-05,
        0.00074761085978205719,
        0.00012544091723067259,
        -0.0034716478028440734,
        -0.0006111263857992088,
        0.012157040948785737,
        0.0019385970672402002,
        -0.035373336756604236,
        -0.0068437019650692274,
        0.088919668028199561,
        0.036250951653933078,
        -0.16057829841525254,
        -0.051088342921067398,
        0.47199147510148703,
        0.75116272842273002,
        0.40583144434845059,
        -0.029819368880333728,
        -0.078994344928398158,
        0.025579349509413946,
        0.0081232283560096815,
        -0.031629437144957966,
        -0.0033138573836233591,
        0.017004049023390339,
        0.0014230873594621453,
        -0.0066065857990888609,
        -0.0003052628317957281,
        0.0020889947081901982,
        7.2159911880740349e-05,
        -0.00049473109156726548,
        -1.928412300645204e-05,
        7.992967835772481e-05,
        3.0256660627369661e-06,
        -7.919361411976999e-06,
        -1.9015675890554106e-07,
        3.695537474835221e-07
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

  } // Sym20

} // class
