package de.tp82.laufometer.web.api.rest;

/**
 * @author Thorsten Platz
 */
public enum Audience {
	PUBLIC("public"), ADMIN("admin");

	private String audienceString;

	private Audience(String audience) {
		this.audienceString = audience;
	}

	@Override
	public String toString() {
		return this.audienceString;
	}

	public static Audience from(String string) {
		for(Audience audience : Audience.values()) {
			if(audience.toString().equals(string))
				return audience;
		}
		throw new IllegalArgumentException("String '" + string + "' is not a valid audience!");
	}
}
