package com.gateway.routingapi.util.constant;

public class RoutingMessageDict {
    private RoutingMessageDict() {
    }

    public static final String ROUTING_TAG = "Routing";
    public static final String REPONSE_OK = "Réponse OK";
    public static final String REQUETE_MAL_FORMEE_OU_NON_VALIDE = "Requête mal formée ou non valide";
    public static final String REQUETE_NON_AUTORISEE_JETON_OAUTH_2_KO_OU_ABSENT = "Requête non autorisée (jeton oauth2 KO ou absent)";
    public static final String DONNEE_NON_TROUVEE = "Donnée non trouvée";
    public static final String ERREUR_INTERNE = "Erreur interne";
    public static final String LE_SERVEUR_DISTANT_EST_INDISPONIBLE = "Le serveur distant est indisponible";

    public static final String PARTNER_ACTIONS_NAME = "partnerActionsName";
    public static final String PARTNER_ID = "partnerId";
    public static final String DEFAULT_ADAPTER_NAME = "default-adapter";
    public static final String GENERIC_ADAPTER_NAME = "generic-adapter";
    public static final String ADAPT = "adapt";

    public static final String NO_ACTIVE_ACTION_FOUND = "No active version of action {partnerActionsName} found in standards for partner({partnerId})";
    public static final String ROUTING_SERVICE_CALL_URL = "Routing service will call {}";
    public static final String FAIL_CONTACTING_URL_WITH_MESSAGE = "Fail contacting {}. {}";
}
