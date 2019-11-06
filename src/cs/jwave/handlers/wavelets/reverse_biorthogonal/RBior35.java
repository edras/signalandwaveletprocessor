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
package cs.jwave.handlers.wavelets.reverse_biorthogonal;

import cs.jwave.handlers.wavelets.Wavelet;
import cs.jwave.handlers.wavelets.biorthogonal.Bior35;

/**
 * Ingrid Daubechies' orthonormal Biorthogonal wavelet of twelve coefficients and the
 * scales; normed, due to ||*||2 - euclidean norm.
 * 
 * @date 10.02.2010 16:32:38
 * @author Christian Scheiblich
 */
public class RBior35 extends Wavelet {

  /**
   * Constructor setting up the orthonormal Bior35 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public RBior35( ) {

    Bior35 biorXX = new Bior35();
    
    _waveLength = biorXX.getWaveLength( );
    
    double[ ] coeffs = biorXX.getCoeffs( );
    double[ ] scales = biorXX.getScales( );

    _scales = new double[ _waveLength ]; // can be done in static way also; faster?
    _coeffs = new double[ _waveLength ]; // can be done in static way also; faster?
    
    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _coeffs[ _waveLength-1-i ] = scales[ i ];
      } else {
        _coeffs[ _waveLength-1-i ] = -scales[ i ];
      } // if
    
    for( int i = 0; i < _waveLength; i++ )
      if( ( i % 2 ) == 0 ) {
        _scales[ _waveLength-1-i ] = coeffs[ i ];
      } else {
        _scales[ _waveLength-1-i ] = -coeffs[ i ];
      } // if

  } // Bior35

} // class
