# GATEWAY Standardisation des MaaS



![schema-global](docs/assets/shema_global_gateway.png)

pour générer les classes Java sur la base d'une connexion à la base de données

`mvn antrun:run@hbm2java`

# Installation de l'environnement

Suivre les instructions d'installation du fichier docs > "Installation_Environnement.docx"

# Lancement de data-api

Avec l'utilisateur postgres :

+ créer une database nommée 'gateway' (avec l'owner postgres), puis créer dans cette base un schéma nommé 'msp' et un
  schéma nommé 'configuration'. Il s'agit de la base de dév local.
+ créer une database nommée 'gateway-tests' (avec l'owner postgres), puis créer dans cette base un schéma nommé 'msp' et
  un schéma nommé 'configuration'. Il s'agit de la base utilisée par les tests d'intégration lancés localement.

Afin de lancer data-api surchargez le parametre `--DATABASE_PASSWORD=[mon_password]`

NB :

+ Le schema msp contient les tables du data-mapping détaillées dans README_DATAMAPPING.md
+ Le schema configuration contient les données de configuration telle que le table gateway_params stockant les divers
  paramètres de la Gateway. Le paramètre GATEWAY_ACTIVATION de cette table indique si le cache est actif ou non et peut
  prendre la valeur soit 'true' soit 'false'

# Tests

## Lancement des tests

Au niveau du pom parent lancer install, elle lancera les phases test et verify respectivement pour les TU et TI.

`mvn clean install`

**Dans le fichier maven settings.xml de C:/Users/login/.m2/settings.xml, définir les login et mot de passe de la bdd locale et le répertoire ou point de montage des mocks API.**


```xml
      <test.database.username>postgres</test.database.username>
      <test.database.password>*******</test.database.password>
	
      <gateway.database.username>postgres</gateway.database.username>
      <gateway.database.password>*****</gateway.database.password>

      <gateway.service.mockapi.dir>*****/../******</gateway.service.mockapi.dir>
```

Dans les fichiers application.yml, les balises du type @gateway.database.password@ seront remplacés par leurs valeurs du
fait de présence dans le pom.xml des élements suivants:

```xml
 <resources>
    <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
    </resource>
</resources> 
```

et

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>2.7</version>
    <configuration>
        <delimiters>
            <delimiter>@</delimiter>
        </delimiters>
        <useDefaultDelimiters>false</useDefaultDelimiters>
    </configuration>
</plugin>
```


# Logs

Afin d'afficher les logs de requête de BDD passer le paramètre suivant au lancement `--SHOW_SQL=[true|false]`

# Exceptions

### Normes codes retours

Les endpoints exposés par la Gateway dans le module api sont divisés en 2 catégories : ceux qui suivent le standard TOMP et ceux qui respectent le standard COVOITURAGE.


| Catégorisation des enpoints      | endpoints                                            | Norme des réponses |
|----------------------------------|------------------------------------------------------| -------------------|
| Information voyageur             | POST /partners/around-me                                 | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/vehicle-types                      | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/system-pricing-plan                | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/stations-status                    | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/stations                           | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/available-assets                   | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/assets                             | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}/areas/{areaType}                   | TOMP v1.3.0        |
|                                  | GET /partners/global-view                                | TOMP v1.3.0        |
|                                  | GET /partners/{partnerId}                                    | TOMP v1.3.0        |
|                                  | GET /partners                                            | TOMP v1.3.0        |
| covoiturage Information Voyageur | GET /partners/{partnerId}/carpooling/passenger_regular_trips | Covoiturage        |
|                                  | GET /partners/{partnerId}/carpooling/passenger-journeys      | Covoiturage        |
|                                  | GET /partners/{partnerId}/carpooling/driver_regular_trips    | Covoiturage        |
|                                  | GET /partners/{partnerId}/carpooling/driver-journeys         | Covoiturage        |
| covoiturage reservation          | POST /carpooling/bookings/{bookingId}                | Covoiturage        |
|                                  | PATCH /carpooling/bookings/{bookingId}               | Covoiturage        |
|                                  | GET /carpooling/bookings/{bookingId}                 | Covoiturage        |
| covoiturage messagerie           | POST /carpooling/messages                            | Covoiturage        |
| covoiturage status               | GET /carpooling/status                               | Covoiturage        |
| covoiturage WebHook              | POST /carpooling/booking_events                      | Covoiturage        |

Les exceptions retournées doivent alors se conformer à la norme du standard suivi. En ce sens le UserContext expose deux variables outputStandard (standard suivi) et validCodes (codes retours supportés) qui sont affectés au niveau du APIController selon les normes des endpoints.
AU sein de le Gateway des erreurs au format propre à la Gateway sont soulevées (GenericError), puis au niveau du ResponseEntityExceptionHandler les retours sont construits en fonction du outputStandard, validCodes du enpoint afin de convertir l'exception soulevée au format attendu : Gateway (GenericError), TOMP (TompError) ou COVOITURAGE (CarpoolError).

Types d'erreurs par norme :

<table>
<tr>
<td> Norme </td> <td> Format </td>
</tr>
<tr>
<td> Gateway </td>
<td>

```json
{
  "status": int,
  "errorCode": int,
  "label": string,
  "description": string,
  "timestamp": "2020-04-28T00:00:00.000Z",
  "callId": "ee507da7-fc0b-464a-a2ee-25847fa6a073"
}
```
</td>
</tr>
<tr>
<td> Covoiturage </td>
<td>

```json
{
  "error": "string"
}
```

</td>
</tr>
<tr>
<td> TOMP v1.3.0</td>
<td>

```json
{
  "errorcode": 0,
  "type": "string",
  "title": "string",
  "status": 0,
  "detail": "string",
  "instance": "string"
}
```

</td>
</tr>
<tr>
<td> GBFS v2.3 </td>
<td>

</td>
</tr>
</table>


Matching des champs au format Gateway vers TOMP et vers TOMP enrichi : 

| Format Gateway | Format TOMP | Format TOMP enrichi |
|----------------|-------------| --------------------|
| status         | status      | status              |
| errorCode      | errorcode   | errorcode           |
| label          | title       | title               |
| description    | detail      | detail              |
| **???**        | type        | type                |
| callId         | instance    | instance            |
| timestamp      | N/A         | timestamp           |


### Gestion des exceptions

La gestion des exceptions se fait de manière centralisée dans le module common-api.

L'approche utilisée est l'utilisation du @ControllerAdvice étendant ResponseEntityExceptionHandler.

Toutes les exceptions spring-web sont catchées pour être retranscrites dans le format DTO défini par la gateway et représenté par GenericError du package com.gateway.commonapi.dto.exceptions

S'ils sont spécifiés lorsque les exceptions sont levées, errorCode, label et descriptions sont surchargées sinon c'est la valeur défini dans le fichier error.properties qui est utilisée.

Pour chaque code status http que nous souhaitons implémenté, une classe d'exception existe ainsi que son dto d'erreur.

Le ResponseEntityExceptionHandler contrôle les exceptions catchées, le outputstandard et les validCodes du context afin de convertir les erreurs au format attendu.

Lors de la levée d'une de nos exceptions à n'importe quelle couche du code (controller, service, utilitaire, ...) on peut définir le message et la classe d'erreur correspondant. L'exception sera catchée par le module centralisé de gestion des exceptions et contruira la réponse DTO basée sur l'exception reçue.

Le controller devra définir les Schemas utilisées pour les code d'erreur. Cela permet notamment d'avoir une doc swagger avec l'object exact correspondant au code d'erreur qu'on souhaite gérer.

Lors de la réception de l'exception, on vérifie la présence ou non d'un header correlationId servant à identifier la requête à l'origine de la traversée des différents mricoservices dans le but de mieux tracer les logs associés. S'il est présent, ce header est utilisé pour être injecté dans le champ de réponse callId.

Pour cela tout appel à un web service doit se faire avec un RestTemplate et en retransmettant le correlation id s'il existe.
Exemple :
```java
// get the correlationId of the current thread and forward as http header
        UserContext userContext = new ThreadLocalUserSession().get();
                String correlationId = userContext.getContextId();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
                HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
                
            .../...
        try {
            ResponseEntity<MspMetaDTO[]> mspMetasDto = restTemplate.exchange(urlGetMetas, HttpMethod.GET, entity, MspMetaDTO[].class);
            .../...
        } catch (NotFoundException e) {
            log.error("No metadata for MSP identifier {}", mm.getMspId(), e);
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorsProperties.getTechnicalRestHttpClientError(),urlGetMetas));
        } catch (RestClientException e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new BadGatewayException(MessageFormat.format(errorsProperties.getTechnicalRestHttpClientError(),urlGetMetas));
        } catch (Exception e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new UnavailableException(MessageFormat.format(errorsProperties.getTechnicalRestHttpClientError(),urlGetMetas));
        }
```

_ExceptionUtils.getMappedGatewayRuntimeException_ est très important, c'est ce qui permet de convertir les exceptions liées à l'appel REST effectué en tant que client à une des exceptions custom et la rendre ainsi "catchable" et donc gérer une réponse retranscrite dans le bon format.

Exemple de levée d'une exception:

```java
throw new BadGatewayException(MessageFormat.format(errorsProperties.getTechnicalRestHttpClientError(),urlGetMeta));
```

errorProperties est un Bean qui sert à accéder aux différents messages qu'on veut paramétrable afin d'en avoir le moins possible en dur dans le code surtout si le message est destiné à l'appelant du service et non uniquement un log.

Lors de la levée de l'exception, on peut passer l'objet global ou uniquement un message (ce qui est le cas ici) qui sera utilisé comme description.

Exemple de levée d'une exception avec un objet complet.

```java
   BadRequest errorBody = new BadRequest();
   errorBody.setErrorCode(Integer.valueOf(this.errorsProperties.getTechnicalMethodNotAllowedCode()));
   errorBody.setDescription(this.errorsProperties.getTechnicalMethodNotAllowedDescription());
   errorBody.setLabel(this.errorsProperties.getTechnicalMethodNotAllowedLabel());
   throw new BadRequestException(errorBody);
```

Lorsque la Gateway reçoit une erreur du partenaire interrogé, celle-ci est remontée jusqu'à api telle quelle (si le standard est suivi) ou transformée si besoin.



# Bonnes pratiques

## code

### Variables / Constantes / Fonctions

* Nommer les variables et les fonctions explicitement (éviter le mm, h, abr, toto1, test1, ... ) ainsi que les abréviations. Seul e ou ex est autorisé pour les exceptions catchées.
* Commencer les noms des fonctions et méthodes par un verbe
* Lorsqu'une fonction est privée déclarée dans le contexte d'une classe, utiliser le `.this.nom_de_la_methode()` avant le nom de celle-ci lorsqu'elle est appelée
* Favoriser l'utilisation des méthodes fournies par java comme `.equals, .filter, .stream, ... ` afin de gérer les conditions, ou autre...
* Sortir les string ou chaines de caractères dans des constantes déclarées au niveau de la classe, dans des dictionnaires pour les path d'url, en mettant bien : `private static final String = ".... "` (le nom doit être en majuscule)
  * Lorsque cette chaine est une url et contient des variables, utiliser le nom de la variable défini dans le swagger (ex {mspId}) et utiliser CommonUtils.placeholderFormat afin substituer la variable par sa valeur. 
  * Lorsque cette chaine est un message à destination de l'utilisateur, utiliser un fichier .properties pour stocker les labels et utiliser les variables de substitution {0} {1}, etc... couplé à MessageFormat.format pour la substitution.
* Pas d'instruction de code commenté.
* Les variables, fonctions, messages de logs, commentaires et javadoc en **anglais**. Seuls les messages provenant d'un fichier .properties peuvent être en Français.
* **Checker** vos **MR AVANT** DE DEMANDER A QUELQU'UN DE FAIRE UNE **REVUE** pour faire un premier control. La **pipeline** de test doit être **passante**.
* Vérifier l'**indentation** avec intellij (4 espaces comme motif d'indentation et non la tabulation)


### Exceptions

* Devront être privilégiées l'utilisation des exceptions de `com.gateway.commonapi.exception` en levant ce type d'exceptions sans les catcher puisqu'elle le sront par le @AdviceController.
* Les exceptions hors spring-web (nullPointerExceptions, ParsingExceptions, etc,...) devront être catchées et une des exceptions custom devra être levée dans le catch afin de "remplacer" l'exception et bénéficier du format d'erreur standadisé dans la Gateway.

### Autre 

* Les noms de packages en minuscules sans tirets ou underscore
* Loggers
   * Utiliser les annotations @Slf4j et @Data de lombok pour s'épargner à écrire du code
   * sur un log d'erreur, toujours renseigner l'exception en dernier paramètre du log afin de ne pas masquer la stack stace: exemple `log.error("Error parsing price List",e);`
   * si plusieurs ligne de code ou une boucle déclenche un log, encadrer par une condition `if(log.isDebugEnabled())` ou le niveau de code correspondant afin de ne pas évaluer la condition à chaque log.debug d'une boucle et la bypasser.
   * Utiliser au maximum le niveau debug pour les logs, info avec parcimonie, warn et error que pour des cas d'erreurs ou de risque d'instabillité nécessitant d'être traité. En production les logs debug voire info ne seront pas générés.
   * Ne jamais logger d'information utilisateur, apiKey, mot de passe ou information sensible sans la masquer

### Web services

|Sujet| Convention cible                            |
|:----|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|Langue| Anglais partout préconisé                            |
|Contrôleurs| Le nom du groupe d’opérations doit correspondre au nom de l’objet principal (ex. Citoyens au lieu de CitoyensController)<br/>Un seul groupe par objet métier principal<br/>La version figure dans l’url du endpoint au début (ex. /v1/demandes)                            |                            |
|URLs - Noms > Verbes| Utiliser les noms des objets métiers pour décrire les ressources.  <br/>On peut utiliser un verbe uniquement dans certains cas spécifiques (ex. GET /search sur une collection, POST /search), lorsqu’aucun verbe HTTP ne correspond à l’action réalisée. <br/>Note : La méthode HEAD peut être aussi utiliser pour obtenir un count d’une collection. |
|URLs – Pluriel / Singulier| Pluriel tout le temps                            |
|Casse cohérente - URLs| spinal-case (ex. /global-view, /msp-metas)                            |
|Casse cohérente – Body / Params| lowerCamelCase (fréquemment utilisée par les communautés Javascript, Java…) (ex. employeeId)                            |
|Casse cohérente                | seuls les attributs de base de données et tables de bdd sont en snake_case                            |
| Versionnement                 | Chaque URI doit commencer par un numéro de version sur un digit (ex. /v1 (ou v2, …)  <br/> Ex. /v1/demandes  <br/>On ne change la version qu’en cas de changement majeur, lorsque la rétrocompatibilité est impossible.                            |
| CRUD - verbes HTTP            | Utiliser systématiquement les méthodes HTTP (GET/ DELETE / POST / PATCH / PUT) pour décrire les actions. HEAD pourrait être utilisé le cas échéant.                            |
| Recherche                     | Utiliser /ressource/search?type=value                            |
| Filtres                       | Prévoir des filtres pour chaque GET des grandes collections.                            |
| Pagination                    | La réponse doit être HTTP 206, en fournissant offset / limit /count. <br/>Concerne tous les GET sur des grandes collections.                            |
| Réponses partielles           | Sur les GET des grandes collections, proposer un filtre permettant de choisir les champs à retourner. GET /users/007?fields=firstname,name,street                            |
| Tri                           | Sur les GET des grandes collections, proposer le paramètre sort.   <br/>Ex. ?desc=attribute1,attribute2                            |
| Codes statut HTTP- Succès     | Utiliser les codes retours adaptés.                            |
| Codes statut HTTP- Erreurs    | Spécifier les cas d’erreurs notamment le 400 Bad Request qui permet à l’utilisateur de comprendre pourquoi sa requête n’est pas bonne.  <br/>Les cas d’erreurs fonctionnels identifiés doivent apparaître dans la réponse et des exemples doivent apparaitre dans la doc. <br/>L’erreur 500 Server Error doit toujours être spécifié et retourner un format standard d’erreur.                            |
| Commentaires                  | Chaque URI doit avoir une description.<br/>Chaque URI doit avoir un exemple de réponse visible. <br/>Chaque paramètre doit avoir une description.                            |

### Cache Redis

Prérequis local

- Couper votre redis installé sous windows s'il n'est pas en vers 6.2 ou supérieure. Pour cela aller dans service en tant d'admin, chercher redis et désactiver le service et son lancement auto au redémarrage. 
- Installation de WSL2
- Lancer ubuntu for windows
- Installation de portainer.io en tant qu'image docker sur le ubuntu de WSL
  <pre>docker pull portainer/portainer-ce:2.11.0
  docker run -d --privileged -p 9010:9000 --name portainer --restart=always -v ///mnt/wsl/shared-docker/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:2.11.0
  </pre>

- aller sur http://localhost:9010/ avec un navigateur web. Lors de la première connection vous aurez à definir un mot de passe pour admin.
- Charger l'image redis dans portainer et lancer son run, sous Ubuntu, lancer les commandes :
  <pre>
  docker pull redis:6.2-alpine
  docker run -d --name redis-gateway -p 6379:6379 redis:6.2-alpine
  </pre>

Dans portainer.io:

Aller dans Home > local > Images > Vérifier que l'image existe

![image redis](docs/assets/portainer-image-redis.png)

Une fois l'image chargée, aller dans Container et vérifier que le container a bien été rajouté

![run container](docs/assets/portainer-container-run.png)














