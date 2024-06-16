package com.han.towersofivory.agent.businesslayer.ast;

import java.util.ArrayList;
import java.util.List;

public class Expressie extends ASTNode {

    private List<ASTNode> body;

    public Expressie() {
        this.body = new ArrayList<>();
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public List<ASTNode> getChildren() {
        return this.body;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        body.add(child);
        return this;
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        body.remove(child);
        return this;
    }


    @Override
    public String getNodeLabel() {
        return "Expressie";
    }
}
