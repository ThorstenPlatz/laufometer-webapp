package de.tp82.laufometer.persistence;

/**
 * @author Thorsten Platz
 */
public class EntityNotFoundException extends RuntimeException {
	private String entityId;

	public EntityNotFoundException(String entityId) {
		super("Entity with id='" + entityId + "' not found!");
		this.entityId = entityId;
	}

	public String getEntityId() {
		return entityId;
	}
}
