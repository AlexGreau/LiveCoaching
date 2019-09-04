# LiveCoaching

minSdkVersion 24
targetSdkVersion 28

Projet de Stage de fin d'études

Cette application est à utiliser avec l'application [LiveCoached](https://github.com/AlexGreau/LiveCoached) pour tablettes Android. Cette paire d'application sert à conduire une expérience cherchant à prouver nos hypothèses sur de nouvelles façons d'interagir en contexte actif. Voici le schéma avec captures d'écran de la logique de la paire d'applications :

![flowNormal](https://github.com/AlexGreau/LiveCoaching/blob/master/readmeImages/flowNormal.PNG)

## Architecture

Lorsque l'utilisateur appuie sur le bouton startExp, un dialogue apparait pour lui demander un pseudonyme, si celui ci est considéré valide par la fonction `protected boolean isValid(String text)`, alors il est sauvegadré par la MainActivity et est utilisé lors de la sauvegarde de données.
Ensuite, la MainActivity change son interface et crée un objet "Experiment".

L'objet Experiment, à l'image d'une experience, comporte un tableau de "Trials" et s'occupe de leur création avec les paramètres appropriés. L'Experiment gère aussi la transition d'un "Trial" à l'autre. C'est pour cela qu'il implémente l'interface "TrialOrganiser".

Un "Trial" a pour paramètre `public Trial(String ID, int interactionType, int difficulty, TrialOrganiser organiser)`. Cet objet s'occupe de garder les données brutes et calculer les données telles que le temps total mis pour réaliser cet essai, la distance totale parcourue et la distance totale théorique.

Ayant toutes les données, c'est le trial en cours qui se chargera de decoder les ordres et de suavegarder les données détaillée dans le fichier dédié.

L'afichage de ces données a l'écra se fait par un `public interface ExperimentVisualizer` implémenté par la mainActivity.

## Communication
Voici le schéma des communications du système :

![schemaNormal](https://github.com/AlexGreau/LiveCoaching/blob/master/readmeImages/schemaNormal.PNG)

Les communications sont faites par le "Server". Cette classe Server lance un thread avec un socket qui sera à l'écoute des messages envoyées par la montre et fera decoder ces ordres par l'activity voulue.

## Sauvegarde des données

La sauvegarde des données se fait dans des fichiers texte, pour pouvoir les manipuler aisément plus tard.
Ces fichiers ont besoin d'être publiques et accessibles par l'ordinateur lorsqu'il est branché afin de les manipuler.
Ils sont situés dans la mémoire externe de la tablette car la mémoire interne n'est accessible que par l'application.

L'écriture dans ces fichiers fichiers se fait par la classe "Logger", qui regroupe les fonction de formattage de nos données, d'écriture et de lecture.
