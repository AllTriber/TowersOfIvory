package com.han.towersofivory.agent.businesslayer.checker;

public class SemanticError {
    public final String description;

    public SemanticError(String description) {
        this.description = description;
    }

    public String toString() {
        return "Checker error: " + description;
    }
}
