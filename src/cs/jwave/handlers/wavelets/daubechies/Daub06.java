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
 * Ingrid Daubechies orthonormalized wavelet using 12 coefficients.
 * 
 * @date 29.04.2011 21:53:00
 * @author Edras Pacola
 */
public class Daub06 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using 12 coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub06( ) {

    _waveLength = 12;

    double[ ] scales = {
        1.115407433501094636213239172409234390425395919844216759082360e-01,
        4.946238903984530856772041768778555886377863828962743623531834e-01,
        7.511339080210953506789344984397316855802547833382612009730420e-01,
        3.152503517091976290859896548109263966495199235172945244404163e-01,
       -2.262646939654398200763145006609034656705401539728969940143487e-01,
       -1.297668675672619355622896058765854608452337492235814701599310e-01,
        9.750160558732304910234355253812534233983074749525514279893193e-02,
        2.752286553030572862554083950419321365738758783043454321494202e-02,
       -3.158203931748602956507908069984866905747953237314842337511464e-02,
        5.538422011614961392519183980465012206110262773864964295476524e-04,
        4.777257510945510639635975246820707050230501216581434297593254e-03,
       -1.077301085308479564852621609587200035235233609334419689818580e-03};

    _scales = new double[ _waveLength ];

    for( int i = 0; i < _waveLength; i++ )
      _scales[ i ] = scales[ i ];

    _coeffs = new double[ _waveLength ]; 

    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _coeffs[ i ] = _scales[ ( _waveLength - 1 ) - i ];
      } else {
        _coeffs[ i ] = -_scales[ ( _waveLength - 1 ) - i ];
      } // if

  } // Daub06

} // class
