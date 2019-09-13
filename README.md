# GRUPPO 1 - BENTLEY, GONDOLO, SCHIAVI
## Applicazione web full stack per la gestione di entità Module

### Team

* DEV 1: Alessandro Gondolo, dedicato alla configurazione del db H2 e alla gestione della persistenza dei dati sul backend
* DEV 2: Andrea Schiavi, dedicato all'implementazione del controller sul backend e alla supervisione del repository Git
* DEV 3: Andrea Bentley, dedicato al'implementazione del frontend

### Set up del progetto

1. Abbiamo attribuito i ruoli secondo le indicazioni di progetto.
2. Abbiamo deciso di lavorare con una modalità di sviluppo incrementale, non test-driven.
3. Abbiamo stabilito di procedere in prima istanza con due progetti separati per frontend e backend:

    * DEV 1 e DEV 2 implementano il backend su uno scaffold Maven Spring Boot versionato con Git, sviluppando su branch separati.
    * DEV 3 si forma su Angular, sperimentando su un progetto separato.
    * Successivamente il frontend viene integrato nel progetto di backend
    
    Il risultato finale sarà un progetto Maven padre con due moduli figli per frontend e backend 

4. Abbiamo stabilito informalmente il contratto tra repository e controller, e la web API.
5. Abbiamo concordato un set di dati di mock condivisi con un foglio di calcolo Google Docs.

### Steps

#### DEV 1

1. Implementazione dell'entity modules secondo le specifiche ricevute e da quelel definite da noi
2. Implementazione del repository tramite l'interfaccia DataJpaRepository
3. Inserimento dei metodi necessari a noi a testare la correttezza dei dati inseriti
4. Creazione dei dati di mock su cui testeremo il repository
5. Creazione dei test per il database in memory che andremo a testare
6. Modifica del repository in memory reso persistente tramite le configuration properties
7. Inserimento di file di cofigurazione per prendere i dati di mock necessari ai test in memory
8. Verifica che la tabella con i dati in memory non si vede se viene eseguita l'applicazione 

#### DEV 2

1. Implementazione di un REST controller per la gestione delle richieste CRUD
2. Unit test delle richieste CRUD con repository di mock
3. Integration test delle richieste CRUD con repository di produzione con db H2 in memory
4. Test euristico dell'applicazione con db H2 persistito, lanciando richieste HTTP da Postman
5. Estensione della web API con una chiamata che gestisca la paginazione dell'elenco dei Module
6. Creazione di un report Jasper in formato jrxml per i dettagli di un'entità selezionata
7. Estensione della web API con una chiamata che gestisca la produzione e il download del report
8. Integrazione del frontend nel backend
9. Configurazione delle policies CORS per consentire a DEV 3 di sviluppare il progetto Angular senza dover ricompilare l'intera applicazione
10. Estensione della web API con una chiamata per la selezione di un'entità parametrizzata su tutti i campi dell'entità
11. Implementazione di nuovi test di integrazione per testare le nuove funzionalità della web API
12. Configurazione di Swagger per la documentazione della web API

#### DEV 3
