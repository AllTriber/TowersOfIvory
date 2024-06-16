package com.han.towersofivory.agent.businesslayer.ast.literal;

import com.han.towersofivory.agent.businesslayer.ast.Literal;

public class IntegerLiteral extends Literal {
    private int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    public IntegerLiteral(String value) {
        this.value = Integer.parseInt(value);
    }

    @Override
    public String getNodeLabel() {
        return "IntegerLiteral (" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
