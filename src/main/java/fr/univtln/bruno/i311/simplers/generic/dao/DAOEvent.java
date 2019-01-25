package fr.univtln.bruno.i311.simplers.generic.dao;

/**
 * Created by bruno on 28/03/15.
 */
public class DAOEvent {
    private Type type;
    private IdentifiableEntity entity;
    public DAOEvent(Type type, IdentifiableEntity entity) {
        this(type);
        this.entity = entity;
    }

    public DAOEvent(Type type) {
        this.type = type;
    }

    public enum Type {CREATE, UPDATE, DELETE}

    public Type getType() {
        return type;
    }

    public IdentifiableEntity getEntity() {
        return entity;
    }
}
