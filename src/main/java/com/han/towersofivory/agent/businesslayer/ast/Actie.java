package com.han.towersofivory.agent.businesslayer.ast;

import java.util.ArrayList;
import java.util.List;

public class Actie extends ASTNode {

    private List<ASTNode> body = new ArrayList<>();

    public Actie() {
    }

    public Actie(List<ASTNode> body) {
        this.body = body;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<ASTNode> getBody() {
        return body;
    }

    public void setBody(List<ASTNode> body) {
        this.body = body;
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
        return "Actie";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }


}
