package com.han.towersofivory.agent.businesslayer.ast.operations;

import com.han.towersofivory.agent.businesslayer.ast.Operatie;

public class GelijkAanOperatie extends Operatie {
    @Override
    public String getNodeLabel() {
        return " is gelijk aan ";
    }
}
