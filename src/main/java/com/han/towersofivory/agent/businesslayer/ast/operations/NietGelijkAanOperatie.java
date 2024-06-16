package com.han.towersofivory.agent.businesslayer.ast.operations;

import com.han.towersofivory.agent.businesslayer.ast.Operatie;

public class NietGelijkAanOperatie extends Operatie {
    @Override
    public String getNodeLabel() {
        return "niet gelijk aan";
    }
}
