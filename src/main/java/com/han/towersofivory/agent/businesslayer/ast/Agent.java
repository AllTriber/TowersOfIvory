package com.han.towersofivory.agent.businesslayer.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A stylesheet is the root node of the AST, it consists of one or more statements
 */
public class Agent extends ASTNode {

    private List<ASTNode> body;

    public Agent() {
        this.body = new ArrayList<>();
    }

    public Agent(List<ASTNode> body) {
        this.body = body;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    public void setBody(List<ASTNode> body) {
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "Agent";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Agent that = (Agent) o;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
