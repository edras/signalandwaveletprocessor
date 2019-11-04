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
public class Daub37 extends Wavelet {

  /**
   * Ingrid Daubechies orthonormalized wavelet using XX coefficients. 
   * 
   * @date 29.04.2011 21:53:00
   * @author Edras Pacola
   */
  public Daub37( ) {

    _waveLength = 74;

    double[ ] scales = {
        2.022060862498392121815038335333633351464174415618614893795880e-06,
        4.942343750628132004714286117434454499485737947791397867195910e-05,
        5.662418377066724013768394373249439163518654840493603575144737e-04,
        4.024140368257286770702140124893772447952256842478891548092703e-03,
        1.976228615387959153244055502205017461538589475705618414896893e-02,
        7.058482597718160832030361890793007659963483925312132741868671e-02,
        1.873263318620649448028843491747601576761901656888288838192023e-01,
        3.684409724003061409445838616964941132670287724754729425204047e-01,
        5.181670408556228873104519667534437205387109579265718071174178e-01,
        4.622075536616057145505448401528172070050768534504278694229363e-01,
        1.308789632330201726057701201017649601034381070893275586898075e-01,
       -2.461804297610834132869018581145720710365433914584680691693717e-01,
       -2.943759152626617722808219575932673733674290772235644691367427e-01,
        1.967150045235938977077768648740052380288156507222647187301894e-02,
        2.515232543602686933435224095078166291442923992611593827552710e-01,
        8.180602838721862339029076982652411696000045533716726027662147e-02,
       -1.819622917786080007408824256525225216444443143868752611284260e-01,
       -1.084517138233017845554078812341876568514835176341639783558543e-01,
        1.299296469598537527842528895259188653120602318620944502979726e-01,
        1.017802968388141797470948228505865617480048287983176581607964e-01,
       -9.660754061668439030915405045955772715988585374771282291315496e-02,
       -8.233021190655740867404073660920379414988302492018783774702028e-02,
        7.504761994836017933579005072594245435071674452882148228583865e-02,
        5.956741087152995245435589042520108066877114768216272503684398e-02,
       -5.925681563265897095153806724965924334077555174281436189512239e-02,
       -3.825382947938424882011108885090442116802994193611884738133373e-02,
        4.580794415126833246633256156110381805848138158784734496981778e-02,
        2.097280059259754883313769469036393294461497749083921162354229e-02,
       -3.352358406410096994358662875913243067234786296009238949920582e-02,
       -8.833493890410232394064187990625563257107429109130726291528648e-03,
        2.261865154459947356571431658958802912061105608212828675323452e-02,
        1.690472383484423743663952859090705636512807161536954018400081e-03,
       -1.376398196289478433857985486097070339786225136728067000591187e-02,
        1.519305778833399218481261844599507408563295102235964076544334e-03,
        7.387757452855583640107787619408806919082115520707105052944171e-03,
       -2.248053187003824706127276829147166466869908326245810952521710e-03,
       -3.394523276408398601988475786247462646314228994098320665709345e-03,
        1.816871343801423525477184531347879515909226877688306010517914e-03,
        1.263934258117477182626760951047019242187910977671449470318766e-03,
       -1.111484865318630197259018233162929628309920117691177260742614e-03,
       -3.280788470880198419407186455190899535706232295554613820907245e-04,
        5.490532773373631230219769273898345809368332716288071475378651e-04,
        1.534439023195503211083338679106161291342621676983096723309776e-05,
       -2.208944032455493852493630802748509781675182699536797043565515e-04,
        4.336726125945695214852398433524024058216834313839357806404424e-05,
        7.055138782065465075838703109997365141906130284669094131032488e-05,
       -3.098662927619930052417611453170793938796310141219293329658062e-05,
       -1.639162496160583099236044020495877311072716199713679670940295e-05,
        1.354327718416781810683349121150634031343717637827354228989989e-05,
        1.849945003115590390789683032647334516600314304175482456338006e-06,
       -4.309941556597092389020622638271988877959028012481278949268461e-06,
        4.854731396996411681769911684430785681028852413859386141424939e-07,
        1.002121399297177629772998172241869405763288457224082581829033e-06,
       -3.494948603445727645895194867933547164628229076947330682199174e-07,
       -1.509885388671583553484927666148474078148724554849968758642331e-07,
        1.109031232216439389999036327867142640916239658806376290861690e-07,
        5.350657515461434290618742656970344024396382191417247602674540e-09,
       -2.252193836724805775389816424695618411834716065179297102428180e-08,
        4.224485706362419268050011630338101126995607958955688879525896e-09,
        2.793974465953982659829387370821677112004867350709951380622807e-09,
       -1.297205001469435139867686007585972538983682739297235604327668e-09,
       -1.031411129096974965677950646498153071722880698222864687038596e-10,
        1.946164894082315021308714557636277980079559327508927751052218e-10,
       -3.203398244123241367987902201268363088933939831689591684670080e-11,
       -1.398415715537641487959551682557483348661602836709278513081908e-11,
        6.334955440973913249611879065201632922100533284261000819747915e-12,
       -2.096363194234800541614775742755555713279549381264881030843258e-13,
       -4.421612409872105367333572734854401373201808896976552663098518e-13,
        1.138052830921439682522395208295427884729893377395129205716662e-13,
       -4.518889607463726394454509623712773172513778367070839294449849e-16,
       -5.243025691884205832260354503748325334301994904062750850180233e-15,
        1.189012387508252879928637969242590755033933791160383262132698e-15,
       -1.199280335852879554967035114674445327319437557227036460257649e-16,
        4.906615064935203694857690087429901193139905690549533773201453e-18
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
