> **Note** : La publication du code source est accompagné de documents permettant à l’usager d’appréhender facilement les éléments partagés. Basé un mode de fonctionnement agile, les éléments constitutifs du projet sont susceptibles d’évoluer au fil de l’eau, en amont de tout déploiement éventuel, du fait : des retours des tests et de tout autre élément susceptible de conduire à des modifications

# Rappel du contexte
Le programme Mon Compte Mobilité, porté par Capgemini Invent et la Fabrique des Mobilité, est une plateforme de services neutre qui facilite les relations entre citoyens, employeurs, collectivités et opérateurs de mobilité autour d’un compte personnel de mobilité et d'une passerelle (i.e. Gateway) d'échanges de services standardisés à destination des MaaS. Son ambition est d’accélérer les mutations des mobilités pour réduire massivement l’autosolisme et encourager l’utilisation des mobilités douces.
Ce programme répond parfaitement à une des propositions de la convention citoyenne : mettre en place un portail unique permettant de savoir à tout moment, rapidement et simplement, quels sont les moyens et dispositifs existants sur un territoire pour se déplacer.

Le projet Mon Compte Mobilité est lauréat de l’appel à projet pour des programmes de Certificats d’économie d’énergie par l’arrêté du 27 février 2020, et publié au journal officiel le 8 mars 2020.

La Gateway Mon Compte Mobilité Standardisation des MaaS est une plateforme de services et d'interface API qui permet :
- Pour le citoyen, de réduire la dispersion des offres de mobilité sur les territoires qui lui sont proposées et ainsi augmenter la multimodalité et la portée géographique
- Pour l'AOM (Autorité Organisatrice de la Mobilité), de changer les comportements grâce à une mobilité plus fluide et de permettre l’émergence de nouveaux acteurs engagés pour la transition énergétique
- Pour les MSP (Mobility Service Providers), de réduire la redondance et la complexité d'intégration à des plateformes de mobilité et les coûts de création et de maintien des interfaces
- Pour les solutions MaaS (Mobility as a Service), de créer des synergies entre les MaaS et réduire les coûts de développement des interfaces spécifiques avec les MSP

Le développement de la plateforme est incrémental, et expérimenté sur des territoires pilotes à partir de fin 2022, en partenariat avec plusieurs Autorités Organisatrices de la Mobilité et opérateurs de mobilité.

# Processus de création de la plateforme Gateway Mon Compte Mobilité Std MaaS
- Dans un mode agile, les développeurs membres de l’équipe projet Mon Compte Mobilité Std MaaS ont travaillé à l’implémentation des premières briques fonctionnelles APIs et de son infrastructure, dans l’optique de proposer une plateforme déployable opérationnellement, dans le cadre d’un calendrier fixé par le programme CEE Mon Compte Mobilité.
- La publication des codes sources et de la documentation se poursuivra pendant toute la durée du projet. L’évolution du code prévoit l’analyse et l’intégration éventuelle des améliorations qui seront soumises par la communauté des développeurs.
- Les mises à jour de l’application seront disponibles au fur et à mesure du déroulé des sprints de l'équipe de développement.

# Principe général de publication
Pour permettre aux différentes communautés de développeurs et de spécialistes d’expertiser la façon dont cette plateforme est programmée, le code source est publié sur [https://github.com/fabmob/mcm-gateway](https://github.com/fabmob/mcm-gateway), géré par l'organisation de la Fabrique des Mobilités, co-porteur du programme. Le code source présenté est le résultat d’un processus de développement collaboratif impliquant de nombreuses personnes et organisations au sein de l’équipe projet Mon Compte Mobilité.
Ce processus de développement collaboratif va s’ouvrir progressivement pour permettre de proposer des évolutions à la plateforme, de signaler des bugs, de proposer des changements pour la documentation et de suivre la prise en compte ou non de ces propositions. Pour ce faire, le choix de la plateforme GitHub a été retenu.
Les contributions attendues par la communauté des développeurs permettront de faire évoluer des briques logicielles pour, au final, étendre les fonctionnalités et améliorer la qualité de l’application. Pour contribuer, merci de prendre connaissance du fichier [CONTRIBUTING.md](https://github.com/fabmob/mcm-gateway). La plateforme GitHub n’a pas vocation à héberger les débats d’ordre plus général, politique ou sociétal. La politique de publication du code source développé dans le cadre du projet repose sur 2 catégories :
- Une partie (restreinte) qui n’est pas publiée car correspondant à des tests ou à des parties critiques pour la sécurité de l’infrastructure ;
- Une partie qui relève à strictement parler de l’open source, avec des appels à contribution qui sont attendus : cela concerne le cœur de l’application.
# Phases de publication en Open Source
L’équipe projet Mon Compte Mobilité publiera le code source par le biais de pull requests, à la fin des sprints de développement Agile. Ce phasage ne remet pas en question les principes fondamentaux de publication ouverte du code mais permet une meilleure gestion et s'adapte au fonctionnement actuel du projet.
# # Phase 1 : transparence
Désormais visible, le code peut être revu par tous ceux qui le souhaitent. En le rendant public, l’équipe projet Mon Compte Mobilité respecte son engagement de transparence.
Les personnes externes à l’équipe projet Mon Compte Mobilité peuvent, à ce stade, donner un avis, faire remonter des suggestions ou des commentaires.
Toutes les contributions seront lues attentivement afin de pouvoir retenir celles qui seront jugées pertinentes voire qui seront susceptibles de jouer un rôle critique à ce stade du développement du code.
# # Phase 2 : contribution
La phase de contribution permettra à la communauté de contribuer au logiciel tout en respectant les mécanismes de régulation qui seront mis en place (essentiellement via de la revue de code et une acceptation ou un rejet par un comité de validation).
A ce stade, le travail de la communauté des développeurs, qu’ils soient internes ou externes au projet, sera précieux. Un temps d’intégration avec des process transparents sera précisé sous la responsabilité d’un comité de validation.
# Description des sous-projets
Le projet principal est découpé en plusieurs sous-projets dont l’articulation globale est détaillée dans le document [README.md](https://github.com/fabmob/mcm-gateway#readme).
# Contribution au projet
Pour contribuer au projet, merci de prendre connaissance du fichier [CONTRIBUTING.md](https://github.com/fabmob/mcm-gateway).
# Licence
Merci de vous référer au fichier dédié : [LICENSE.txt](https://github.com/fabmob/mcm-gateway)
# Liens
- La présentation du projet Mon Compte Mobilité : [https://moncomptemobilite.fr/](https://moncomptemobilite.fr/)
- La page [LinkedIn](https://www.linkedin.com/showcase/mon-compte-mobilit%C3%A9/)

# Roadmap produit
Sera déterminé et partagé prochainement.