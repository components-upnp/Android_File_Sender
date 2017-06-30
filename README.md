# Android_File_Sender
Composant UPnP permettant à un terminal Android d'envoyer un fichier

<strong> Description: </strong>

Ce composant permet à l'utilisateur d'envoyer un fichier à un autre composant connecté au réseau, on peut imaginer par exmemple
qu'un professeur envoie des explications audios du cours aux étudiants connectés. Le transfert des fichiers se fait grâce à un serveur via des sockets JAVA.
Ici le protocole UPnP servira surtout à à gérer les abonnements des composants clients.

L'application étant un service Android elle ne présente pas d'interface graphique. Elle enverra le fichier spécifié lors 
de la réception d'un message UPnP contenant le chemin de celui-ci.

<strong>Lancement de l'application: </strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composantsur wcomp en suivant la méthode décrite sur le wiki oppocampus.

<strong>Spécification UPnP: </strong>

Ce composant offre deux services dont voici leur description:

  a) FileSenderService 
  
    1) setFile(String newFileValue) : transmet par fichier XML l'UDN du composant émetteur, le nom du fichier envoyé ainsi que l'adresse 
  IP du serveur. Le fichier XML est transmis via un événement UPnP dont le nom est "File".
  
  b) FileToSendService :
  
    1) setPathFile(String NewPathFileValue) : cette action UPnP reçoit un fichier XML contenant l'UDN du composant qui a 
    envoyé le message ainsi que le chemin du fichier à envoyer. Le chemin est ensuite transmis à l'application.
  

Voici le schéma correspondant à ce composant:

![alt tag](https://github.com/components-upnp/Android_File_Sender/blob/master/Android_File_Sender.png)

<strong>Maintenance:</strong>

Le projet de l'application est un projet gradle.
