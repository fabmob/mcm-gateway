package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TOMP-API
 * Gets or Sets day
 */
public enum Day {
  MON("MON"),
    TUE("TUE"),
    WED("WED"),
    THU("THU"),
    FRI("FRI"),
    SAT("SAT"),
    SUN("SUN");

  private String value;

  Day(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Day fromValue(String text) {
    for (Day b : Day.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
