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
public class Daub10 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub10( ) {

    _waveLength = 20;

    double[ ] scales = {
        2.667005790055555358661744877130858277192498290851289932779975e-02,
        1.881768000776914890208929736790939942702546758640393484348595e-01,
        5.272011889317255864817448279595081924981402680840223445318549e-01,
        6.884590394536035657418717825492358539771364042407339537279681e-01,
        2.811723436605774607487269984455892876243888859026150413831543e-01,
       -2.498464243273153794161018979207791000564669737132073715013121e-01,
       -1.959462743773770435042992543190981318766776476382778474396781e-01,
        1.273693403357932600826772332014009770786177480422245995563097e-01,
        9.305736460357235116035228983545273226942917998946925868063974e-02,
       -7.139414716639708714533609307605064767292611983702150917523756e-02,
       -2.945753682187581285828323760141839199388200516064948779769654e-02,
        3.321267405934100173976365318215912897978337413267096043323351e-02,
        3.606553566956169655423291417133403299517350518618994762730612e-03,
       -1.073317548333057504431811410651364448111548781143923213370333e-02,
        1.395351747052901165789318447957707567660542855688552426721117e-03,
        1.992405295185056117158742242640643211762555365514105280067936e-03,
       -6.858566949597116265613709819265714196625043336786920516211903e-04,
       -1.164668551292854509514809710258991891527461854347597362819235e-04,
        9.358867032006959133405013034222854399688456215297276443521873e-05,
       -1.326420289452124481243667531226683305749240960605829756400674e-05
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
