package it.euris.group1.modules.controllers;

public class ModuleNotFoundException extends Exception {
    public ModuleNotFoundException() {
    }

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleNotFoundException(Throwable cause) {
        super(cause);
    }
}
