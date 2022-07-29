package com.gateway.commonapi.dto.api;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.gateway.commonapi.utils.enums.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

/**
 * Bean for a list of potential actions.
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Validated
@Schema(description = "List of potential actions")
@JsonPropertyOrder({ "list" })
public class ActionList extends ArrayList<Action> {

    /** Serialization */
    private static final long serialVersionUID = -4672315511444001140L;

    /**
     * Default constructor.
     */
    public ActionList() {
        //
    }

    /**
     * Retrieve the action for a given type.
     *
     * @param type Type of action.
     * @return Action for the given type.
     */
    @JsonIgnore
    public Action getAction(ActionType type) {
        for (Action action : this) {
            if ((action != null) && (action.getType() == type)) {
                return action;
            }
        }
        return null;
    }

    /**
     * Add an action to be performed with a deep link.
     *
     * @param type Type of action.
     * @param deeplink Deep link.
     */
    public void addActionWithDeeplink(ActionType type, String deeplink) {
        Action action = createAction(type);
        action.setDeeplink(deeplink);
    }

    /**
     * Add an action to be performed with an API call.
     *
     * @param type Type of action.
     * @param call API call.
     */
    public void addActionWithAPICall(ActionType type, String call) {
        Action action = createAction(type);
        action.setApiCall(call);
    }

    /**
     * Add an action to be performed with a web view.
     *
     * @param type Type of action.
     * @param url URL to be displayed in a web view.
     */
    public void addActionWithWebView(ActionType type, String url) {
        Action action = createAction(type);
        action.setWebView(url);
    }

    /**
     * Add an action to be performed with an external browser.
     *
     * @param type Type of action.
     * @param url URL to be displayed in an external browser.
     */
    public void addActionWithUrl(ActionType type, String url) {
        Action action = createAction(type);
        action.setUrl(url);
    }

    /**
     * Retrieve or create an action for a given type of action.
     *
     * @param type Type of action.
     * @return Action for the given type of action.
     */
    private Action createAction(ActionType type) {
        Action action = getAction(type);
        if (action != null) {
            return action;
        }
        action = new Action();
        action.setType(type);
        add(action);
        return action;
    }
}

