# Commandes utiles Git
## 1. Avant de créer une branche, mettre à jour la branche locale develop
`git pull`
## 2. Gestion des branches
### - Lister les branches existantes en local
`git branch`
### - Lister les branches existantes sur le serveur (remote)
`git branch -r`
### - Créer d'une branche et basculer directement dessus
`git checkout -b <nom-de-la-branche>`
### - Basculer sur une branche existante
`git checkout <nom-de-la-branche>`
### - Nommer les branches
**Important** : préfixer le nom de la branche par (Feature || Fix || Hotfix || Release ) en fonction de la nature de l'US, suivi du numéro de l'US et une description :
Feature/[numéro de l'US] + une description de l'US
*Exemple: feature/13-configuration-cache-redis*
## 3. Gestion des commits
### - Voir les modifications apportées aux fichiers :
`git status`
### - Sélectionner les fichiers à commiter
`git add . ` : permet de sélectionner tous les fichiers modifiés afin de les commit
`git add <nom-du-fichier>` : permet de sélectionner unitairement les fichiers à commit
### - Nommer les commits
il est important de nommer ses commits pour avoir une meilleure compréhension du travail réalisé
`git commit -m "[numéro Sprint][#numéro US][titre de l'US simplifié]:[message du commit] "`
Ex: git commit -m "[SP1][#6] CRUD Service MSP: création controller /v1/msps"
**Important** Ne pas oublier le `#` avant le numéro de l'US
## 4. Gestion des push
### - pousser des développements sur sa branche locale
`git push`
**Important : ** il est important de mettre à jour sa branche régulièrement pour récupérer les dernières modifications poussées sur la branche principale (develop) et éviter ainsi, d'avoir des conflits à la fin, lors du merge.
### - pousser sa branche locale pour la première fois sur le serveur
`git push --set-upstream origin <nom-de-la-branche>`
Ou
`git push origin <nom-de-la-branche>`
### - Mettre à jour sa branche avec la develop
- Basculer vers la develop `git checkout develop`
- Faire un `git pull`
- Rebasculer vers sa branche `git checkout <ma-branche>`
- mettre à jour sa branche avec la develop `git merge develop`
- Corriger les conflits s'il y en a
- Faire un push sur la branche remote `git push`
## 5. Gestion des stash
### - Qu'est-ce que le Stash?
Il arrive qu'on ait besoin de garder les modifications en cours de côté et de les récupérer ultérieurement. Pour cela, on utilise le stash
- Spécifier le nom du stash pour le retrouver facilement : `git stash save "nom-du-stash"`
- Utiliser le stash par défaut : `git stash save`
## - Voir la liste des stashs sauvegardés
`git stash list`
ex: lorsqu'on exécute cette commande, on a la liste suivante:
- stash@{0}: On develop : modification style page
- stash@{1}: On develop : ajout configuration redis
- stash@{2}: On feature/6_crud_service_msp: modification du yml
## - Appliquer un stash tout en le gardant dans la liste des stash
`git stash apply stash{n}`
n: Correspond au numéro du stash que l'on souhaiterait appliquer
## Appliquer un stash et le supprimer directement après l'avoir appliqué
`git stash pop stash{n}`
n: correspond au numéro du stash que l'on souhaiterait appliquer