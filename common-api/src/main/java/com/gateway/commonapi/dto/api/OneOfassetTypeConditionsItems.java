package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * TOMP-API Standard
* OneOfassetTypeConditionsItems
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConditionDeposit.class, name = "ConditionDeposit"),
  @JsonSubTypes.Type(value = ConditionPayWhenFinished.class, name = "ConditionPayWhenFinished"),
  @JsonSubTypes.Type(value = ConditionPostponedCommit.class, name = "ConditionPostponedCommit"),
  @JsonSubTypes.Type(value = ConditionRequireBookingData.class, name = "ConditionRequireBookingData"),
  @JsonSubTypes.Type(value = ConditionReturnArea.class, name = "ConditionReturnArea"),
  @JsonSubTypes.Type(value = ConditionUpfrontPayment.class, name = "ConditionUpfrontPayment")
})
public interface OneOfassetTypeConditionsItems extends Serializable {

}
