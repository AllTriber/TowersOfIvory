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
import com.han.towersofivory.agent.businesslayer.listeners.AgentGrammarParser;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Talk about a monster class
class AgentParserTest {

    private AgentParser sut;


    @BeforeEach
    void setUp() {
        sut = new AgentParser();
    }

    @Test
    void testIfGetASTReturnsAST() {
        // Arrange
        AST mockAST = new AST();

        // Act
        AST result = sut.getAST();

        // Assert
        assertEquals(mockAST, result, "The AST should be returned");
    }

    @Test
    void testIfGetCurrentContainerReturnsASTStack() {
        // Arrange
        ASTNode mockASTNode = mock(ASTNode.class);
        sut.getCurrentContainer().push(mockASTNode);

        // Act
        ASTNode result = sut.getCurrentContainer().peek();

        // Assert
        assertEquals(mockASTNode, result, "The current container should be returned");
    }

    @Test
    void testEnterAgentPushesNewAgentToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should initially have only the root item");

        // Act
        sut.enterAgent(mockCtx);

        // Assert

        assertEquals(2, sut.getCurrentContainer().size(), "A new Agent should be pushed onto the stack");
        assertEquals(Agent.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of Agent");
    }

    @Test
    void testExitAgentAddsAgentAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        sut.enterAgent(mockCtx);
        Agent topAgent = (Agent) sut.getCurrentContainer().peek();

        // Act
        sut.exitAgent(mockCtx);

        // Assert
        ASTNode root = sut.getCurrentContainer().peek();
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should only have the root item now");
        assertEquals(1, root.getChildren().size(), "The popped agent should be added as a child to the current top of the stack");
        assertEquals(topAgent, root.getChildren().getFirst(), "The popped agent should be the same as the agent that was on top of the stack");
    }

    @Test
    void testEnterRegelPushesNewRegelToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        sut.enterAgent(mockCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent on top of the root");

        // Act
        sut.enterRegel(mockRegelCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new Regel should be pushed onto the stack");
        assertEquals(Regel.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of Regel");
    }

    @Test
    void testExitRegelAddsRegelAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        Regel topRegel = (Regel) sut.getCurrentContainer().peek();

        // Act
        sut.exitRegel(mockRegelCtx);

        // Assert
        ASTNode agent = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and the agent");
        assertEquals(1, agent.getChildren().size(), "The popped regel should be added as a child to the current top of the stack");
        assertEquals(topRegel, agent.getChildren().getFirst(), "The popped regel should be the same as the regel that was on top of the stack");
    }

    @Test
    void testEnterExpressiePushesNewExpressieToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should have an Agent and Regel on top of the root");

        // Act
        sut.enterExpressie(mockExpressieCtx);

        // Assert
        assertEquals(4, sut.getCurrentContainer().size(), "A new Expressie should be pushed onto the stack");
        assertEquals(Expressie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of Expressie");
    }

    @Test
    void testExitExpressieAddsExpressieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        sut.enterExpressie(mockExpressieCtx);
        Expressie topExpressie = (Expressie) sut.getCurrentContainer().peek();

        // Act
        sut.exitExpressie(mockExpressieCtx);

        // Assert
        ASTNode regel = sut.getCurrentContainer().get(2);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should only have the root, agent and regel");
        assertEquals(1, regel.getChildren().size(), "The popped expressie should be added as a child to the current top of the stack");
        assertEquals(topExpressie, regel.getChildren().getFirst(), "The popped expressie should be the same as the expressie that was on top of the stack");
    }

    @Test
    void testEnterSpelerssituatiePushesNewSpelerssituatieToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should have an Agent and Regel on top of the root");

        // Act
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);

        // Assert
        assertEquals(4, sut.getCurrentContainer().size(), "A new Spelerssituatie should be pushed onto the stack");
        assertEquals(SpelerSituatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of ASTNode");
    }

    @Test
    void testExitSpelerssituatieAddsSpelerssituatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        SpelerSituatie topSpelerssituatie = (SpelerSituatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitSpelerssituatie(mockSpelerssituatieCtx);

        // Assert
        ASTNode regel = sut.getCurrentContainer().get(2);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should only have the root, agent and regel");

        assertEquals(1, regel.getChildren().size(), "The popped Spelerssituatie should be added as a child to the current top of the stack");
        assertEquals(topSpelerssituatie, regel.getChildren().getFirst(), "The popped Spelerssituatie should be the same as the Spelerssituatie that was on top of the stack");
    }

    @Test
    void testEnterOmgevingssituatiePushesNewOmgevingssituatieToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.OmgevingssituatieContext mockOmgevingssituatieCtx = mock(AgentGrammarParser.OmgevingssituatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should have an Agent and Regel on top of the root");

        // Act
        sut.enterOmgevingssituatie(mockOmgevingssituatieCtx);

        // Assert
        assertEquals(4, sut.getCurrentContainer().size(), "A new Omgevingssituatie should be pushed onto the stack");
        assertEquals(OmgevingSituatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of OmgevingSituatie");
    }

    @Test
    void testExitOmgevingssituatieAddsOmgevingssituatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.OmgevingssituatieContext mockOmgevingssituatieCtx = mock(AgentGrammarParser.OmgevingssituatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        sut.enterOmgevingssituatie(mockOmgevingssituatieCtx);
        OmgevingSituatie topOmgevingSituatie = (OmgevingSituatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitOmgevingssituatie(mockOmgevingssituatieCtx);

        // Assert
        ASTNode regel = sut.getCurrentContainer().get(2);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should only have the root, agent and regel");
        assertEquals(1, regel.getChildren().size(), "The popped Omgevingssituatie should be added as a child to the current top of the stack");
        assertEquals(topOmgevingSituatie, regel.getChildren().getFirst(), "The popped Omgevingssituatie should be the same as the Omgevingssituatie that was on top of the stack");
    }

    @Test
    void testEnterKamerObservatiePushesNewKamerObservatieToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.KamerObservatieContext mockKamerObservatieCtx = mock(AgentGrammarParser.KamerObservatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should have an Agent and Regel on top of the root");

        // Act
        sut.enterKamerObservatie(mockKamerObservatieCtx);

        // Assert
        assertEquals(4, sut.getCurrentContainer().size(), "A new KamerObservatie should be pushed onto the stack");
        assertEquals(KamerObservatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of KamerObservatie");
    }

    @Test
    void testExitKamerObservatieAddsKamerObservatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.KamerObservatieContext mockKamerObservatieCtx = mock(AgentGrammarParser.KamerObservatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        sut.enterKamerObservatie(mockKamerObservatieCtx);
        KamerObservatie topKamerObservatie = (KamerObservatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitKamerObservatie(mockKamerObservatieCtx);

        // Assert
        ASTNode regel = sut.getCurrentContainer().get(2);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should only have the root, agent and regel");
        assertEquals(1, regel.getChildren().size(), "The popped KamerObservatie should be added as a child to the current top of the stack");
        assertEquals(topKamerObservatie, regel.getChildren().getFirst(), "The popped KamerObservatie should be the same as the KamerObservatie that was on top of the stack");
    }

    @Test
    void testEnterGangObservatiePushesNewGangObservatieToStack() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.GangObservatieContext mockGangObservatieCtx = mock(AgentGrammarParser.GangObservatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should have an Agent and Regel on top of the root");

        // Act
        sut.enterGangObservatie(mockGangObservatieCtx);

        // Assert
        assertEquals(4, sut.getCurrentContainer().size(), "A new GangObservatie should be pushed onto the stack");
        assertEquals(GangObservatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of GangObservatie");

    }

    @Test
    void testExitGangObservatieAddsGangObservatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AgentContext mockCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        AgentGrammarParser.GangObservatieContext mockGangObservatieCtx = mock(AgentGrammarParser.GangObservatieContext.class);
        sut.enterAgent(mockCtx);
        sut.enterRegel(mockRegelCtx);
        sut.enterGangObservatie(mockGangObservatieCtx);
        GangObservatie topGangObservatie = (GangObservatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitGangObservatie(mockGangObservatieCtx);

        // Assert
        ASTNode regel = sut.getCurrentContainer().get(2);
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should only have the root, agent and regel");
        assertEquals(1, regel.getChildren().size(), "The popped GangObservatie should be added as a child to the current top of the stack");
        assertEquals(topGangObservatie, regel.getChildren().getFirst(), "The popped GangObservatie should be the same as the GangObservatie that was on top of the stack");
    }

    @Test
    void testEnterLevenspuntenAttribuutPushesNewLevenspuntenAttribuutToStack() {
        // Arrange
        AgentGrammarParser.LevenspuntenAttribuutContext mockCtx = mock(AgentGrammarParser.LevenspuntenAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel and Spelerssituatie on top of the root");

        // Act
        sut.enterLevenspuntenAttribuut(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new LevenspuntenAttribuut should be pushed onto the stack");
        assertEquals(LevenspuntenAttribuut.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of LevenspuntenAttribuut");
    }

    @Test
    void testExitLevenspuntenAttribuutAddsLevenspuntenAttribuutAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.LevenspuntenAttribuutContext mockCtx = mock(AgentGrammarParser.LevenspuntenAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        sut.enterLevenspuntenAttribuut(mockCtx);
        LevenspuntenAttribuut topLevenspuntenAttribuut = (LevenspuntenAttribuut) sut.getCurrentContainer().peek();

        // Act
        sut.exitLevenspuntenAttribuut(mockCtx);

        // Assert
        ASTNode spelerssituatie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and spelerssituatie");
        assertEquals(1, spelerssituatie.getChildren().size(), "The popped LevenspuntenAttribuut should be added as a child to the current top of the stack");
        assertEquals(topLevenspuntenAttribuut, spelerssituatie.getChildren().getFirst(), "The popped LevenspuntenAttribuut should be the same as the LevenspuntenAttribuut that was on top of the stack");
    }

    @Test
    void testEnterAanvalskrachtAttribuutPushesNewAanvalskrachtAttribuutToStack() {
        // Arrange
        AgentGrammarParser.AanvalskrachtAttribuutContext mockCtx = mock(AgentGrammarParser.AanvalskrachtAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel and Spelerssituatie on top of the root");

        // Act
        sut.enterAanvalskrachtAttribuut(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new AanvalskrachtAttribuut should be pushed onto the stack");
        assertEquals(AanvalsKrachtAttribuut.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of AanvalskrachtAttribuut");
    }

    @Test
    void testExitAanvalskrachtAttribuutAddsAanvalskrachtAttribuutAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AanvalskrachtAttribuutContext mockCtx = mock(AgentGrammarParser.AanvalskrachtAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        sut.enterAanvalskrachtAttribuut(mockCtx);
        AanvalsKrachtAttribuut topAanvalskrachtAttribuut = (AanvalsKrachtAttribuut) sut.getCurrentContainer().peek();

        // Act
        sut.exitAanvalskrachtAttribuut(mockCtx);

        // Assert
        ASTNode spelerssituatie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and spelerssituatie");
        assertEquals(1, spelerssituatie.getChildren().size(), "The popped AanvalskrachtAttribuut should be added as a child to the current top of the stack");
        assertEquals(topAanvalskrachtAttribuut, spelerssituatie.getChildren().getFirst(), "The popped AanvalskrachtAttribuut should be the same as the AanvalskrachtAttribuut that was on top of the stack");
    }

    @Test
    void testEnterMaximaleLevensAttribuutPushesNewMaximaleLevensAttribuutToStack() {
        // Arrange
        AgentGrammarParser.MaximaleLevensAttribuutContext mockCtx = mock(AgentGrammarParser.MaximaleLevensAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel and Spelerssituatie on top of the root");

        // Act
        sut.enterMaximaleLevensAttribuut(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new MaximaleLevensAttribuut should be pushed onto the stack");
        assertEquals(MaximaleLevensAttribuut.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of MaximaleLevensAttribuut");
    }

    @Test
    void testExitMaximaleLevensAttribuutAddsMaximaleLevensAttribuutAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.MaximaleLevensAttribuutContext mockCtx = mock(AgentGrammarParser.MaximaleLevensAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        sut.enterMaximaleLevensAttribuut(mockCtx);
        MaximaleLevensAttribuut topMaximaleLevensAttribuut = (MaximaleLevensAttribuut) sut.getCurrentContainer().peek();

        // Act
        sut.exitMaximaleLevensAttribuut(mockCtx);

        // Assert
        ASTNode spelerssituatie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and spelerssituatie");
        assertEquals(1, spelerssituatie.getChildren().size(), "The popped MaximaleLevensAttribuut should be added as a child to the current top of the stack");
        assertEquals(topMaximaleLevensAttribuut, spelerssituatie.getChildren().getFirst(), "The popped MaximaleLevensAttribuut should be the same as the MaximaleLevensAttribuut that was on top of the stack");
    }

    @Test
    void testEnterVerdedigingskrachtAttribuutPushesNewVerdedigingskrachtAttribuutToStack() {
        // Arrange
        AgentGrammarParser.VerdedigingskrachtAttribuutContext mockCtx = mock(AgentGrammarParser.VerdedigingskrachtAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel and Spelerssituatie on top of the root");

        // Act
        sut.enterVerdedigingskrachtAttribuut(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new VerdedigingskrachtAttribuut should be pushed onto the stack");
        assertEquals(VerdedigingsKrachtAttribuut.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of VerdedigingsKrachtAttribuut");
    }

    @Test
    void testExitVerdedigingskrachtAttribuutAddsVerdedigingskrachtAttribuutAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.VerdedigingskrachtAttribuutContext mockCtx = mock(AgentGrammarParser.VerdedigingskrachtAttribuutContext.class);
        AgentGrammarParser.SpelerssituatieContext mockSpelerssituatieCtx = mock(AgentGrammarParser.SpelerssituatieContext.class);
        sut.enterSpelerssituatie(mockSpelerssituatieCtx);
        sut.enterVerdedigingskrachtAttribuut(mockCtx);
        VerdedigingsKrachtAttribuut topVerdedigingskrachtAttribuut = (VerdedigingsKrachtAttribuut) sut.getCurrentContainer().peek();

        // Act
        sut.exitVerdedigingskrachtAttribuut(mockCtx);

        // Assert
        ASTNode spelerssituatie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and spelerssituatie");
        assertEquals(1, spelerssituatie.getChildren().size(), "The popped VerdedigingskrachtAttribuut should be added as a child to the current top of the stack");
        assertEquals(topVerdedigingskrachtAttribuut, spelerssituatie.getChildren().getFirst(), "The popped VerdedigingskrachtAttribuut should be the same as the VerdedigingskrachtAttribuut that was on top of the stack");
    }

    @Test
    void testEnterMeerDanOperatiePushesNewMeerDanOperatieToStack() {
        // Arrange
        AgentGrammarParser.MeerDanOperatieContext mockCtx = mock(AgentGrammarParser.MeerDanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and MeerDanOperatie on top of the root");

        // Act
        sut.enterMeerDanOperatie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new MeerDanOperatie should be pushed onto the stack");
        assertEquals(MeerDanOperatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of MeerDanOperatie");
    }

    @Test
    void testExitMeerDanOperatieAddsMeerDanOperatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.MeerDanOperatieContext mockCtx = mock(AgentGrammarParser.MeerDanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterMeerDanOperatie(mockCtx);
        MeerDanOperatie topMeerDanOperatie = (MeerDanOperatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitMeerDanOperatie(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped MeerDanOperatie should be added as a child to the current top of the stack");
        assertEquals(topMeerDanOperatie, expressie.getChildren().getFirst(), "The popped MeerDanOperatie should be the same as the MeerDanOperatie that was on top of the stack");
    }

    @Test
    void testEnterGelijkAanOperatiePushesNewGelijkAanOperatieToStack() {
        // Arrange
        AgentGrammarParser.GelijkAanOperatieContext mockCtx = mock(AgentGrammarParser.GelijkAanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and GelijkAanOperatie on top of the root");

        // Act
        sut.enterGelijkAanOperatie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new GelijkAanOperatie should be pushed onto the stack");
        assertEquals(GelijkAanOperatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of GelijkAanOperatie");
    }

    @Test
    void testExitGelijkAanOperatieAddsGelijkAanOperatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.GelijkAanOperatieContext mockCtx = mock(AgentGrammarParser.GelijkAanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterGelijkAanOperatie(mockCtx);
        GelijkAanOperatie topGelijkAanOperatie = (GelijkAanOperatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitGelijkAanOperatie(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped GelijkAanOperatie should be added as a child to the current top of the stack");
        assertEquals(topGelijkAanOperatie, expressie.getChildren().getFirst(), "The popped GelijkAanOperatie should be the same as the GelijkAanOperatie that was on top of the stack");
    }

    @Test
    void testEnterMinderDanOperatiePushesNewMinderDanOperatieToStack() {
        // Arrange
        AgentGrammarParser.MinderDanOperatieContext mockCtx = mock(AgentGrammarParser.MinderDanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and MinderDanOperatie on top of the root");

        // Act
        sut.enterMinderDanOperatie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new MinderDanOperatie should be pushed onto the stack");
        assertEquals(MinderDanOperatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of MinderDanOperatie");
    }

    @Test
    void testExitMinderDanOperatieAddsMinderDanOperatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.MinderDanOperatieContext mockCtx = mock(AgentGrammarParser.MinderDanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterMinderDanOperatie(mockCtx);
        MinderDanOperatie topMinderDanOperatie = (MinderDanOperatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitMinderDanOperatie(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped MinderDanOperatie should be added as a child to the current top of the stack");
        assertEquals(topMinderDanOperatie, expressie.getChildren().getFirst(), "The popped MinderDanOperatie should be the same as the MinderDanOperatie that was on top of the stack");
    }

    @Test
    void testEnterNietGelijkAanOperatiePushesNewNietGelijkAanOperatieToStack() {
        // Arrange
        AgentGrammarParser.NietGelijkAanOperatieContext mockCtx = mock(AgentGrammarParser.NietGelijkAanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and NietGelijkAanOperatie on top of the root");

        // Act
        sut.enterNietGelijkAanOperatie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new NietGelijkAanOperatie should be pushed onto the stack");
        assertEquals(NietGelijkAanOperatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of NietGelijkAanOperatie");
    }

    @Test
    void testExitNietGelijkAanOperatieAddsNietGelijkAanOperatieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.NietGelijkAanOperatieContext mockCtx = mock(AgentGrammarParser.NietGelijkAanOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterNietGelijkAanOperatie(mockCtx);
        NietGelijkAanOperatie topNietGelijkAanOperatie = (NietGelijkAanOperatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitNietGelijkAanOperatie(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped NietGelijkAanOperatie should be added as a child to the current top of the stack");
        assertEquals(topNietGelijkAanOperatie, expressie.getChildren().getFirst(), "The popped NietGelijkAanOperatie should be the same as the NietGelijkAanOperatie that was on top of the stack");
    }

    @Test
    void testEnterIntegerPushesNewIntegerToStack() {
        // Arrange
        AgentGrammarParser.IntegerContext mockCtx = mock(AgentGrammarParser.IntegerContext.class);
        TerminalNode mockTerminalNode = mock(TerminalNode.class);

        // Mock the INT() method to return the mocked TerminalNode
        when(mockCtx.INT()).thenReturn(mockTerminalNode);
        // Mock the getText() method to return a specific value, e.g., "42"
        when(mockTerminalNode.getText()).thenReturn("42");

        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);

        // The stack should have an Agent, Regel, Expressie on top of the root initially
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent and Expressie on top of the root");

        // Act
        sut.enterInteger(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new Integer should be pushed onto the stack");
        assertEquals(IntegerLiteral.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of Integer");
    }

    @Test
    void testExitIntegerAddsIntegerAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.IntegerContext mockCtx = mock(AgentGrammarParser.IntegerContext.class);
        TerminalNode mockTerminalNode = mock(TerminalNode.class);

        // Mock the INT() method to return the mocked TerminalNode
        when(mockCtx.INT()).thenReturn(mockTerminalNode);
        // Mock the getText() method to return a specific value, e.g., "42"
        when(mockTerminalNode.getText()).thenReturn("42");

        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterInteger(mockCtx);
        IntegerLiteral topInteger = (IntegerLiteral) sut.getCurrentContainer().peek();

        // Act
        sut.exitInteger(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped Integer should be added as a child to the current top of the stack");
        assertEquals(topInteger, expressie.getChildren().getFirst(), "The popped Integer should be the same as the Integer that was on top of the stack");
    }

    @Test
    void testEnterActiePushesNewActieToStack() {
        // Arrange
        AgentGrammarParser.ActieContext mockCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        sut.enterRegel(mockRegelCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent and Actie on top of the root");

        // Act
        sut.enterActie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new Actie should be pushed onto the stack");
        assertEquals(Actie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of Actie");
    }

    @Test
    void testExitActieAddsActieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.ActieContext mockCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.RegelContext mockRegelCtx = mock(AgentGrammarParser.RegelContext.class);
        sut.enterRegel(mockRegelCtx);
        sut.enterActie(mockCtx);
        Actie topActie = (Actie) sut.getCurrentContainer().peek();

        // Act
        sut.exitActie(mockCtx);

        // Assert
        ASTNode regel = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root, agent and regel");
        assertEquals(1, regel.getChildren().size(), "The popped Actie should be added as a child to the current top of the stack");
        assertEquals(topActie, regel.getChildren().getFirst(), "The popped Actie should be the same as the Actie that was on top of the stack");
    }

    @Test
    void testEnterDeurPushesNewDeurToStack() {
        // Arrange
        AgentGrammarParser.DeurContext mockCtx = mock(AgentGrammarParser.DeurContext.class);
        AgentGrammarParser.AgentContext mockAgentCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.ActieContext mockActieCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.InteracteerActieContext mockInteracteerActieCtx = mock(AgentGrammarParser.InteracteerActieContext.class);
        AgentGrammarParser.NabijheidsLocatieContext mockNabijheidsLocatieCtx = mock(AgentGrammarParser.NabijheidsLocatieContext.class);
        sut.enterAgent(mockAgentCtx);
        sut.enterActie(mockActieCtx);
        sut.enterInteracteerActie(mockInteracteerActieCtx);
        sut.enterNabijheidsLocatie(mockNabijheidsLocatieCtx);
        assertEquals(5, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Actie and Deur on top of the root");

        // Act
        sut.enterDeur(mockCtx);

        // Assert
        assertEquals(6, sut.getCurrentContainer().size(), "A new Deur should be pushed onto the stack");
        assertEquals(DeurAttribuut.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of Deur");
    }

    @Test
    void testExitDeurAddsDeurAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.DeurContext mockCtx = mock(AgentGrammarParser.DeurContext.class);
        AgentGrammarParser.AgentContext mockAgentCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.ActieContext mockActieCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.InteracteerActieContext mockInteracteerActieCtx = mock(AgentGrammarParser.InteracteerActieContext.class);
        AgentGrammarParser.NabijheidsLocatieContext mockNabijheidsLocatieCtx = mock(AgentGrammarParser.NabijheidsLocatieContext.class);
        sut.enterAgent(mockAgentCtx);
        sut.enterActie(mockActieCtx);
        sut.enterInteracteerActie(mockInteracteerActieCtx);
        sut.enterNabijheidsLocatie(mockNabijheidsLocatieCtx);
        sut.enterDeur(mockCtx);
        DeurAttribuut topDeur = (DeurAttribuut) sut.getCurrentContainer().peek();

        // Act
        sut.exitDeur(mockCtx);

        // Assert
        ASTNode nabijheidsLocatie = sut.getCurrentContainer().get(4);
        assertEquals(5, sut.getCurrentContainer().size(), "The stack should only have the root, agent, actie, interacteeractie and nabijheidslocatie");
        assertEquals(1, nabijheidsLocatie.getChildren().size(), "The popped Deur should be added as a child to the current top of the stack");
        assertEquals(topDeur, nabijheidsLocatie.getChildren().getFirst(), "The popped Deur should be the same as the Deur that was on top of the stack");
    }

    @Test
    void testEnterAanvalsActiePushesNewAanvalsActieToStack() {
        // Arrange
        AgentGrammarParser.AanvalsActieContext mockCtx = mock(AgentGrammarParser.AanvalsActieContext.class);
        AgentGrammarParser.AgentContext mockAgentCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.ActieContext mockActieCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.InteracteerActieContext mockInteracteerActieCtx = mock(AgentGrammarParser.InteracteerActieContext.class);
        AgentGrammarParser.NabijheidsLocatieContext mockNabijheidsLocatieCtx = mock(AgentGrammarParser.NabijheidsLocatieContext.class);
        sut.enterAgent(mockAgentCtx);
        sut.enterActie(mockActieCtx);
        sut.enterInteracteerActie(mockInteracteerActieCtx);
        sut.enterNabijheidsLocatie(mockNabijheidsLocatieCtx);
        assertEquals(5, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Actie and AanvalsActie on top of the root");

        // Act
        sut.enterAanvalsActie(mockCtx);

        // Assert
        assertEquals(6, sut.getCurrentContainer().size(), "A new AanvalsActie should be pushed onto the stack");
        assertEquals(AanvalsActie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of AanvalsActie");
    }


    @Test
    void testExitAanvalsActieAddsAanvalsActieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.AanvalsActieContext mockCtx = mock(AgentGrammarParser.AanvalsActieContext.class);
        AgentGrammarParser.AgentContext mockAgentCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.ActieContext mockActieCtx = mock(AgentGrammarParser.ActieContext.class);
        sut.enterAgent(mockAgentCtx);
        sut.enterActie(mockActieCtx);
        sut.enterAanvalsActie(mockCtx);
        AanvalsActie topAanvalsActie = (AanvalsActie) sut.getCurrentContainer().peek();

        // Act
        sut.exitAanvalsActie(mockCtx);

        // Assert
        ASTNode nabijheidsLocatie = sut.getCurrentContainer().getLast();
        assertEquals(3, sut.getCurrentContainer().size(), "The stack should only have the root, agent, and actie");
        assertEquals(1, nabijheidsLocatie.getChildren().size(), "The popped AanvalsActie should be added as a child to the current top of the stack");
        assertEquals(topAanvalsActie, nabijheidsLocatie.getChildren().getFirst(), "The popped AanvalsActie should be the same as the AanvalsActie that was on top of the stack");
    }

    @Test
    void testEnterInteracteerActiePushesNewInteracteerActieToStack() {
        // Arrange
        AgentGrammarParser.InteracteerActieContext mockCtx = mock(AgentGrammarParser.InteracteerActieContext.class);
        AgentGrammarParser.AgentContext mockAgentCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.ActieContext mockActieCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.NabijheidsLocatieContext mockNabijheidsLocatieCtx = mock(AgentGrammarParser.NabijheidsLocatieContext.class);
        sut.enterAgent(mockAgentCtx);
        sut.enterActie(mockActieCtx);
        sut.enterNabijheidsLocatie(mockNabijheidsLocatieCtx);
        assertEquals(4, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Actie and InteracteerActie on top of the root");

        // Act
        sut.enterInteracteerActie(mockCtx);

        // Assert
        assertEquals(5, sut.getCurrentContainer().size(), "A new InteracteerActie should be pushed onto the stack");
        assertEquals(InteracteerActie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of InteracteerActie");
    }

    @Test
    void testExitInteracteerActieAddsInteracteerActieAsChildToCurrentTop() {
        // Arrange
        AgentGrammarParser.InteracteerActieContext mockCtx = mock(AgentGrammarParser.InteracteerActieContext.class);
        AgentGrammarParser.AgentContext mockAgentCtx = mock(AgentGrammarParser.AgentContext.class);
        AgentGrammarParser.ActieContext mockActieCtx = mock(AgentGrammarParser.ActieContext.class);
        AgentGrammarParser.NabijheidsLocatieContext mockNabijheidsLocatieCtx = mock(AgentGrammarParser.NabijheidsLocatieContext.class);
        sut.enterAgent(mockAgentCtx);
        sut.enterActie(mockActieCtx);
        sut.enterNabijheidsLocatie(mockNabijheidsLocatieCtx);
        sut.enterInteracteerActie(mockCtx);
        InteracteerActie topInteracteerActie = (InteracteerActie) sut.getCurrentContainer().peek();

        // Act
        sut.exitInteracteerActie(mockCtx);

        // Assert
        ASTNode nabijheidsLocatie = sut.getCurrentContainer().get(3);
        assertEquals(4, sut.getCurrentContainer().size(), "The stack should only have the root, agent, actie and nabijheidslocatie");
        assertEquals(1, nabijheidsLocatie.getChildren().size(), "The popped InteracteerActie should be added as a child to the current top of the stack");
        assertEquals(topInteracteerActie, nabijheidsLocatie.getChildren().getFirst(), "The popped InteracteerActie should be the same as the InteracteerActie that was on top of the stack");
    }

    @Test
    void testEnterEnOperatie() {
        // Arrange
        AgentGrammarParser.EnOperatieContext mockCtx = mock(AgentGrammarParser.EnOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and EnOperatie on top of the root");

        // Act
        sut.enterEnOperatie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new EnOperatie should be pushed onto the stack");
        assertEquals(EnOperatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of EnOperatie");
    }

    @Test
    void testExitEnOperatie() {
        // Arrange
        AgentGrammarParser.EnOperatieContext mockCtx = mock(AgentGrammarParser.EnOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterEnOperatie(mockCtx);
        EnOperatie topEnOperatie = (EnOperatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitEnOperatie(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped EnOperatie should be added as a child to the current top of the stack");
        assertEquals(topEnOperatie, expressie.getChildren().getFirst(), "The popped EnOperatie should be the same as the EnOperatie that was on top of the stack");
    }

    @Test
    void testEnterOfOperatie() {
        // Arrange
        AgentGrammarParser.OfOperatieContext mockCtx = mock(AgentGrammarParser.OfOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and OfOperatie on top of the root");

        // Act
        sut.enterOfOperatie(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new OfOperatie should be pushed onto the stack");
        assertEquals(OfOperatie.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of OfOperatie");
    }

    @Test
    void testExitOfOperatie() {
        // Arrange
        AgentGrammarParser.OfOperatieContext mockCtx = mock(AgentGrammarParser.OfOperatieContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterOfOperatie(mockCtx);
        OfOperatie topOfOperatie = (OfOperatie) sut.getCurrentContainer().peek();

        // Act
        sut.exitOfOperatie(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped OfOperatie should be added as a child to the current top of the stack");
        assertEquals(topOfOperatie, expressie.getChildren().getFirst(), "The popped OfOperatie should be the same as the OfOperatie that was on top of the stack");
    }

    @Test
    void testEnterGangRichting() {
        // Arrange
        AgentGrammarParser.GangRichtingContext mockCtx = mock(AgentGrammarParser.GangRichtingContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and GangRichting on top of the root");

        // Act
        sut.enterGangRichting(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new GangRichting should be pushed onto the stack");
        assertEquals(GangRichting.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of GangRichting");
    }

    @Test
    void testExitGangRichting() {
        // Arrange
        AgentGrammarParser.GangRichtingContext mockCtx = mock(AgentGrammarParser.GangRichtingContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterGangRichting(mockCtx);
        GangRichting topGangRichting = (GangRichting) sut.getCurrentContainer().peek();

        // Act
        sut.exitGangRichting(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped GangRichting should be added as a child to the current top of the stack");
        assertEquals(topGangRichting, expressie.getChildren().getFirst(), "The popped GangRichting should be the same as the GangRichting that was on top of the stack");
    }

    @Test
    void testEnterSpeler() {
        // Arrange
        AgentGrammarParser.SpelerContext mockCtx = mock(AgentGrammarParser.SpelerContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have an Agent, Regel, Expressie and SpelerAttribuut on top of the root");

        // Act
        sut.enterSpeler(mockCtx);

        // Assert
        assertEquals(3, sut.getCurrentContainer().size(), "A new SpelerAttribuut should be pushed onto the stack");
        assertEquals(SpelerAttribuut.class, sut.getCurrentContainer().peek().getClass(), "The top of the stack should be an instance of SpelerAttribuut");
    }

    @Test
    void testExitSpeler() {
        // Arrange
        AgentGrammarParser.SpelerContext mockCtx = mock(AgentGrammarParser.SpelerContext.class);
        AgentGrammarParser.ExpressieContext mockExpressieCtx = mock(AgentGrammarParser.ExpressieContext.class);
        sut.enterExpressie(mockExpressieCtx);
        sut.enterSpeler(mockCtx);
        SpelerAttribuut topSpeler = (SpelerAttribuut) sut.getCurrentContainer().peek();

        // Act
        sut.exitSpeler(mockCtx);

        // Assert
        ASTNode expressie = sut.getCurrentContainer().get(1);
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should only have the root and expressie");
        assertEquals(1, expressie.getChildren().size(), "The popped Speler should be added as a child to the current top of the stack");
        assertEquals(topSpeler, expressie.getChildren().getFirst(), "The popped Speler should be the same as the Speler that was on top of the stack");
    }

    @Test
    void testEnterNaarRichting() {
        // Arrange
        AgentGrammarParser.NaarRichtingContext mockCtx = mock(AgentGrammarParser.NaarRichtingContext.class);

        // Act
        sut.enterNaarRichting(mockCtx);

        // Assert
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have one element");
        assertInstanceOf(NaarRichting.class, sut.getCurrentContainer().peek(), "The top element should be an instance of NaarRichting");
    }

    @Test
    void testExitNaarRichting() {
        // Arrange
        AgentGrammarParser.NaarRichtingContext mockCtx = mock(AgentGrammarParser.NaarRichtingContext.class);
        NaarRichting naarRichting = new NaarRichting();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(naarRichting);

        // Act
        sut.exitNaarRichting(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(naarRichting);
    }

    @Test
    void testEnterWegRichting() {
        // Arrange
        AgentGrammarParser.WegRichtingContext mockCtx = mock(AgentGrammarParser.WegRichtingContext.class);

        // Act
        sut.enterWegRichting(mockCtx);

        // Assert
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have one element");
        assertInstanceOf(WegRichting.class, sut.getCurrentContainer().peek(), "The top element should be an instance of WegRichting");
    }

    @Test
    void testExitWegRichting() {
        // Arrange
        AgentGrammarParser.WegRichtingContext mockCtx = mock(AgentGrammarParser.WegRichtingContext.class);
        WegRichting wegRichting = new WegRichting();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(wegRichting);

        // Act
        sut.exitWegRichting(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(wegRichting);
    }

    @Test
    void testExitNabijheidsLocatie() {
        // Arrange
        AgentGrammarParser.NabijheidsLocatieContext mockCtx = mock(AgentGrammarParser.NabijheidsLocatieContext.class);
        NabijheidsLocatie nabijheid = new NabijheidsLocatie();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(nabijheid);

        // Act
        sut.exitNabijheidsLocatie(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(nabijheid);
    }

    @Test
    void testEnterDichtstbijzijnde() {
        // Arrange
        AgentGrammarParser.DichtstbijzijndeContext mockCtx = mock(AgentGrammarParser.DichtstbijzijndeContext.class);

        // Act
        sut.enterDichtstbijzijnde(mockCtx);

        // Assert
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have one element");
        assertInstanceOf(Dichtstbijzijnde.class, sut.getCurrentContainer().peek(), "The top element should be an instance of Dichtstbijzijnde");
    }

    @Test
    void testExitDichtstbijzijnde() {
        // Arrange
        AgentGrammarParser.DichtstbijzijndeContext mockCtx = mock(AgentGrammarParser.DichtstbijzijndeContext.class);
        Dichtstbijzijnde dichtstbijzijnde = new Dichtstbijzijnde();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(dichtstbijzijnde);

        // Act
        sut.exitDichtstbijzijnde(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(dichtstbijzijnde);
    }

    @Test
    void testEnterVerste() {
        // Arrange
        AgentGrammarParser.VersteContext mockCtx = mock(AgentGrammarParser.VersteContext.class);

        // Act
        sut.enterVerste(mockCtx);

        // Assert
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have one element");
        assertInstanceOf(Verste.class, sut.getCurrentContainer().peek(), "The top element should be an instance of Verste");
    }

    @Test
    void testExitVerste() {
        // Arrange
        AgentGrammarParser.VersteContext mockCtx = mock(AgentGrammarParser.VersteContext.class);
        Verste verste = new Verste();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(verste);

        // Act
        sut.exitVerste(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(verste);
    }

    @Test
    void testEnterTrap() {
        // Arrange
        AgentGrammarParser.TrapContext mockCtx = mock(AgentGrammarParser.TrapContext.class);

        // Act
        sut.enterTrap(mockCtx);

        // Assert
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have one element");
        assertInstanceOf(TrapAttribuut.class, sut.getCurrentContainer().peek(), "The top element should be an instance of TrapAttribuut");
    }

    @Test
    void testExitTrap() {
        // Arrange
        AgentGrammarParser.TrapContext mockCtx = mock(AgentGrammarParser.TrapContext.class);
        TrapAttribuut trapAttribuut = new TrapAttribuut();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(trapAttribuut);

        // Act
        sut.exitTrap(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(trapAttribuut);
    }

    @Test
    void testEnterLoopActie() {
        // Arrange
        AgentGrammarParser.LoopActieContext mockCtx = mock(AgentGrammarParser.LoopActieContext.class);

        // Act
        sut.enterLoopActie(mockCtx);

        // Assert
        assertEquals(2, sut.getCurrentContainer().size(), "The stack should have one element");
        assertInstanceOf(LoopActie.class, sut.getCurrentContainer().peek(), "The top element should be an instance of LoopActie");
    }

    @Test
    void testExitLoopActie() {
        // Arrange
        AgentGrammarParser.LoopActieContext mockCtx = mock(AgentGrammarParser.LoopActieContext.class);
        LoopActie loopActie = new LoopActie();
        ASTNode parent = mock(ASTNode.class);
        sut.getCurrentContainer().clear(); // Ensure the stack is empty before the test
        sut.getCurrentContainer().push(parent);
        sut.getCurrentContainer().push(loopActie);

        // Act
        sut.exitLoopActie(mockCtx);

        // Assert
        assertEquals(1, sut.getCurrentContainer().size(), "The stack should have one element");
        assertSame(parent, sut.getCurrentContainer().peek(), "The top element should be the parent node");
        verify(parent).addChild(loopActie);
    }
}