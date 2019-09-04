# LiveCoaching

minSdkVersion 24
targetSdkVersion 28

Projet de Stage de fin d'études

Cette application est à utiliser avec l'application [LiveCoached](https://github.com/AlexGreau/LiveCoached) pour tablettes Android. Cette paire d'application sert à conduire une expérience cherchant à prouver nos hypothèses sur de nouvelles façons d'interagir en contexte actif. Voici le schéma avec captures d'écran de la logique de la paire d'applications :

![flowNormal](https://github.com/AlexGreau/LiveCoaching/blob/master/readmeImages/flowNormal.PNG)

## Architecture



## Communication
Voici le schéma des communications du système :

![schemaNormal](https://github.com/AlexGreau/LiveCoaching/blob/master/readmeImages/schemaNormal.PNG)

Les communications sont faites par le "Server". Cette classe Server lance un thread avec un socket qui sera à l'écoute des messages envoyées par la montre et fera decoder ces ordres par l'activity voulue.

## Sauvegarde des données

La sauvegarde des données se fait dans des fichiers texte, pour pouvoir les manipuler aisément plus tard.
Ces fichiers ont besoin d'être publiques et accessibles par l'ordinateur lorsqu'il est branché afin de les manipuler.
Ils sont situés dans la mémoire externe de la tablette car la mémoire interne n'est accessible que par l'application.

L'écriture dans ces fichiers fichiers se fait par la classe "Logger", qui regroupe les fonction de formattage de nos données, d'écriture et de lecture.
