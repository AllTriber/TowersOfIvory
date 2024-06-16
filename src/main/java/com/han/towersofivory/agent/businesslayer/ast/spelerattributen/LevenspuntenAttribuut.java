package com.han.towersofivory.agent.businesslayer.ast.spelerattributen;

import com.han.towersofivory.agent.businesslayer.ast.Attribuut;

public class LevenspuntenAttribuut extends Attribuut {
    @Override
    public String getNodeLabel() {
        return "Levenspunten";
    }
    public String getField(){return "hp";}
}
