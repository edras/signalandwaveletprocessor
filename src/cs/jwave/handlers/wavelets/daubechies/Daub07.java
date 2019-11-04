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
 * Ingrid Daubechies orthonormalized wavelet using 14 coefficients.
 * 
 * @date 29.04.2011 21:53:00
 * @author Edras Pacola
 */
public class Daub07 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using 14 coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub07( ) {

    _waveLength = 14;

    double[ ] scales = {
        7.785205408500917901996352195789374837918305292795568438702937e-02,
        3.965393194819173065390003909368428563587151149333287401110499e-01,
        7.291320908462351199169430703392820517179660611901363782697715e-01,
        4.697822874051931224715911609744517386817913056787359532392529e-01,
       -1.439060039285649754050683622130460017952735705499084834401753e-01,
       -2.240361849938749826381404202332509644757830896773246552665095e-01,
        7.130921926683026475087657050112904822711327451412314659575113e-02,
        8.061260915108307191292248035938190585823820965629489058139218e-02,
       -3.802993693501441357959206160185803585446196938467869898283122e-02,
       -1.657454163066688065410767489170265479204504394820713705239272e-02,
        1.255099855609984061298988603418777957289474046048710038411818e-02,
        4.295779729213665211321291228197322228235350396942409742946366e-04,
       -1.801640704047490915268262912739550962585651469641090625323864e-03,
        3.537137999745202484462958363064254310959060059520040012524275e-04};

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
