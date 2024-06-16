package com.han.towersofivory.agent.businesslayer.transformer;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgentTransformerTest {

    AgentTransformer transformer;
    AST ast;
    Agent rootAgent;
    Agent nestedAgent;
    Regel regel;
    Expressie expressie;
    Actie actie;
    String expectedStartCode =
            """
                    import java.lang.reflect.Field;
                    import com.han.towersofivory.agent.businesslayer.ast.*;
                    import java.util.HashMap;
                    public class TestAgent {
                    public static <T> String getAction(T character) {
                    try {
                    int hp = character.getClass().getField("hp").getInt(character);
                    int atk = character.getClass().getField("atk").getInt(character);
                    int def = character.getClass().getField("def").getInt(character);
                    int maxHp = character.getClass().getField("maxHp").getInt(character);
                    HashMap<String, int> objects = character.getClass().getField("vision").get(character);
                    int door = objects.get("door");
                    int player = objects.get("player");
                    int stairs = objects.get("stairs");
                    boolean inRoom = objects.get("inRoom");
                    """;
    String expectedEndCode =
            """
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                    }
                    }
                    }
                    """;

    @BeforeEach
    public void setUp() {
        transformer = new AgentTransformer();
        ast = new AST();
        rootAgent = new Agent();
        nestedAgent = new Agent();
        regel = new Regel();
        expressie = new Expressie();
        actie = new Actie();
        regel.getChildren().add(expressie);
        regel.getChildren().add(actie);
        nestedAgent.getChildren().add(regel);
        rootAgent.getChildren().add(nestedAgent);
        ast.setRoot(rootAgent);
    }

    @Test
    void testTransformAST_WanneerIkInEenGangBenDanLoopIkDeGangDoor_Success() {
        // Build the AST
        OmgevingSituatie omgevingSituatie = new OmgevingSituatie();
        omgevingSituatie.getChildren().add(new GangObservatie());
        expressie.getChildren().add(omgevingSituatie);

        LoopActie loopActie = new LoopActie();
        loopActie.getChildren().add(new GangRichting());

        actie.getChildren().add(loopActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (!inRoom) {\nreturn \"move through corridor\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerMijnLevenspuntenMinderDan10ZijnDanLoopIkNaarDeDichtstbijzijndeDeur_Success() {
        // Build the AST
        SpelerSituatie spelerSituatie = new SpelerSituatie();
        spelerSituatie.getChildren().add(new LevenspuntenAttribuut());
        spelerSituatie.getChildren().add(new MinderDanOperatie());
        spelerSituatie.getChildren().add(new IntegerLiteral(10));

        expressie.getChildren().add(spelerSituatie);

        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add(new Dichtstbijzijnde());
        nabijheidsLocatie.getChildren().add(new DeurAttribuut());

        NaarRichting naarRichting = new NaarRichting();
        naarRichting.getChildren().add(nabijheidsLocatie);

        LoopActie loopActie = new LoopActie();
        loopActie.getChildren().add(naarRichting);

        actie.getChildren().add(loopActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (hp < 10) {\nreturn \"move towards closest door\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerMijnAanvalskrachtNietGelijk30ZijnDanLoopIkNaarDeDichtstbijzijndeTrap_Success() {
        // Build the AST
        SpelerSituatie spelerSituatie = new SpelerSituatie();
        AanvalsKrachtAttribuut laanvalsKrachtAttribuut = new AanvalsKrachtAttribuut();
        NietGelijkAanOperatie nietGelijkAanOperatie = new NietGelijkAanOperatie();
        IntegerLiteral integerLiteral = new IntegerLiteral(30);
        spelerSituatie.getChildren().add(laanvalsKrachtAttribuut);
        spelerSituatie.getChildren().add(nietGelijkAanOperatie);
        spelerSituatie.getChildren().add(integerLiteral);
        expressie.getChildren().add(spelerSituatie);

        LoopActie loopActie = new LoopActie();
        NaarRichting naarRichting = new NaarRichting();
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        Dichtstbijzijnde dichtstbijzijnde = new Dichtstbijzijnde();
        TrapAttribuut trapAttribuut = new TrapAttribuut();
        nabijheidsLocatie.getChildren().add(dichtstbijzijnde);
        nabijheidsLocatie.getChildren().add(trapAttribuut);
        naarRichting.getChildren().add(nabijheidsLocatie);
        loopActie.getChildren().add(naarRichting);
        actie.getChildren().add(loopActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (atk != 30) {\nreturn \"move towards closest stairs\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerMijnMaximaleLevensGelijk30ZijnDanLoopIkNaarDeVersteDeur_Success() {
        // Build the AST

        SpelerSituatie spelerSituatie = new SpelerSituatie();
        MaximaleLevensAttribuut maximaleLevensAttribuut = new MaximaleLevensAttribuut();
        GelijkAanOperatie gelijkAanOperatie = new GelijkAanOperatie();
        IntegerLiteral integerLiteral = new IntegerLiteral(30);
        spelerSituatie.getChildren().add(maximaleLevensAttribuut);
        spelerSituatie.getChildren().add(gelijkAanOperatie);
        spelerSituatie.getChildren().add(integerLiteral);
        expressie.getChildren().add(spelerSituatie);

        LoopActie loopActie = new LoopActie();
        NaarRichting naarRichting = new NaarRichting();
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        Verste verste = new Verste();
        DeurAttribuut deurAttribuut = new DeurAttribuut();
        nabijheidsLocatie.getChildren().add(verste);
        nabijheidsLocatie.getChildren().add(deurAttribuut);
        naarRichting.getChildren().add(nabijheidsLocatie);
        loopActie.getChildren().add(naarRichting);
        actie.getChildren().add(loopActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (maxHp == 30) {\nreturn \"move towards farthest door\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerMijnVerdedigingsKrachtMeerDan30IsDanSlaIkDeSpeler() {
        // Build the AST
        SpelerSituatie spelerSituatie = new SpelerSituatie();
        VerdedigingsKrachtAttribuut verdedigingsKrachtAttribuut = new VerdedigingsKrachtAttribuut();
        MeerDanOperatie meerDanOperatie = new MeerDanOperatie();
        IntegerLiteral integerLiteral = new IntegerLiteral(30);
        spelerSituatie.getChildren().add(verdedigingsKrachtAttribuut);
        spelerSituatie.getChildren().add(meerDanOperatie);
        spelerSituatie.getChildren().add(integerLiteral);
        expressie.getChildren().add(spelerSituatie);

        AanvalsActie aanvalsActie = new AanvalsActie();
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        SpelerAttribuut spelerAttribuut = new SpelerAttribuut();
        nabijheidsLocatie.getChildren().add(spelerAttribuut);
        aanvalsActie.getChildren().add(nabijheidsLocatie);
        actie.getChildren().add(aanvalsActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (def > 30) {\nreturn \"attack player\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerDeKamer3DeurenBevatDanLoopIkWegVanDeDeur() {
        // Build the AST
        LoopActie loopActie = new LoopActie();
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        WegRichting wegRichting = new WegRichting();

        nabijheidsLocatie.getChildren().add(new DeurAttribuut());
        wegRichting.getChildren().add(nabijheidsLocatie);
        loopActie.getChildren().add(wegRichting);
        actie.getChildren().add(loopActie);

        KamerObservatie kamerObservatie = new KamerObservatie();
        kamerObservatie.getChildren().add(new IntegerLiteral(3));
        kamerObservatie.getChildren().add(new DeurAttribuut());

        OmgevingSituatie omgevingSituatie = new OmgevingSituatie();
        omgevingSituatie.getChildren().add(kamerObservatie);

        expressie.getChildren().add(omgevingSituatie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (door == 3) {\nreturn \"move away from door\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerDeKamer1TrapBevatEnWanneerDeKamer2SpelersBevatDanLoopIkWegVanDeDeur() {
        // Building the AST

        KamerObservatie kamerObservatie = new KamerObservatie();
        kamerObservatie.getChildren().add(new IntegerLiteral(1));
        kamerObservatie.getChildren().add(new TrapAttribuut());

        OmgevingSituatie omgevingSituatie = new OmgevingSituatie();
        omgevingSituatie.getChildren().add(kamerObservatie);

        KamerObservatie kamerObservatie2 = new KamerObservatie();
        kamerObservatie2.getChildren().add(new IntegerLiteral(2));
        kamerObservatie2.getChildren().add(new SpelerAttribuut());

        OmgevingSituatie omgevingSituatie2 = new OmgevingSituatie();
        omgevingSituatie2.getChildren().add(kamerObservatie2);

        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add(new DeurAttribuut());

        WegRichting wegRichting = new WegRichting();
        wegRichting.getChildren().add(nabijheidsLocatie);

        LoopActie loopActie = new LoopActie();
        loopActie.getChildren().add(wegRichting);

        expressie.getChildren().add(omgevingSituatie);
        EnOperatie enOperatie = new EnOperatie();
        expressie.getChildren().add(enOperatie);
        expressie.getChildren().add(omgevingSituatie2);

        actie.getChildren().add(loopActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (stair == 1 && player == 2) {\nreturn \"move away from door\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }

    @Test
    void testTransformAST_WanneerDeKamer1TrapBevatOfWanneerDeKamer2SpelersBevatDanInteracteerIkMetDeTrap() {
        // Building the AST

        KamerObservatie kamerObservatie = new KamerObservatie();
        kamerObservatie.getChildren().add(new IntegerLiteral(1));
        kamerObservatie.getChildren().add(new TrapAttribuut());

        OmgevingSituatie omgevingSituatie = new OmgevingSituatie();
        omgevingSituatie.getChildren().add(kamerObservatie);

        KamerObservatie kamerObservatie2 = new KamerObservatie();
        kamerObservatie2.getChildren().add(new IntegerLiteral(2));
        kamerObservatie2.getChildren().add(new SpelerAttribuut());

        OmgevingSituatie omgevingSituatie2 = new OmgevingSituatie();
        omgevingSituatie2.getChildren().add(kamerObservatie2);

        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add(new TrapAttribuut());

        InteracteerActie interacteerActie = new InteracteerActie();
        interacteerActie.getChildren().add(nabijheidsLocatie);

        expressie.getChildren().add(omgevingSituatie);
        OfOperatie ofOperatie = new OfOperatie();
        expressie.getChildren().add(ofOperatie);
        expressie.getChildren().add(omgevingSituatie2);

        actie.getChildren().add(interacteerActie);

        // Transform the AST
        String result = transformer.transformAST(ast, "TestAgent");

        // Assert the output is as expected
        String expectedCode = expectedStartCode + "if (stair == 1 || player == 2) {\nreturn \"interact stairs\";\n}\n" + expectedEndCode;
        assertEquals(expectedCode, result);
    }
}
