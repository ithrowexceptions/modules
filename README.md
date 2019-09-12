# GRUPPO 1 - BENTLEY, GONDOLO, SCHIAVI
## Applicazione web full stack per la gestione di entità Module

### Team

* DEV 1: Alessandro Gondolo, dedicato alla configurazione del db H2 e alla gestione della persistenza dei dati
* DEV 2: Andrea Schiavi, dedicato all'implementazione del web RESTful API e alla supervisione del repository Git.
* DEV 3: Andrea Bentley, dedicato al'implementazione del frontend

### Set up del progetto

1. Abbiamo attribuito i ruoli secondo le indicazioni di progetto.
2. Abbiamo deciso di lavorare con una modalità di sviluppo incrementale, non test-driven.
3. Abbiamo stabilito di procedere in prima istanza con due progetti separati per frontend e backend:

    * DEV 1 e DEV 2 implementano il backend su uno scaffold Maven Spring Boot versionato con Git, sviluppando su branch separati.
    * DEV 3 si forma su Angular, sperimentando su un progetto separato.
    * Non appena il frontend è sufficientemente aderente alle specifche, viene integrato nel progetto di backend.
    
    Il risultato finale sarà un progetto Maven padre con due moduli figli per frontend e backend, configurando le CORS policies in modo tale che DEV 3 possa modificare il frontend senza dover ricompilare l'intero progetto. 

4. Abbiamo stabilito informalmente i contratti tra repository e controller e la web API.
5. Abbiamo concordato un set di dati di mock condivisi con un foglio di calcolo Google Docs.

### Steps

#### DEV 1

#### DEV 2

1. Implementazione di un REST controller per la gestione delle operazioni CRUD
2. Unit test sul controller con repository di mock
3. Integration test su controller + repository con db H2 in memory
4. Test euristico dell'applicazione con db H2 persistito, lanciando richieste HTTP da Postman
5. Estensione della web API con una chiamata che gestisca la paginazione
6. Implementazione di un report Jasper in formato jrxml
7. Estensione della web API con una chiamata che gestisca la produzione di un report di un'entità in formato pdf
8. Integrazione del frontend nel backend

#### DEV 3
