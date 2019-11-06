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
public class Daub19 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub19( ) {

    _waveLength = 38;

    double[ ] scales = {
        1.108669763181710571099154195209715164245299677773435932135455e-03,
        1.428109845076439737439889152950199234745663442163665957870715e-02,
        8.127811326545955065296306784901624839844979971028620366497726e-02,
        2.643884317408967846748100380289426873862377807211920718417385e-01,
        5.244363774646549153360575975484064626044633641048072116393160e-01,
        6.017045491275378948867077135921802620536565639585963293313931e-01,
        2.608949526510388292872456675310528324172673101301907739925213e-01,
       -2.280913942154826463746325776054637207093787237086425909534822e-01,
       -2.858386317558262418545975695028984237217356095588335149922119e-01,
        7.465226970810326636763433111878819005865866149731909656365399e-02,
        2.123497433062784888090608567059824197077074200878839448416908e-01,
       -3.351854190230287868169388418785731506977845075238966819814032e-02,
       -1.427856950387365749779602731626112812998497706152428508627562e-01,
        2.758435062562866875014743520162198655374474596963423080762818e-02,
        8.690675555581223248847645428808443034785208002468192759640352e-02,
       -2.650123625012304089901835843676387361075068017686747808171345e-02,
       -4.567422627723090805645444214295796017938935732115630050880109e-02,
        2.162376740958504713032984257172372354318097067858752542571020e-02,
        1.937554988917612764637094354457999814496885095875825546406963e-02,
       -1.398838867853514163250401235248662521916813867453095836808366e-02,
       -5.866922281012174726584493436054373773814608340808758177372765e-03,
        7.040747367105243153014511207400620109401689897665383078229398e-03,
        7.689543592575483559749139148673955163477947086039406129546422e-04,
       -2.687551800701582003957363855070398636534038920982478290170267e-03,
        3.418086534585957765651657290463808135214214848819517257794031e-04,
        7.358025205054352070260481905397281875183175792779904858189494e-04,
       -2.606761356786280057318315130897522790383939362073563408613547e-04,
       -1.246007917341587753449784408901653990317341413341980904757592e-04,
        8.711270467219922965416862388191128268412933893282083517729443e-05,
        5.105950487073886053049222809934231573687367992106282669389264e-06,
       -1.664017629715494454620677719899198630333675608812018108739144e-05,
        3.010964316296526339695334454725943632645798938162427168851382e-06,
        1.531931476691193063931832381086636031203123032723477463624141e-06,
       -6.862755657769142701883554613486732854452740752771392411758418e-07,
        1.447088298797844542078219863291615420551673574071367834316167e-08,
        4.636937775782604223430857728210948898871748291085962296649320e-08,
       -1.116402067035825816390504769142472586464975799284473682246076e-08,
        8.666848838997619350323013540782124627289742190273059319122840e-10
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
