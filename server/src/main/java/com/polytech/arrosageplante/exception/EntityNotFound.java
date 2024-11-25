package com.polytech.arrosageplante.exception;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound() {
        super("Entity was not found in database");
    }
}
