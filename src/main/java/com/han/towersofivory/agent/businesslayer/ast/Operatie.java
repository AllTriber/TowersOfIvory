package com.han.towersofivory.agent.businesslayer.ast;

import java.util.ArrayList;

public class Operatie extends Expressie {
    private Expressie leftHandSide;
    private Expressie rightHandSide;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if (leftHandSide != null) {
            children.add(leftHandSide);
        }
        if (rightHandSide != null) {
            children.add(rightHandSide);
        }
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (leftHandSide == null) {
            leftHandSide = (Expressie) child;
        } else if (rightHandSide == null) {
            rightHandSide = (Expressie) child;
        }
        return this;
    }
}
