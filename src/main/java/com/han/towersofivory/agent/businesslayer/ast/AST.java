package com.han.towersofivory.agent.businesslayer.ast;

import com.han.towersofivory.agent.businesslayer.checker.SemanticError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AST {

    private Agent root;

    public AST() {
        root = new Agent();
    }

    public AST(Agent agent) {
        root = agent;
    }

    public Agent getRoot() {
        return root;
    }

    public void setRoot(Agent agent) {
        root = agent;
    }

    public List<SemanticError> getErrors() {
        ArrayList<SemanticError> errors = new ArrayList<>();
        collectErrors(errors, root);
        return errors;
    }

    private void collectErrors(ArrayList<SemanticError> errors, ASTNode node) {
        if (node.hasError()) {
            errors.add(node.getError());
        }
        for (ASTNode child : node.getChildren()) {
            collectErrors(errors, child);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST ast = (AST) o;
        return Objects.equals(root, ast.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }
}
