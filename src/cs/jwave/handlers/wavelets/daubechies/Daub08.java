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
 * Ingrid Daubechies orthonormalized wavelet using 16 coefficients.
 * 
 * @date 29.04.2011 21:53:00
 * @author Edras Pacola
 */
public class Daub08 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using 16 coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub08( ) {

    _waveLength = 16;

    double[ ] scales = {
        5.441584224310400995500940520299935503599554294733050397729280e-02,
        3.128715909142999706591623755057177219497319740370229185698712e-01,
        6.756307362972898068078007670471831499869115906336364227766759e-01,
        5.853546836542067127712655200450981944303266678053369055707175e-01,
       -1.582910525634930566738054787646630415774471154502826559735335e-02,
       -2.840155429615469265162031323741647324684350124871451793599204e-01,
        4.724845739132827703605900098258949861948011288770074644084096e-04,
        1.287474266204784588570292875097083843022601575556488795577000e-01,
       -1.736930100180754616961614886809598311413086529488394316977315e-02,
       -4.408825393079475150676372323896350189751839190110996472750391e-02,
        1.398102791739828164872293057263345144239559532934347169146368e-02,
        8.746094047405776716382743246475640180402147081140676742686747e-03,
       -4.870352993451574310422181557109824016634978512157003764736208e-03,
       -3.917403733769470462980803573237762675229350073890493724492694e-04,
        6.754494064505693663695475738792991218489630013558432103617077e-04,
       -1.174767841247695337306282316988909444086693950311503927620013e-04};

    _scales = new double[_waveLength];
    
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
