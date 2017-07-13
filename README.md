
Progetto di Software Engineering
NL2SNL 
NLTK

------------------------------------------------------------------------
Premetto che se nel seguente procedimento per acquisizione di codice e librerie dovvessero esserci problemi, per quel che riguarda la parte di recupero del "RequirementsValidator" (di tipo : Executable Jar File (.jar) ) mi sono semplicemente limmitato a scaricare la cartella "RequirementsValidator" nella cartella relativa al link dropBox che il Professor Narizzano ci inviò a tempo debito a noi del progetto NL2SNL. 

Per poter lanciare il programma SE-NL2SNL correttamente su ECLIPSE è necessario seguire i seguenti passi.

Si possono prendere due vie per il download del codice : 
o si scarica la cartella "SE-NL2SNL" (che ho direttamente prelevato dal mio "workspace") e la si importa in ECLIPSE insieme al "RequirementsValidator.jar" per il corretto funzionamento di alcune funzionalità del parser di Stanford,
o si scarica la cartella "src" insieme ai file "bin", ".project", ".classpath" insieme al "RequirementsValidator.jar" per il corretto funzionamento di alcune funzionalità del parser di Stanford.

Una volta scelta una delle due suddette strade, andranno inseriti nel progetto, a livello dell'"src", anche i seguenti file :

- "pattern.txt" (file necessario per la memorizzazione dei requisiti tradotti nella fase di testing strutturale) ;
- "pos-tags.txt" (file necessario per la lettura dei pos-tags fondamentali per il loro riconoscimento nell'albero sintattico) ;
- "requirements.txt" (raccolta di requisiti inviati dal Professor Tacchella. Essi possono essere eventualmente utilizzati per la verifica del corretto funzionamento del progetto. Per utilizzarli basterà settare nella main la stringa relativa al nome del file dei requisiti da leggere a "requirements.txt") ;
- "requirementsForFunctionalTesting" (raccolta di requisiti di default utilizzati dal progetto all'avvio della "run") ;
- "signals.txt" (raccolta di segnali associati ai requisiti (relativi quindi sia al "requirements.txt" sia al "requirementsForFunctionalTesting") inviati dal Professor Tacchella) ;

Nella sezione libraries c'è il file per l'apposito link per scaricare la zip contenente il "RequirementsValidator". (Non potevo fare altrimenti causa l'eccessiva dimensione del programma.)

Il .jar "RequirementsValidator.jar" si recupera seguendo questo percorso -> cartella "RequirementsValidator" -> cartella "bin" -> "RequirementsValidator" di tipo : Executable Jar File (.jar).

Dopo di chè si aggiunga anche il .jar denominato "RequirementsValidator.jar", nel senso di andare sul "build path" relativo al suddetto progetto precedentemente scaricato e importato, da qui procedere con "configure build path" e infine, tramite la funzionalità "add external jars" aggiungere il .jar.

Eseguito anche quest'ultimo passo, basterà eseguire la "run" e il sistema stamperà a video il corretto risultato.
------------------------------------------------------------------------
