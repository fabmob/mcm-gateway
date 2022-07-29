package com.gateway.api.util.enums;

/**
 * Enumeration for quote type.
 */
public enum QuoteType {

	FIXED,
	ESTIMATED,
	METERED;
	
	public static QuoteType getQuoteType(String value) {
		QuoteType quoteType = null;
		for (QuoteType qt : QuoteType.values()) {
			if(value.equals(qt.name())) {
				quoteType = qt;
				break;
			}
		}
		return quoteType;
	}
}