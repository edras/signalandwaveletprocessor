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
public class Daub14 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub14( ) {

    _waveLength = 28;

    double[ ] scales = {
        6.461153460087947818166397448622814272327159419201199218101404e-03,
        6.236475884939889832798566758434877428305333693407667164602518e-02,
        2.548502677926213536659077886778286686187042416367137443780084e-01,
        5.543056179408938359926831449851154844078269830951634609683997e-01,
        6.311878491048567795576617135358172348623952456570017289788809e-01,
        2.186706877589065214917475918217517051765774321270432059030273e-01,
       -2.716885522787480414142192476181171094604882465683330814311896e-01,
       -2.180335299932760447555558812702311911975240669470604752747127e-01,
        1.383952138648065910739939690021573713989900463229686119059119e-01,
        1.399890165844607012492943162271163440328221555614326181333683e-01,
       -8.674841156816968904560822066727795382979149539517503657492964e-02,
       -7.154895550404613073584145115173807990958069673129538099990913e-02,
        5.523712625921604411618834060533403397913833632511672157671107e-02,
        2.698140830791291697399031403215193343375766595807274233284349e-02,
       -3.018535154039063518714822623489137573781575406658652624883756e-02,
       -5.615049530356959133218371367691498637457297203925810387698680e-03,
        1.278949326633340896157330705784079299374903861572058313481534e-02,
       -7.462189892683849371817160739181780971958187988813302900435487e-04,
       -3.849638868022187445786349316095551774096818508285700493058915e-03,
        1.061691085606761843032566749388411173033941582147830863893939e-03,
        7.080211542355278586442977697617128983471863464181595371670094e-04,
       -3.868319473129544821076663398057314427328902107842165379901468e-04,
       -4.177724577037259735267979539839258928389726590132730131054323e-05,
        6.875504252697509603873437021628031601890370687651875279882727e-05,
       -1.033720918457077394661407342594814586269272509490744850691443e-05,
       -4.389704901781394115254042561367169829323085360800825718151049e-06,
        1.724994675367812769885712692741798523587894709867356576910717e-06,
       -1.787139968311359076334192938470839343882990309976959446994022e-07
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
