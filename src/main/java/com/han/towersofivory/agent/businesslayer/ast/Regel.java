package com.han.towersofivory.agent.businesslayer.ast;

import java.util.ArrayList;
import java.util.List;

public class Regel extends ASTNode {
    private Expressie conditionalExpression;
    private List<ASTNode> body = new ArrayList<>();

    public Regel() {
    }

    public Regel(Expressie conditionalExpression, List<ASTNode> body) {
        this.conditionalExpression = conditionalExpression;
        this.body = body;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public Expressie getConditionalExpression() {
        return conditionalExpression;
    }

    public void setConditionalExpression(Expressie conditionalExpression) {
        this.conditionalExpression = conditionalExpression;
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
        return "Regel";
    }
}
