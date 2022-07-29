package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets assetAccessMethods
 */
public enum AssetAccessMethods {
  DEEPLINK("DEEPLINK"),
    QR("QR"),
    AZTEC("AZTEC"),
    TOMP_API("TOMP-API"),
    AXA_EKEY_OTP("AXA-EKEY-OTP"),
    PHYSICAL_KEY("PHYSICAL-KEY"),
    BARCODE("BARCODE"),
    PDF("PDF"),
    HTML("HTML"),
    OVC("OVC"),
    EMV("EMV"),
    NONE("NONE");

  private String value;

  AssetAccessMethods(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static AssetAccessMethods fromValue(String text) {
    for (AssetAccessMethods b : AssetAccessMethods.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
