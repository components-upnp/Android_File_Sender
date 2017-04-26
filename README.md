# Android_File_Sender
Composant UPnP permettant à un terminal Android d'envoyer un fichier

<strong> Description: </strong>

Ce composant permet à l'utilisateur d'envoyer un fichier à un autre composant connecté au réseau, on peut imaginer par exmemple
qu'un professeur envoie des explications audios du cours aux étudiants connectés. Le transfert des fichiers se fait grâce à un serveur via des sockets JAVA.
HTTP. Ici le protocole UPnP servira surtout à à gérer les abonnements des composants clients.

L'interface graphique est composée de deux boutons: SEND et STOP. Le premier sert à lancer le serveur qui va pouvoir transmettre le fichier aux clients, le deuxième sert à stoper le serveur.

On ajoutera par la suite des boutons afin d'enregistrer un fichier audio, qui pourra par la suite être transmis.

<strong>Lancement de l'application: </strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composantsur wcomp en suivant la méthode décrite sur le wiki oppocampus.


ATTENTION: -redémarrer l'application fait changer l'UID du composant, il faut donc le rajouter sur wcomp à nouveau.(ce problème sera réglé utltérieurement).

<strong>Spécification UPnP:</strong>

Ce composant offre l'interface UPnP FileSenderController dont voici la description:

  1) setSending(boolean newSendingValue): permet de définir l'état du serveur (eteint ou allumé).
  2) setAddresse(String newAddresseValue): permet de définir l'addresse du serveur à transmettre aux clients.
  
Ce composant envoie les évènements suivants:

  1) SendingEvent lorsque le serveur change d'état (allumé/éteint)
  2) AddresseEvent lors du démarrage du serveur afin de trasnmettre l'addresse de ce dernier aux clients abonnés.
  

Voici le schéma correspondant à ce composant:

![alt tag](https://github.com/components-upnp/Android_File_Sender/blob/master/File_Sender.png)

<strong>Maintenance:</strong>

Le projet de l'application est un projet gradle.
