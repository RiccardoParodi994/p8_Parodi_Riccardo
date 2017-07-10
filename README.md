
Progetto di Software Engineering
NL2SNL 
NLTK

------------------------------------------------------------------------

Per poter lanciare il programma SE-NL2SNL correttamente su ECLIPSE è necessario seguire i seguenti passi.

Si possono prendere due vie per il download del codice : 
o si scarica la cartella "SE-NL2SNL" e la si importa in ECLIPSE insieme alla libreria per il parser di Stanford,
o si scarica la cartella "src" insieme ai file "bin", ".project", ".classpath" insieme alla libreria per il parser di Stanford.

Una volta scelta una delle due suddette strade, andranno inseriti nel progetto, a livello dell'"src", anche i seguenti file :

- "pattern.txt" (file necessario per la memorizzazione dei requisiti tradotti nella fase di testing strutturale) ;
- "pos-tags.txt" (file necessario per la lettura dei pos-tags fondamentali per il loro riconoscimento nell'albero sintattico) ;
- "requirements.txt" (raccolta di requisiti inviati dal Professor Tacchella. Essi possono essere eventualmente utilizzati per la verifica del corretto funzionamento del progetto. Per utilizzarli basterà settare nella main la stringa relativa al nome del file dei requisiti da leggere a "requirements.txt") ;
- "requirementsForFunctionalTesting" (raccolta di requisiti di default utilizzati dal progetto all'avvio della "run") ;
- "signals.txt" (raccolta di segnali associati ai requisiti (relativi quindi sia al "requirements.txt" sia al "requirementsForFunctionalTesting") inviati dal Professor Tacchella) ;

Dopo di chè si aggiunga anche il la libreria situata all'interno della cartella "libraries" per il corretto funzionamento della classe "Parser" implementante le funzioni del parser di Stanford.

Eseguito anche quest'ultimo passo, basterà eseguire la "run" e il sistema stamperà a video il corretto risultato.
------------------------------------------------------------------------
