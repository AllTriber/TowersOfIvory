package com.han.towersofivory.agent.businesslayer.ast.spelerattributen;

import com.han.towersofivory.agent.businesslayer.ast.Attribuut;

public class VerdedigingsKrachtAttribuut extends Attribuut {
    @Override
    public String getNodeLabel() {
        return "Verdedigingskracht";
    }
    public String getField(){return "def";}
}
