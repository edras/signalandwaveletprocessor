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
public class Daub12 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub12( ) {

    _waveLength = 24;

    double[ ] scales = {
        1.311225795722951750674609088893328065665510641931325007748280e-02,
        1.095662728211851546057045050248905426075680503066774046383657e-01,
        3.773551352142126570928212604879206149010941706057526334705839e-01,
        6.571987225793070893027611286641169834250203289988412141394281e-01,
        5.158864784278156087560326480543032700677693087036090056127647e-01,
       -4.476388565377462666762747311540166529284543631505924139071704e-02,
       -3.161784537527855368648029353478031098508839032547364389574203e-01,
       -2.377925725606972768399754609133225784553366558331741152482612e-02,
        1.824786059275796798540436116189241710294771448096302698329011e-01,
        5.359569674352150328276276729768332288862665184192705821636342e-03,
       -9.643212009650708202650320534322484127430880143045220514346402e-02,
        1.084913025582218438089010237748152188661630567603334659322512e-02,
        4.154627749508444073927094681906574864513532221388374861287078e-02,
       -1.221864906974828071998798266471567712982466093116558175344811e-02,
       -1.284082519830068329466034471894728496206109832314097633275225e-02,
        6.711499008795509177767027068215672450648112185856456740379455e-03,
        2.248607240995237599950865211267234018343199786146177099262010e-03,
       -2.179503618627760471598903379584171187840075291860571264980942e-03,
        6.545128212509595566500430399327110729111770568897356630714552e-06,
        3.886530628209314435897288837795981791917488573420177523436096e-04,
       -8.850410920820432420821645961553726598738322151471932808015443e-05,
       -2.424154575703078402978915320531719580423778362664282239377532e-05,
        1.277695221937976658714046362616620887375960941439428756055353e-05,
       -1.529071758068510902712239164522901223197615439660340672602696e-06
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
