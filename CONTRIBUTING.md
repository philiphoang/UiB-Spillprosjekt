## Steg 1: Opprett en *Issue*
Hvis du finner noe du vil fikse i prosjektet (hva som helst, kode, bilde, lyd, osv...) må du lage en ny *Issue* her på Gitlab. Lag overskriften så kort og presis som mulig, men forklar gjerne problemet i mer detalj i innholdet. Pass også på at problemet du vil løse ikke er for bredt (da er det bedre å lage flere mindre *Issues*). For å gjøre Håvar glad, kan du også markere hvem som har ansvaret for å fikse problemet og hva slags type problem det er.

## Steg 2: Opprett og skifte til en *Branch*
Fra *Issue*-siden i forrige steg skal det være en knapp som lager en ny *Branch*. Denne oppretter en ny branch her i Gitlab og kobler dem sammen. Deretter kjører du ```git fetch``` på ```master```-branchen på din maskin og så ```git checkout navn-på-branch```.

## Steg 3: Arbeid for å løse problemet
Nå kan du bruke Git normalt mens du gjør endringer i prosjektet for å fikse problemet du fant i steg 1. Hvis du har identifisert et problem bra nok så skal du ikke trenge å endre på mange deler i prosjektet. Husk at en *commit* bør være en enkel logisk endring og derfor ikke være altfor stor.

## Steg 4: Sjekk at bygningsprosessen ikke feiler
Hver gang du pusher endringene til Gitlab vil de gå gjennom en pipeline som blant annet sjekker at det ikke er noen kompileringsfeil, kjører enhetstestene og får feedback fra SonarQube. Det er viktig å sjekke og fikse feil som blir rapportert før man merger med ```master```.

## Steg 5: *Merge request* 
Når du er ferdig med å løse problemet må første passe på at du ikke har noen endringer på maskinen din som ikke er i en *commit*. Deretter kan du kjøre ```git push``` for å sende endringene dine til Gitlab. Nå kan du navigere til ```branch```-siden og så skal du kunne lage et nytt ```Merge Request```. Deretter må du spørre noen andre om de kan se over endringene og så akseptere. Hvis noe er feil kan du gjøre endringer og kjøre ```git push``` på nytt. Da blir endringene reflektert og personen kan se de over en gang til.
