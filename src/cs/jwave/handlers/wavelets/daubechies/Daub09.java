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
public class Daub09 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub09( ) {

    _waveLength = 18;

    double[ ] scales = {
        3.807794736387834658869765887955118448771714496278417476647192e-02,
        2.438346746125903537320415816492844155263611085609231361429088e-01,
        6.048231236901111119030768674342361708959562711896117565333713e-01,
        6.572880780513005380782126390451732140305858669245918854436034e-01,
        1.331973858250075761909549458997955536921780768433661136154346e-01,
       -2.932737832791749088064031952421987310438961628589906825725112e-01,
       -9.684078322297646051350813353769660224825458104599099679471267e-02,
        1.485407493381063801350727175060423024791258577280603060771649e-01,
        3.072568147933337921231740072037882714105805024670744781503060e-02,
       -6.763282906132997367564227482971901592578790871353739900748331e-02,
        2.509471148314519575871897499885543315176271993709633321834164e-04,
        2.236166212367909720537378270269095241855646688308853754721816e-02,
       -4.723204757751397277925707848242465405729514912627938018758526e-03,
       -4.281503682463429834496795002314531876481181811463288374860455e-03,
        1.847646883056226476619129491125677051121081359600318160732515e-03,
        2.303857635231959672052163928245421692940662052463711972260006e-04,
       -2.519631889427101369749886842878606607282181543478028214134265e-04,
        3.934732031627159948068988306589150707782477055517013507359938e-05
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
