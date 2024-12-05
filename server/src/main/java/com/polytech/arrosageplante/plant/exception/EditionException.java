package com.polytech.arrosageplante.plant.exception;

public class EditionException extends RuntimeException {
    public EditionException() {
        super("Erreur lors de l'édition de la plante");
    }
}
