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
 * Ingrid Daubechies orthonormalized wavelet using XX coefficients.
 * 
 * @date 29.04.2011 21:53:00
 * @author Edras Pacola
 */
public class Daub11 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub11( ) {

    _waveLength = 22;

    double[ ] scales = {
        1.869429776147108402543572939561975728967774455921958543286692e-02,
        1.440670211506245127951915849361001143023718967556239604318852e-01,
        4.498997643560453347688940373853603677806895378648933474599655e-01,
        6.856867749162005111209386316963097935940204964567703495051589e-01,
        4.119643689479074629259396485710667307430400410187845315697242e-01,
       -1.622752450274903622405827269985511540744264324212130209649667e-01,
       -2.742308468179469612021009452835266628648089521775178221905778e-01,
        6.604358819668319190061457888126302656753142168940791541113457e-02,
        1.498120124663784964066562617044193298588272420267484653796909e-01,
       -4.647995511668418727161722589023744577223260966848260747450320e-02,
       -6.643878569502520527899215536971203191819566896079739622858574e-02,
        3.133509021904607603094798408303144536358105680880031964936445e-02,
        2.084090436018106302294811255656491015157761832734715691126692e-02,
       -1.536482090620159942619811609958822744014326495773000120205848e-02,
       -3.340858873014445606090808617982406101930658359499190845656731e-03,
        4.928417656059041123170739741708273690285547729915802418397458e-03,
       -3.085928588151431651754590726278953307180216605078488581921562e-04,
       -8.930232506662646133900824622648653989879519878620728793133358e-04,
        2.491525235528234988712216872666801088221199302855425381971392e-04,
        5.443907469936847167357856879576832191936678525600793978043688e-05,
       -3.463498418698499554128085159974043214506488048233458035943601e-05,
        4.494274277236510095415648282310130916410497987383753460571741e-06
    };

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

  } // Daub08

} // class
