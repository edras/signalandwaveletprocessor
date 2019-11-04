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
package cs.jwave.handlers.wavelets.coiflet;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Ingrid Daubechies' orthonormal Coiflet wavelet of thirty coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class Coif05 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Coiflet3 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Coif05( ) {

    _waveLength = 30; // minimal array size for transform

    double[ ] scales = {
        -9.517657273819165e-08,
        -1.6744288576823017e-07,
        2.0637618513646814e-06,
        3.7346551751414047e-06,
        -2.1315026809955787e-05,
        -4.1340432272512511e-05,
        0.00014054114970203437,
        0.00030225958181306315,
        -0.00063813134304511142,
        -0.0016628637020130838,
        0.0024333732126576722,
        0.0067641854480530832,
        -0.0091642311624818458,
        -0.019761778942572639,
        0.032683574267111833,
        0.041289208750181702,
        -0.10557420870333893,
        -0.062035963962903569,
        0.43799162617183712,
        0.77428960365295618,
        0.42156620669085149,
        -0.052043163176243773,
        -0.091920010559696244,
        0.02816802897093635,
        0.023408156785839195,
        -0.010131117519849788,
        -0.004159358781386048,
        0.0021782363581090178,
        0.00035858968789573785,
        -0.00021208083980379827
    };
    
    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = _waveLength-1; i > -1; i-- )
      _scales[ _waveLength-1-i ] = scales[ i ];

    _coeffs = new double[ _waveLength ]; 

    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _coeffs[ i ] = _scales[ ( _waveLength - 1 ) - i ];
      } else {
        _coeffs[ i ] = -_scales[ ( _waveLength - 1 ) - i ];
      } // if

  } // Coif05

} // class
