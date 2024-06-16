package com.han.towersofivory.agent.businesslayer.parser;

import com.han.towersofivory.agent.businesslayer.ast.*;
import com.han.towersofivory.agent.businesslayer.ast.acties.AanvalsActie;
import com.han.towersofivory.agent.businesslayer.ast.acties.InteracteerActie;
import com.han.towersofivory.agent.businesslayer.ast.acties.LoopActie;
import com.han.towersofivory.agent.businesslayer.ast.literal.IntegerLiteral;
import com.han.towersofivory.agent.businesslayer.ast.looprichtingen.GangRichting;
import com.han.towersofivory.agent.businesslayer.ast.looprichtingen.NaarRichting;
import com.han.towersofivory.agent.businesslayer.ast.looprichtingen.WegRichting;
import com.han.towersofivory.agent.businesslayer.ast.nabijheid.Dichtstbijzijnde;
import com.han.towersofivory.agent.businesslayer.ast.nabijheid.Verste;
import com.han.towersofivory.agent.businesslayer.ast.observatie.GangObservatie;
import com.han.towersofivory.agent.businesslayer.ast.observatie.KamerObservatie;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.DeurAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.SpelerAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.TrapAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.operations.GelijkAanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.operations.MeerDanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.operations.MinderDanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.operations.NietGelijkAanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.AanvalsKrachtAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.LevenspuntenAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.MaximaleLevensAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.VerdedigingsKrachtAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.voegwoorden.EnOperatie;
import com.han.towersofivory.agent.businesslayer.ast.voegwoorden.OfOperatie;
import com.han.towersofivory.agent.businesslayer.listeners.AgentGrammarBaseListener;
import com.han.towersofivory.agent.businesslayer.listeners.AgentGrammarParser;

import java.util.Stack;


public class AgentParser extends AgentGrammarBaseListener {
    private final AST ast;

    @SuppressWarnings("all")
    private final Stack<ASTNode> currentContainer;

    public AgentParser() {
        ast = new AST();
        currentContainer = new Stack<>();
        currentContainer.push(ast.getRoot());
    }

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterAgent(AgentGrammarParser.AgentContext ctx) {
        Agent agent = new Agent();
        currentContainer.push(agent);
    }

    @Override
    public void exitAgent(AgentGrammarParser.AgentContext ctx) {
        Agent agent = (Agent) currentContainer.pop();
        currentContainer.peek().addChild(agent);
    }

    @Override
    public void enterRegel(AgentGrammarParser.RegelContext ctx) {
        Regel regel = new Regel();
        currentContainer.push(regel);
    }

    @Override
    public void exitRegel(AgentGrammarParser.RegelContext ctx) {
        Regel regel = (Regel) currentContainer.pop();
        currentContainer.peek().addChild(regel);
    }

    @Override
    public void enterExpressie(AgentGrammarParser.ExpressieContext ctx) {
        Expressie expressie = new Expressie();
        currentContainer.push(expressie);
    }

    @Override
    public void exitExpressie(AgentGrammarParser.ExpressieContext ctx) {
        Expressie expressie = (Expressie) currentContainer.pop();
        currentContainer.peek().addChild(expressie);
    }

    @Override
    public void enterSpelerssituatie(AgentGrammarParser.SpelerssituatieContext ctx) {
        SpelerSituatie spelerSituatie = new SpelerSituatie();
        currentContainer.push(spelerSituatie);
    }

    @Override
    public void exitSpelerssituatie(AgentGrammarParser.SpelerssituatieContext ctx) {
        SpelerSituatie spelerSituatie = (SpelerSituatie) currentContainer.pop();
        currentContainer.peek().addChild(spelerSituatie);
    }

    @Override
    public void enterOmgevingssituatie(AgentGrammarParser.OmgevingssituatieContext ctx) {
        OmgevingSituatie omgevingSituatie = new OmgevingSituatie();
        currentContainer.push(omgevingSituatie);
    }

    @Override
    public void exitOmgevingssituatie(AgentGrammarParser.OmgevingssituatieContext ctx) {
        OmgevingSituatie omgevingSituatie = (OmgevingSituatie) currentContainer.pop();
        currentContainer.peek().addChild(omgevingSituatie);
    }

    @Override
    public void enterKamerObservatie(AgentGrammarParser.KamerObservatieContext ctx) {
        KamerObservatie kamerObservatie = new KamerObservatie();
        currentContainer.push(kamerObservatie);
    }

    @Override
    public void exitKamerObservatie(AgentGrammarParser.KamerObservatieContext ctx) {
        KamerObservatie kamerObservatie = (KamerObservatie) currentContainer.pop();
        currentContainer.peek().addChild(kamerObservatie);
    }

    @Override
    public void enterGangObservatie(AgentGrammarParser.GangObservatieContext ctx) {
        GangObservatie gangObservatie = new GangObservatie();
        currentContainer.push(gangObservatie);
    }

    @Override
    public void exitGangObservatie(AgentGrammarParser.GangObservatieContext ctx) {
        GangObservatie gangObservatie = (GangObservatie) currentContainer.pop();
        currentContainer.peek().addChild(gangObservatie);
    }

    @Override
    public void enterLevenspuntenAttribuut(AgentGrammarParser.LevenspuntenAttribuutContext ctx) {
        LevenspuntenAttribuut levenspuntenAttribuut = new LevenspuntenAttribuut();
        currentContainer.push(levenspuntenAttribuut);
    }

    @Override
    public void exitLevenspuntenAttribuut(AgentGrammarParser.LevenspuntenAttribuutContext ctx) {
        LevenspuntenAttribuut levenspuntenAttribuut = (LevenspuntenAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(levenspuntenAttribuut);
    }

    @Override
    public void enterAanvalskrachtAttribuut(AgentGrammarParser.AanvalskrachtAttribuutContext ctx) {
        AanvalsKrachtAttribuut aanvalsKrachtAttribuut = new AanvalsKrachtAttribuut();
        currentContainer.push(aanvalsKrachtAttribuut);
    }

    @Override
    public void exitAanvalskrachtAttribuut(AgentGrammarParser.AanvalskrachtAttribuutContext ctx) {
        AanvalsKrachtAttribuut aanvalsKrachtAttribuut = (AanvalsKrachtAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(aanvalsKrachtAttribuut);
    }

    @Override
    public void enterMaximaleLevensAttribuut(AgentGrammarParser.MaximaleLevensAttribuutContext ctx) {
        MaximaleLevensAttribuut maximaleLevensAttribuut = new MaximaleLevensAttribuut();
        currentContainer.push(maximaleLevensAttribuut);
    }

    @Override
    public void exitMaximaleLevensAttribuut(AgentGrammarParser.MaximaleLevensAttribuutContext ctx) {
        MaximaleLevensAttribuut maximaleLevensAttribuut = (MaximaleLevensAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(maximaleLevensAttribuut);
    }

    @Override
    public void enterVerdedigingskrachtAttribuut(AgentGrammarParser.VerdedigingskrachtAttribuutContext ctx) {
        VerdedigingsKrachtAttribuut verdedigingsKrachtAttribuut = new VerdedigingsKrachtAttribuut();
        currentContainer.push(verdedigingsKrachtAttribuut);
    }

    @Override
    public void exitVerdedigingskrachtAttribuut(AgentGrammarParser.VerdedigingskrachtAttribuutContext ctx) {
        VerdedigingsKrachtAttribuut verdedigingsKrachtAttribuut = (VerdedigingsKrachtAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(verdedigingsKrachtAttribuut);
    }

    @Override
    public void enterMeerDanOperatie(AgentGrammarParser.MeerDanOperatieContext ctx) {
        MeerDanOperatie meerDan = new MeerDanOperatie();
        currentContainer.push(meerDan);
    }

    @Override
    public void exitMeerDanOperatie(AgentGrammarParser.MeerDanOperatieContext ctx) {
        MeerDanOperatie meerDan = (MeerDanOperatie) currentContainer.pop();
        currentContainer.peek().addChild(meerDan);
    }

    @Override
    public void enterGelijkAanOperatie(AgentGrammarParser.GelijkAanOperatieContext ctx) {
        GelijkAanOperatie gelijkAan = new GelijkAanOperatie();
        currentContainer.push(gelijkAan);
    }

    @Override
    public void exitGelijkAanOperatie(AgentGrammarParser.GelijkAanOperatieContext ctx) {
        GelijkAanOperatie gelijkAan = (GelijkAanOperatie) currentContainer.pop();
        currentContainer.peek().addChild(gelijkAan);
    }

    @Override
    public void enterMinderDanOperatie(AgentGrammarParser.MinderDanOperatieContext ctx) {
        MinderDanOperatie minderDan = new MinderDanOperatie();
        currentContainer.push(minderDan);
    }

    @Override
    public void exitMinderDanOperatie(AgentGrammarParser.MinderDanOperatieContext ctx) {
        MinderDanOperatie minderDan = (MinderDanOperatie) currentContainer.pop();
        currentContainer.peek().addChild(minderDan);
    }

    @Override
    public void enterNietGelijkAanOperatie(AgentGrammarParser.NietGelijkAanOperatieContext ctx) {
        NietGelijkAanOperatie nietGelijkAan = new NietGelijkAanOperatie();
        currentContainer.push(nietGelijkAan);
    }

    @Override
    public void exitNietGelijkAanOperatie(AgentGrammarParser.NietGelijkAanOperatieContext ctx) {
        NietGelijkAanOperatie nietGelijkAan = (NietGelijkAanOperatie) currentContainer.pop();
        currentContainer.peek().addChild(nietGelijkAan);
    }

    @Override
    public void enterInteger(AgentGrammarParser.IntegerContext ctx) {
        IntegerLiteral integerLiteral = new IntegerLiteral(ctx.INT().getText());
        currentContainer.push(integerLiteral);
    }

    @Override
    public void exitInteger(AgentGrammarParser.IntegerContext ctx) {
        IntegerLiteral integerLiteral = (IntegerLiteral) currentContainer.pop();
        currentContainer.peek().addChild(integerLiteral);
    }

    @Override
    public void enterActie(AgentGrammarParser.ActieContext ctx) {
        Actie actie = new Actie();
        currentContainer.push(actie);
    }

    @Override
    public void exitActie(AgentGrammarParser.ActieContext ctx) {
        Actie actie = (Actie) currentContainer.pop();
        currentContainer.peek().addChild(actie);
    }

    @Override
    public void enterTrap(AgentGrammarParser.TrapContext ctx) {
        TrapAttribuut trapAttribuut = new TrapAttribuut();
        currentContainer.push(trapAttribuut);
    }

    @Override
    public void exitTrap(AgentGrammarParser.TrapContext ctx) {
        TrapAttribuut trapAttribuut = (TrapAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(trapAttribuut);
    }

    @Override
    public void enterDeur(AgentGrammarParser.DeurContext ctx) {
        DeurAttribuut deurAttribuut = new DeurAttribuut();
        currentContainer.push(deurAttribuut);
    }

    @Override
    public void exitDeur(AgentGrammarParser.DeurContext ctx) {
        DeurAttribuut deurAttribuut = (DeurAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(deurAttribuut);
    }

    @Override
    public void enterLoopActie(AgentGrammarParser.LoopActieContext ctx) {
        LoopActie loopActie = new LoopActie();
        currentContainer.push(loopActie);
    }

    @Override
    public void exitLoopActie(AgentGrammarParser.LoopActieContext ctx) {
        LoopActie loopActie = (LoopActie) currentContainer.pop();
        currentContainer.peek().addChild(loopActie);
    }

    @Override
    public void enterAanvalsActie(AgentGrammarParser.AanvalsActieContext ctx) {
        AanvalsActie aanvalsActie = new AanvalsActie();
        currentContainer.push(aanvalsActie);
    }

    @Override
    public void exitAanvalsActie(AgentGrammarParser.AanvalsActieContext ctx) {
        AanvalsActie aanvalsActie = (AanvalsActie) currentContainer.pop();
        currentContainer.peek().addChild(aanvalsActie);
    }

    @Override
    public void enterInteracteerActie(AgentGrammarParser.InteracteerActieContext ctx) {
        InteracteerActie interacteerActie = new InteracteerActie();
        currentContainer.push(interacteerActie);
    }

    @Override
    public void exitInteracteerActie(AgentGrammarParser.InteracteerActieContext ctx) {
        InteracteerActie interacteerActie = (InteracteerActie) currentContainer.pop();
        currentContainer.peek().addChild(interacteerActie);
    }

    @Override
    public void enterNabijheidsLocatie(AgentGrammarParser.NabijheidsLocatieContext ctx) {
        NabijheidsLocatie nabijheid = new NabijheidsLocatie();
        currentContainer.push(nabijheid);
    }

    @Override
    public void exitNabijheidsLocatie(AgentGrammarParser.NabijheidsLocatieContext ctx) {
        NabijheidsLocatie nabijheid = (NabijheidsLocatie) currentContainer.pop();
        currentContainer.peek().addChild(nabijheid);
    }

    @Override
    public void enterDichtstbijzijnde(AgentGrammarParser.DichtstbijzijndeContext ctx) {
        Dichtstbijzijnde dichtstbijzijnde = new Dichtstbijzijnde();
        currentContainer.push(dichtstbijzijnde);
    }

    @Override
    public void exitDichtstbijzijnde(AgentGrammarParser.DichtstbijzijndeContext ctx) {
        Dichtstbijzijnde dichtstbijzijnde = (Dichtstbijzijnde) currentContainer.pop();
        currentContainer.peek().addChild(dichtstbijzijnde);
    }

    @Override
    public void enterVerste(AgentGrammarParser.VersteContext ctx) {
        Verste verste = new Verste();
        currentContainer.push(verste);
    }

    @Override
    public void exitVerste(AgentGrammarParser.VersteContext ctx) {
        Verste verste = (Verste) currentContainer.pop();
        currentContainer.peek().addChild(verste);
    }

    @Override
    public void enterEnOperatie(AgentGrammarParser.EnOperatieContext ctx) {
        EnOperatie enOperatie = new EnOperatie();
        currentContainer.push(enOperatie);
    }

    @Override
    public void exitEnOperatie(AgentGrammarParser.EnOperatieContext ctx) {
        EnOperatie enOperatie = (EnOperatie) currentContainer.pop();
        currentContainer.peek().addChild(enOperatie);
    }

    @Override
    public void enterOfOperatie(AgentGrammarParser.OfOperatieContext ctx) {
        OfOperatie ofOperatie = new OfOperatie();
        currentContainer.push(ofOperatie);
    }

    @Override
    public void exitOfOperatie(AgentGrammarParser.OfOperatieContext ctx) {
        OfOperatie ofOperatie = (OfOperatie) currentContainer.pop();
        currentContainer.peek().addChild(ofOperatie);
    }

    @Override
    public void enterGangRichting(AgentGrammarParser.GangRichtingContext ctx) {
        GangRichting gangRichting = new GangRichting();
        currentContainer.push(gangRichting);
    }

    @Override
    public void exitGangRichting(AgentGrammarParser.GangRichtingContext ctx) {
        GangRichting gangRichting = (GangRichting) currentContainer.pop();
        currentContainer.peek().addChild(gangRichting);
    }

    @Override
    public void enterSpeler(AgentGrammarParser.SpelerContext ctx) {
        SpelerAttribuut spelerAttribuut = new SpelerAttribuut();
        currentContainer.push(spelerAttribuut);
    }

    @Override
    public void exitSpeler(AgentGrammarParser.SpelerContext ctx) {
        SpelerAttribuut spelerAttribuut = (SpelerAttribuut) currentContainer.pop();
        currentContainer.peek().addChild(spelerAttribuut);
    }

    @Override
    public void enterNaarRichting(AgentGrammarParser.NaarRichtingContext ctx) {
        NaarRichting naarRichting = new NaarRichting();
        currentContainer.push(naarRichting);
    }

    @Override
    public void exitNaarRichting(AgentGrammarParser.NaarRichtingContext ctx) {
        NaarRichting naarRichting = (NaarRichting) currentContainer.pop();
        currentContainer.peek().addChild(naarRichting);
    }

    @Override
    public void enterWegRichting(AgentGrammarParser.WegRichtingContext ctx) {
        WegRichting wegRichting = new WegRichting();
        currentContainer.push(wegRichting);
    }

    @Override
    public void exitWegRichting(AgentGrammarParser.WegRichtingContext ctx) {
        WegRichting wegRichting = (WegRichting) currentContainer.pop();
        currentContainer.peek().addChild(wegRichting);
    }

    @SuppressWarnings("all")
    public Stack<ASTNode> getCurrentContainer() {
        return currentContainer;
    }

}
