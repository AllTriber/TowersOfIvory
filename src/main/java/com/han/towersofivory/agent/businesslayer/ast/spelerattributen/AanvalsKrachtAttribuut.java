package com.han.towersofivory.agent.businesslayer.ast.spelerattributen;

import com.han.towersofivory.agent.businesslayer.ast.Attribuut;

public class AanvalsKrachtAttribuut extends Attribuut {
    @Override
    public String getNodeLabel() {
        return "Aanvalskracht";
    }

    public String getField(){return "atk";}
}
