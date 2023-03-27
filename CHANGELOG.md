# Changelog

Ce fichier liste tous les changements notables sur le projet [Gateway].
Le versionning des release suit le [semantic versioning](http://semver.org).

### 0.9.1

- [x] Mise en place des Horizontal Pod Autoscaler

### 0.8.2

- [x] Mise en place des requests timeout au niveau de la gateway + variabilisation CI/CD
- [x] Mise à jour des descriptions des endpoints API

### 0.7.2
- [x] Mise à jour de sécurité sur la dépendance client postgres 
- [x] Mise en place de KPI fonctionnelles pour la réservation à l'aide Grafana et Elasticsearch
- [x] Mise en place de l'Application Performance Monitoring (APM) d'Elasticsearch sur les microservices de la gateway
- [x] Correction des anomalies liées au cache et à la verbosité des erreurs

### 0.6.3
- [x] Séparation des namespaces, job installation et nom du service elasticsearch de l'installation gravitee
- [x] Ajout de nouveaux paramètres de déploiement à la CI/CD pour le déploiement sécurisé d'elasticsearch et gravitee
- [x] Mise en place de persistance des disques Elastic par storageClass
- [x] Mise en place de KPI fonctionnelles à l'aide Grafana et Elasticsearch
- [x] Corrections de vulnérabilités sur la librairie jackson
- [x] Correction d'anomalies mineures


### 0.5.4
- [x] Activation des logs et analytics Gravitee & autorisation de Grafana à contacter Elasticsearch
- [x] Amélioration du déploiement de gravitee et elasticsaerch par les helm charts
- [x] Montée de version de gravitee de  3.18.11 à 3.19.1
- [x] Nouvelles options sur la pipeline de publication de code source vers github
- [x] Optimisation du la lecture du status d'utilisation du cache afin de supprimer le délai
- [x] Mise à jour documentation gravitee
- [x] Sécurité : masquer la verbosité des erreurs sur le module api
- [x] Correction de la liste des service sur l'api management avec changement des context-path des microservices api et dataapi

### 0.4.6
- [x] Fix pipeline de mise en production

### 0.4.5

- [X] Version 2022-11-04
  #### Fonctionnalités générales
- [X] API de point d'entrée unique pour les MaaS
- [X] Aiguillage des requêtes MaaS vers l'adaptateur pour le format du MSP concerné
- [X] Envoi des requêtes aux MSP
- [X] Recensement/découverte des services des MSPs
- [X] Données en cache pour optimisation des réponses
- [X] Publication Opensource
- [X] Audit du code et de l'infrastructure
- [X] Environnements techniques

  #### Fonctionnalités par service de mobilité
    - Covoiturage (Standard Covoiturage https://github.com/fabmob/standard-covoiturage)
        - [X] Information Voyageur (IV)
        - [X] Réservation
        - [X] Usage
    - Stationnement ([Tomp API](https://app.swaggerhub.com/apis-docs/TOMP-API-WG/transport-operator_maas_provider_api/1.3.0))
        - [X] Information Voyageur (IV)
    - VLS ([Tomp API](https://app.swaggerhub.com/apis-docs/TOMP-API-WG/transport-operator_maas_provider_api/1.3.0))
        - [X] Information Voyageur (IV)
    - Auto-partage ([Tomp API](https://app.swaggerhub.com/apis-docs/TOMP-API-WG/transport-operator_maas_provider_api/1.3.0))
        - [X] Information Voyageur (IV)
    - Free-floating ([Tomp API](https://app.swaggerhub.com/apis-docs/TOMP-API-WG/transport-operator_maas_provider_api/1.3.0))
        - [X] Information Voyageur (IV)
