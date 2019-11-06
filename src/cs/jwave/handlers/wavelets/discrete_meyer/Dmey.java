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
package cs.jwave.handlers.wavelets.discrete_meyer;

import cs.jwave.handlers.wavelets.Wavelet;

/**
 * Discrete Meyer wavelet of sixty two coefficients and the
 * scales; 
 * 
 * @date 10.02.2010 16:32:38
 * @author Edras Pacola
 */
public class Dmey extends Wavelet {

  /**
   * Constructor setting up the orthonormal Coiflet3 wavelet coeffs and the
   * scales; normed, due to ||*||2 - euclidean norm.
   * 
   * @date 10.02.2010 16:32:38
   * @author Christian Scheiblich
   */
  public Dmey( ) {

    _waveLength = 62; // minimal array size for transform

    double[ ] scales = {
          0, -1.509740857423615e-06,  1.278766756823499e-06,
          4.495855604488689e-07, -2.096568870494942e-06,  1.723223554480682e-06,
          6.980822763107386e-07, -2.879408032654847e-06,  2.383148394518930e-06,
          9.825156022293386e-07, -4.217789186342479e-06,  3.353501538089444e-06,
          1.674721858836507e-06, -6.034501341860347e-06,  4.837555801559579e-06,
          2.402288022882838e-06, -9.556309845665445e-06,  7.216527694763415e-06,
          4.849078299776749e-06, -1.420692858056419e-05,  1.050391427078387e-05,
          6.187580298111554e-06, -2.443800584565461e-05,  2.010638769090948e-05,
          1.499352360001513e-05, -4.642876428365169e-05,  3.234131191367969e-05,
          3.740966576024984e-05, -1.027790050848848e-04,  2.446195684460230e-05,
          1.497135153892574e-04, -7.559287025516713e-05, -1.399131482174180e-04,
          -9.351289388011380e-05,  1.611898197253463e-04,  8.595002137623775e-04,
          -5.781857952734411e-04, -2.702168733939080e-03,  2.194775336459444e-03,
          6.045510596456078e-03, -6.386728618548126e-03, -1.104464190053889e-02,
          1.525091315858590e-02,  1.740388821017741e-02, -3.209406335450531e-02,
          -2.432178395951878e-02,  6.366730088446831e-02,  3.062124394342457e-02,
          -1.326966153588617e-01, -3.504828739059503e-02,  4.440950307665288e-01,
          7.437510049037870e-01,  4.440950307665288e-01, -3.504828739059503e-02,
          -1.326966153588617e-01,  3.062124394342457e-02,  6.366730088446831e-02,
          -2.432178395951878e-02, -3.209406335450531e-02,  1.740388821017741e-02,
          1.525091315858590e-02, -1.104464190053889e-02, -6.386728618548126e-03,
          6.045510596456078e-03,  2.194775336459444e-03, -2.702168733939080e-03,
          -5.781857952734411e-04,  8.595002137623775e-04,  1.611898197253463e-04,
          -9.351289388011380e-05, -1.399131482174180e-04, -7.559287025516713e-05,
          1.497135153892574e-04,  2.446195684460230e-05, -1.027790050848848e-04,
          3.740966576024984e-05,  3.234131191367969e-05, -4.642876428365169e-05,
          1.499352360001513e-05,  2.010638769090948e-05, -2.443800584565461e-05,
          6.187580298111554e-06,  1.050391427078387e-05, -1.420692858056419e-05,
          4.849078299776749e-06,  7.216527694763415e-06, -9.556309845665445e-06,
          2.402288022882838e-06,  4.837555801559579e-06, -6.034501341860347e-06,
          1.674721858836507e-06,  3.353501538089444e-06, -4.217789186342479e-06,
          9.825156022293386e-07,  2.383148394518930e-06, -2.879408032654847e-06,
          6.980822763107386e-07,  1.723223554480682e-06, -2.096568870494942e-06,
          4.495855604488689e-07,  1.278766756823499e-06, -1.509740857423615e-06,
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
    
  } // Dmey

} // class
