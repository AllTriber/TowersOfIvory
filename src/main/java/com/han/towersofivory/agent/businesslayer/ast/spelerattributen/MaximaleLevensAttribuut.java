package com.han.towersofivory.agent.businesslayer.ast.spelerattributen;

import com.han.towersofivory.agent.businesslayer.ast.Attribuut;

public class MaximaleLevensAttribuut extends Attribuut {
    @Override
    public String getNodeLabel() {
        return "Maximale levens";
    }
    public String getField(){return "maxHp";}

}
