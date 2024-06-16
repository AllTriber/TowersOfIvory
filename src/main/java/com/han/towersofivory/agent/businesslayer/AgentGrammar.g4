grammar AgentGrammar;


INT: '0' | [1-9] [0-9]* ;
WS: [ \t\r\n]+ -> skip;

// Taal
WANNEER: 'Wanneer ';
DAN: 'dan ';
IK: 'ik' (' ')?;
DE: 'de ';
BEVAT: ' bevat';
NAAR: 'naar ';
EN: 'en ';
ZIJN: 'zijn';
KAMER: 'kamer';
GANG: 'gang ';
MIJN: 'mijn ';
MET: 'met ';
VAN: 'van ';
IN: 'in ';
BEN: 'ben';
EEN: 'een';
OF: 'of ';
IS: 'is';

// Operators
MINDERDAN: 'minder dan ';
MEERDAN: 'meer dan ';
NIETGELIJKAAN: 'niet gelijk aan ' | 'ongelijk aan ' | 'niet gelijk ' | 'ongelijk ' | 'niet';
GELIJKAAN: 'gelijk aan ' | 'gelijk ' | 'hetzelfde als ' | 'hetzelfde ';

// Punctuation
PUNT: '.';
COMMA: ', ';
SPATIE: ' ';

//Attributen
LEVENSPUNTEN: 'levenspunten';
// LEVEL: 'level ';
// ERVARINGSPUNTEN: 'ervaringspunten ';
MAXIMALELEVENS: 'maximale levenspunten';
AANVALSKRACHT: 'aanvalskracht';
// SNELHEID: 'snelheid ';
VERDEDIGINGSKRACHT: 'verdedigingskracht';

//Kamerattributen
DEUR: 'deuren' | 'deur';
TRAP: 'trappen' | 'trap';

//Voorwerpen
//VOORWERP: 'voorwerp' | 'voorwerpen';
//GOUDENPLAAT: 'gouden plaat' | 'gouden platen';
//// PATROON: 'patroon ' | 'patronen ';
//// ANTIPATROON: 'anti-patroon ' | 'anti-patronen ';
//ABSENTIE: 'absentie' | 'absenties';
//C4MODEL: 'C4 '('model')?| 'C4 ' ('modellen')?;
//// KENNIS: 'kennis ';
//// KIST: 'kist ' | 'kisten ';

// Entities
//MAMMOET: 'mammoet' | 'mammoeten';
//ELF: 'elf' | 'elven';
//WATERSPUWER: 'waterspuwer' | 'waterspuwers';
//GIGANTISCHESPIN: 'gigantische spin' | 'gigantische spinnen';
SPELER: 'spelers' | 'speler';


// Directions
BOVEN: 'boven';
BENEDEN: 'beneden';
LINKS: 'links';
RECHTS: 'rechts';
TERUG: 'terug';
WEG: 'weg ';
DOOR: 'door';

// Actions
LOOP: 'loop ';
SLA: 'sla ';
INTERACTEER: 'interacteer ';


// Proximity
DICHTSTBIJZIJNDE: 'dichtstbijzijnde ';
VERSTE: 'verste ';

agent: regel+ EOF;

regel: WANNEER expressie COMMA actie PUNT;
expressie: (spelerssituatie | omgevingssituatie) (voegWoorden (spelerssituatie | omgevingssituatie))* ;
omgevingssituatie:  kamerObservatie | gangObservatie;
spelerssituatie:  MIJN spelerAttribuut operatie integer (IS | ZIJN);


voegWoorden: EN #enOperatie
            |OF #ofOperatie;

kamerObservatie: DE KAMER operatie? integer omgevingsAttribuut BEVAT;

gangObservatie: DE GANG operatie? integer omgevingsAttribuut BEVAT | IK IN EEN GANG BEN;

actie: DAN (loopActie
            | aanvalsActie
            | interacteerActie);

loopActie: LOOP IK loopRichting;
aanvalsActie: SLA IK DE nabijheidsLocatie;
interacteerActie: INTERACTEER IK MET DE nabijheidsLocatie;
operatie: MINDERDAN #minderDanOperatie
        | MEERDAN   #meerDanOperatie
        | GELIJKAAN #gelijkAanOperatie
        | NIETGELIJKAAN #nietGelijkAanOperatie;

spelerAttribuut: LEVENSPUNTEN #levenspuntenAttribuut
                | MAXIMALELEVENS #maximaleLevensAttribuut
                | AANVALSKRACHT #aanvalskrachtAttribuut
                | VERDEDIGINGSKRACHT #verdedigingskrachtAttribuut;


loopRichting: (NAAR DE nabijheidsLocatie) #naarRichting | (WEG VAN DE nabijheidsLocatie) #wegRichting |  DE GANG DOOR #gangRichting;

nabijheidsLocatie: nabijheid? omgevingsAttribuut;

nabijheid: DICHTSTBIJZIJNDE #dichtstbijzijnde
         | VERSTE #verste;
 //      | ONVERKENDE

integer: INT;

omgevingsAttribuut: DEUR #deur
            | TRAP #trap
            | SPELER #speler;
//        | MAMMOET
//        | ELF
//        | GIGANTISCHESPIN
//        | WATERSPUWER;
//
//      | GOUDENPLAAT
//      | VOORWERP
//      | ABSENTIE
//      | C4MODEL
//      | TRAP;

