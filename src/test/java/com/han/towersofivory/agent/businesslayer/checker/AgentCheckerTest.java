package com.han.towersofivory.agent.businesslayer.checker;

import com.han.towersofivory.agent.businesslayer.ast.*;
import com.han.towersofivory.agent.businesslayer.ast.acties.AanvalsActie;
import com.han.towersofivory.agent.businesslayer.ast.acties.InteracteerActie;
import com.han.towersofivory.agent.businesslayer.ast.acties.LoopActie;
import com.han.towersofivory.agent.businesslayer.ast.literal.IntegerLiteral;
import com.han.towersofivory.agent.businesslayer.ast.looprichtingen.GangRichting;
import com.han.towersofivory.agent.businesslayer.ast.looprichtingen.NaarRichting;
import com.han.towersofivory.agent.businesslayer.ast.nabijheid.Dichtstbijzijnde;
import com.han.towersofivory.agent.businesslayer.ast.observatie.GangObservatie;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.DeurAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.SpelerAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.TrapAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.operations.GelijkAanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.operations.MeerDanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.operations.MinderDanOperatie;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.AanvalsKrachtAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.LevenspuntenAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.MaximaleLevensAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.VerdedigingsKrachtAttribuut;
import com.han.towersofivory.agent.businesslayer.services.AgentService;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AgentCheckerTest {

    private AgentChecker sut;
    private AST ast;
    private Expressie expressie;
    private Actie actie;

    @BeforeEach
    void setUp() {
        sut = new AgentChecker();
        createPartialASTTreeWithActieAndExpressie();
    }

    private void createPartialASTTreeWithActieAndExpressie() {
        ast = new AST();
        Agent rootAgent = new Agent();
        Agent nestedAgent = new Agent();
        Regel regel = new Regel();
        expressie = new Expressie();
        actie = new Actie();
        regel.getChildren().add(expressie);
        regel.getChildren().add(actie);
        nestedAgent.getChildren().add(regel);
        rootAgent.getChildren().add(nestedAgent);
        ast.setRoot(rootAgent);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testSpelersAttribuutRules(SpelerSituatie spelerSituatie, NabijheidsLocatie nabijheidsLocatie, String expectedError) {
        // Arrange
        expressie.getChildren().add(spelerSituatie);

        NaarRichting naarRichting = new NaarRichting();
        naarRichting.getChildren().add(nabijheidsLocatie);

        LoopActie loopActie = new LoopActie();
        loopActie.getChildren().add(naarRichting);

        actie.getChildren().add(loopActie);

        // Act

        sut.check(ast);

        // Assert
        if (expectedError == null) {
            assertEquals(0, ast.getErrors().size());
        } else {
            String actualError = ast.getErrors().getFirst().toString();
            assertEquals(expectedError, actualError);
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(buildSpelerSituatie(new LevenspuntenAttribuut(), new MinderDanOperatie(), 510), buildNabijheidsLocatieNaarDeur(), "Checker error: Levenspunten mag niet meer dan 500 zijn"),
                Arguments.of(buildSpelerSituatie(new LevenspuntenAttribuut(), new MeerDanOperatie(), 500), buildNabijheidsLocatieNaarGang(), null),
                Arguments.of(buildSpelerSituatie(new LevenspuntenAttribuut(), new MinderDanOperatie(), 0), buildNabijheidsLocatieNaarGang(), "Checker error: Levenspunten mag niet minder of gelijk aan 0 zijn"),
                Arguments.of(buildSpelerSituatie(new LevenspuntenAttribuut(), new MinderDanOperatie(), 3), buildNabijheidsLocatieNaarGang(), null),
                Arguments.of(buildSpelerSituatie(new VerdedigingsKrachtAttribuut(), new MeerDanOperatie(), 300), buildNabijheidsLocatieNaarDeur(), "Checker error: Verdedigingskracht mag niet meer dan 10 zijn"),
                Arguments.of(buildSpelerSituatie(new MaximaleLevensAttribuut(), new MeerDanOperatie(), 1000), buildNabijheidsLocatieNaarDeur(), "Checker error: Maximale levens mag niet meer dan 500 zijn"),
                Arguments.of(buildSpelerSituatie(new AanvalsKrachtAttribuut(), new GelijkAanOperatie(), 41), buildNabijheidsLocatieNaarDeur(), "Checker error: Aanvalskracht mag niet meer dan 40 zijn")
        );
    }

    private static SpelerSituatie buildSpelerSituatie(Object attribuut, Object operaties, int value) {
        SpelerSituatie spelerSituatie = new SpelerSituatie();
        spelerSituatie.getChildren().add((ASTNode) attribuut);
        spelerSituatie.getChildren().add((ASTNode) operaties);
        spelerSituatie.getChildren().add(new IntegerLiteral(value));
        return spelerSituatie;
    }

    private static NabijheidsLocatie buildNabijheidsLocatieNaarDeur() {
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add(new Dichtstbijzijnde());
        nabijheidsLocatie.getChildren().add(new DeurAttribuut());
        return nabijheidsLocatie;
    }

    private static NabijheidsLocatie buildNabijheidsLocatieNaarGang() {
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add(new GangRichting());
        return nabijheidsLocatie;
    }

    @ParameterizedTest
    @MethodSource("provideGangObservatieTestCases")
    void testCheckGangObservatie(OmgevingSituatie omgevingSituatie, String expectedError) {
        // Arrange
        expressie.getChildren().add(omgevingSituatie);

        // Act
        sut.check(ast);

        // Assert
        if (expectedError == null) {
            assertEquals(0, ast.getErrors().size());
        } else {
            String actualError = ast.getErrors().getFirst().toString();
            assertEquals(expectedError, actualError);
        }
    }

    private static Stream<Arguments> provideGangObservatieTestCases() {
        return Stream.of(
                Arguments.of(buildOmgevingSituatie(new TrapAttribuut(), 1), "Checker error: Een gang kan geen trap bevatten."),
                Arguments.of(buildOmgevingSituatie(new DeurAttribuut(), 1), "Checker error: Een gang heeft ten minste twee deuren."),
                Arguments.of(buildOmgevingSituatie(new DeurAttribuut(), 2), null)
        );
    }

    private static OmgevingSituatie buildOmgevingSituatie(Object attribuut, int value) {
        GangObservatie gangObservatie = new GangObservatie();
        gangObservatie.getChildren().add(new IntegerLiteral(value));
        gangObservatie.getChildren().add((ASTNode) attribuut);

        OmgevingSituatie omgevingSituatie = new OmgevingSituatie();
        omgevingSituatie.getChildren().add(gangObservatie);
        return omgevingSituatie;
    }

    @ParameterizedTest
    @MethodSource("provideInteracteerActieTestCases")
    void testCheckInteracteerActie(InteracteerActie spelerActie, String expectedError) {
        // Arrange
        expressie.getChildren().add(buildOmgevingSituatie(new DeurAttribuut(), 2));
        actie.getChildren().add(spelerActie);

        // Act
        sut.check(ast);

        // Assert
        if (expectedError == null) {
            assertEquals(0, ast.getErrors().size());
        } else {
            String actualError = ast.getErrors().getFirst().toString();
            assertEquals(expectedError, actualError);
        }
    }

    private static Stream<Arguments> provideInteracteerActieTestCases() {
        return Stream.of(
                Arguments.of(buildInteracteerActie(new DeurAttribuut()), "Checker error: Er kan niet met een deur geïnteracteerd worden."),
                Arguments.of(buildInteracteerActie(new SpelerAttribuut()), "Checker error: Er kan niet met een speler geïnteracteerd worden.")
        );
    }

    private static InteracteerActie buildInteracteerActie(Object attribuut) {
        InteracteerActie interacteerActie = new InteracteerActie();
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add((ASTNode) attribuut);
        interacteerActie.getChildren().add(nabijheidsLocatie);
        return interacteerActie;
    }

    @ParameterizedTest
    @MethodSource("provideAanvalsActieTestCases")
    void testCheckAanvalsActie(AanvalsActie spelerActie, String expectedError) {
        // Arrange
        expressie.getChildren().add(buildOmgevingSituatie(new DeurAttribuut(), 2));
        actie.getChildren().add(spelerActie);

        // Act
        sut.check(ast);

        // Assert
        if (expectedError == null) {
            assertEquals(0, ast.getErrors().size());
        } else {
            String actualError = ast.getErrors().getFirst().toString();
            assertEquals(expectedError, actualError);
        }
    }

    private static Stream<Arguments> provideAanvalsActieTestCases() {
        return Stream.of(
                Arguments.of(buildAanvalsActie(new DeurAttribuut()), "Checker error: Een deur kan niet aangevallen worden"),
                Arguments.of(buildAanvalsActie(new TrapAttribuut()), "Checker error: Een trap kan niet aangevallen worden"),
                Arguments.of(buildAanvalsActie(new SpelerAttribuut()), null)
        );
    }

    private static AanvalsActie buildAanvalsActie(Object attribuut) {
        AanvalsActie aanvalsActie = new AanvalsActie();
        NabijheidsLocatie nabijheidsLocatie = new NabijheidsLocatie();
        nabijheidsLocatie.getChildren().add((ASTNode) attribuut);
        aanvalsActie.getChildren().add(nabijheidsLocatie);
        return aanvalsActie;
    }

 }