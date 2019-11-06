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
 * This file Daub38.java is part of JWave.
 *
 * @author itechsch
 * date 19.10.2010 15:24:26
 * contact source@linux23.de
 */
package cs.jwave.handlers.wavelets.daubechies;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Ingrid Daubechies orthonormalized wavelet using 10 coefficients.
 * 
 * @date 29.04.2011 21:53:00
 * @author Edras Pacola
 */
public class Daub05 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using 10 coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub05( ) {

    _waveLength = 10;

    _scales = new double[ _waveLength ]; 

    double[ ] scales = {
        1.601023979741929144807237480204207336505441246250578327725699e-01,
        6.038292697971896705401193065250621075074221631016986987969283e-01,
        7.243085284377729277280712441022186407687562182320073725767335e-01,
        1.384281459013207315053971463390246973141057911739561022694652e-01,
        -2.422948870663820318625713794746163619914908080626185983913726e-01,
        -3.224486958463837464847975506213492831356498416379847225434268e-02,
        7.757149384004571352313048938860181980623099452012527983210146e-02,
        -6.241490212798274274190519112920192970763557165687607323417435e-03,
        -1.258075199908199946850973993177579294920459162609785020169232e-02,
        3.335725285473771277998183415817355747636524742305315099706428e-03};

    for( int i = 0; i < _waveLength; i++ )
      _scales[ i ] = scales[ i ];

    _coeffs = new double[ _waveLength ]; 

    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _coeffs[ i ] = _scales[ ( _waveLength - 1 ) - i ];
      } else {
        _coeffs[ i ] = -_scales[ ( _waveLength - 1 ) - i ];
      } // if

  } // Daub05

} // class
