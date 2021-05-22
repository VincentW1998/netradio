# Projet de Programmation Réseaux : NetRadio


Objectif du projet
-----------------------
Le but de ce projet est de programmer différentes entités mettant en place un service de diffusion de messages de 140 caractères. Il y aura trois types d’entités :
-   1. Des diffuseurs de messages
-   2. Des clients capable de recevoir les messages diffusés 
-   3. Des gestionnaire de diffuseurs de messages

Les diffuseurs enverront en multi-diffusion des messages en continu, ils pourront aussi envoyer des messages d’utilisateurs et renvoyer les derniers messages à un utilisateur sur demande. 

Les gestionnaires auront chacun une liste de diffuseurs (avec leurs caractéristiques) et pourront transmettre cette liste aux clients qui la demanderont. Ils devront aussi s’assurer que les diffuseurs présents dans la liste sont encore actifs.

Les clients devront être capable d’écouter les messages des diffuseurs, de leur envoyer des messages par- ticuliers, de leur demander une liste des derniers messages et d’interroger les gestionnaires pour récupérer des informations sur les diffuseurs.


Tester notre projet
----------------------
Notre projet s'exécute à partir du scrit `run.sh` qui lance soit :
* un gestionnaire, `./run.sh gest`
* un diffuseur, `./run.sh diff`
* un client, `./run.sh client`

Les commandes du client :
* `LAST` cf sujet
* `LIST` cf sujet
* `MESS` cf sujet
* `LISTEN` : pour écouter les messages UDP du diffuseur, pour cette commande il faudra indiquer le tty d'un autre terminal ouvert sur la même machine que le client.
* `FILE` : (extension) envoie un fichier au gestionnaire qui enverra le nom du fichier à tous ses diffuseurs.
* `LISTFILES` : (extension) demande à un diffuseur les fichiers que le gestionnaire possède.
* `DOWNLOAD` : (extension) "télécharge" un fichier.
* `HELP` : affiche toutes les commandes disponibles.
* `QUIT` : quitte le client.

Architecture du projet 
----------------------
Notre projet est composé de 2 parties :
* Une partie Java qui contient le Gestionnaire et le Diffuseur
* Et d'une autre partie codée en C : le Client





