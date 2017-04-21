# Android_File_Sender
Composant UPnP permettant à un terminal Android d'envoyer un fichier

<strong> Description: </strong>

Ce composant permet à l'utilisateur d'envoyer un fichier à un autre composant connecté au réseau, on peut imaginer par exmemple
qu'un professeur envoie des explications audios du cours aux étudiants connectés. Le transfert des fichiers se fait grâce à un serveur
HTTP. Ici le protocole UPnP servira surtout à ce que les composants se découvrent (à gérer les abonnements).

<strong>Lancement de l'application: </strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composantsur wcomp en suivant la méthode décrite sur le wiki oppocampus.

Cette application ne présente pas d'interface graphique, c'est un service en arrière plan. TODO: éteindre le service après une certaine période.


ATTENTION: -redémarrer l'application fait changer l'UID du composant, il faut donc le rajouter sur wcomp à nouveau.(ce problème sera réglé utltérieurement).

<strong>Spécification UPnP:</strong>

TODO

<strong>Maintenance:</strong>

Le projet de l'application est projet gradle.
