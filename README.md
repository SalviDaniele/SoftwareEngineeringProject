[ITALIAN] - (English version below)

Il nostro progetto implementa le regole complete del gioco Codex Naturalis e permette di giocarci sia da GUI che da TUI e sia usando TCP che RMI come protocollo di rete.
Seguendo i requisiti per raggiungere un voto massimo di 30, indicati nel documento fornito, abbiamo anche implementato 2 funzionalità aggiuntive: chat e partite multiple.

Per avviare il gioco vi sono 2 file jar presenti nella cartella deliverables, uno per avviare il server e l'altro per avviare il client.
È sufficiente spostarsi in tale cartella e, da terminale, inserire il comando "java -jar server-jar-with-dependencies.jar" per il server o "java -jar client-jar-with-dependencies.jar" per il client.
A quel punto, viene richiesto di scegliere il tipo d'interfaccia (inserendo "gui" o "cli") e il tipo di protocollo (inserendo "tcp" o "rmi").
Dopo aver inserito l'indirizzo IP del server (si assume, come riportato nel documento, che i client conoscano tale IP) il gioco si avvia e, in caso, si avvia la GUI.

Il primo giocatore ad avviare il gioco sceglie il numero di giocatori per la prima partita e il proprio nickname, poi tutti gli altri giocatori scelgono il proprio nickname ed entrano nella partita.
Una volta raggiunto il numero di giocatori scelto dal creatore della partita, essa verrà avviata e al giocatore successivo verrà richiesto nuovamente di inserire il numero di giocatori per una nuova partita.
Sia la TUI che la GUI guidano i giocatori nella fase di preparazione e poi si comincia la partita. Per la TUI vi è il comando "help" che riferisce quali sono tutti i comandi disponibili, il cui output è il seguente:

Commands:
 - place  -> place a card on your play area
 - draw   -> draw a card from one of the decks or pick a card on the table
 - chat   -> use the chat to send a message to everyone in your match or to a specific player
 - hand   -> displays the cards in your hand
 - table  -> displays the cards and decks on the table
 - area   -> displays a player's current points and play area
 - secret -> displays your secret objective card
 - help   -> displays this message

In caso arrivi un messaggio dalla chat mentre si sta digitando un comando, questo viene annullato e va reinserito. I comandi place e draw possono essere inseriti solo nel proprio turno e place va inserito prima di draw.
La play area mostra una matrice con: un colore se una carta con quel colore carta è posizionata a quelle coordinate, 0 se non è possibile posizionare una cata a quelle coordinate, 1 se è possibile.
Al comando place seguono la selezione della carta dalla mano, la riga a cui posizionare la carta, la colonna, il verso di posizionamento, e infine si mostra come cambierebbero le quantità di risorse e oggetti in caso di conferma.
Il giocatore a questo punto è libero di rifiutare e selezionare nuovamente la carta da posizionare, ripetendo il processo.
Il comando chat lascia poi scegliere un destinatario mentre, digitando "all" o "everybody", il messaggio verrà mandato a tutti i giocatori presenti nella stessa partita del mittente.

Vi sono 3 comandi aggiuntivi (inseribili anche durante il turno di altri giocatori), non prsenti nell'elenco, utili a testare il programma:
  -  cheat1 -> fornisce 10000 risorse per ogni simbolo al giocatore, potendo posizionare liberamente le carte oro senza problemi
  -  cheat2 -> imposta il punteggio del giocatore a 20, permettendo di accedere anticipatamente alla fase finale della partita
  -  cheat3 -> svuota i deck risorsa e oro presenti sul tavolo di tutte le carte, permettendo di accedere anticipatamente alla fase finale della partita

Nella GUI, in alto a sinistra, vi è un menu che permette di cambiare finestra, accedendo alla propria area (comprensiva di mano e obbiettivo segreto), al tavolo (per pescare e visualizzare le carte obbiettivo), alla chat e alle play area degli altri giocatori.
Su questo stesso menu, in caso arrivi un messaggio in chat senza che questa sia aperta, comparirà un pallino rosso come notifica. Per vedere il messaggio bisgona aprire la chat e selezionare il mittente giusto ("global" per messaggi pubblici).
La pedina col proprio colore è visualizzata sulla destra nella finestra principale (la play area) e, cliccando sopra di essa, viene attivato il cheat2, portando istantaneamente il proprio punteggio a 20.

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

[ENGLISH]

Our project implements the complete rules of the Codex Naturalis game and allows it to be played both from a GUI and a TUI, using either TCP or RMI as the network protocol.
Following the requirements to achieve a maximum score of 30, as indicated in the provided document, we have also implemented 2 additional features: chat and multiple matches.

To start the game, there are 2 jar files in the deliverables directory, one to start the server and the other to start the client.
Navigate to this folder and, from the terminal, enter the command "java -jar server-jar-with-dependencies.jar" for the server or "java -jar client-jar-with-dependencies.jar" for the client.
At that point, you will be asked to choose the type of interface (by entering "gui" or "cli") and the type of protocol (by entering "tcp" or "rmi").
After entering the server's IP address (it is assumed, as stated in the document, that the clients know this IP), the game will start and, if applicable, the GUI will launch.

The first player to start the game chooses the number of players for the first match and their nickname, then all other players choose their nickname and join the match.
Once the number of players chosen by the match creator is reached, the match will start and the next player will again be asked to enter the number of players for a new match.
TUI and the GUI guide the players through the preparation phase, and the match begins. For the TUI, there is the "help" command that lists all available commands, with the following output:

Commands:
 - place  -> place a card on your play area
 - draw   -> draw a card from one of the decks or pick a card on the table
 - chat   -> use the chat to send a message to everyone in your match or to a specific player
 - hand   -> displays the cards in your hand
 - table  -> displays the cards and decks on the table
 - area   -> displays a player's current points and play area
 - secret -> displays your secret objective card
 - help   -> displays this message

If a chat message arrives while typing a command, the command is cancelled and must be re-entered. The "place" and "draw" commands can only be entered during your turn, and "place" must be entered before "draw".
The play area displays a matrix with a colour if a card of that colour is positioned at those coordinates, 0 if it is not possible to place a card at those coordinates, and 1 if it is possible.
The "place" command is followed by selecting the card from the hand, the row to place the card, the column, the placement face, and finally showing how the quantities of resources and items would change if confirmed.
At this point, the player is free to reject and select the card to place again, repeating the process.
The "chat" command then lets you choose a recipient, and by typing "all" or "everybody", the message will be sent to all players in the same match as the sender.

There are 3 additional commands (which can also be entered during other players' turns), not listed in the "help" output, useful for testing the program:
 - cheat1 -> provides the player with 10,000 resources for each symbol, allowing gold cards to be placed freely
 - cheat2 -> sets the player's score to 20, allowing early access to the final phase of the match
 - cheat3 -> empties the resource and gold decks on the table of all cards, allowing early access to the final phase of the match

In the GUI, in the top left, there is a menu that allows you to switch windows, letting you access your area (including hand and secret objective), the table (to draw and view objective cards), the chat, and the play areas of other players.
In this same menu, if a chat message arrives without the chat being open, a red dot will appear as a notification. To view the message, you need to open the chat and select the correct sender ("global" for public messages).
Your pawn is displayed on the right in the main window (the play area), and by clicking on it, the cheat2 is activated, instantly setting your score to 20.
