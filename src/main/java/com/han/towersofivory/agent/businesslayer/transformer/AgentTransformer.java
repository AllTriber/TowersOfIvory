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


/**
 * The {@code AgentTransformer} class is responsible for transforming an abstract syntax tree (AST)
 * representing agent behavior into a corresponding Java class.
 */
public class AgentTransformer {

    private final StringBuilder agentClass = new StringBuilder();

    /**
     * Generates the initial part of the agent's Java class code.
     *
     * @param agentName the name of the agent
     */
    private void generateStartCode(String agentName) {
        agentClass.append("""
                import java.lang.reflect.Field;
                import com.han.towersofivory.agent.businesslayer.ast.*;
                import java.util.HashMap;
                public class %s {
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
                """.formatted(agentName));
    }

    /**
     * Generates the final part of the agent's Java class code.
     */
    private void generateEndCode() {
        agentClass.append("""
                } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
                }
                }
                }
                """);
    }

    /**
     * Transforms the provided abstract syntax tree (AST) into Java code representing the agent's behavior.
     *
     * @param ast the abstract syntax tree representing the agent's behavior
     * @param agentName the name of the agent
     * @return the generated Java code as a string
     */
    public String transformAST(AST ast, String agentName) {
        Agent agent = ast.getRoot();
        evaluateAgent((Agent) agent.getChildren().getFirst(), agentName);
        return agentClass.toString();
    }

    /**
     * Evaluates the provided agent and generates corresponding Java code.
     *
     * @param agent the agent node to evaluate
     * @param agentName the name of the agent
     */
    private void evaluateAgent(Agent agent, String agentName) {
        generateStartCode(agentName);
        for (ASTNode node : agent.getChildren()) {
            if (node instanceof Regel regel) {
                evaluateRegel(regel);
            }
        }
        generateEndCode();
    }

    /**
     * Evaluates a {@code Regel} node and generates corresponding Java code.
     *
     * @param rule the {@code Regel} node to evaluate
     */
    private void evaluateRegel(Regel rule) {
        agentClass.append("if (");
        for (ASTNode node : rule.getChildren()) {
            if (node instanceof Expressie expressie) {
                evaluateExpressie(expressie);
            } else if (node instanceof Actie actie) {
                agentClass.append(") {\n");
                evaluateActie(actie);
            }
        }
        agentClass.append("\n}\n");
    }

    /**
     * Evaluates an {@code Expressie} node and generates corresponding Java code.
     *
     * @param expressie the {@code Expressie} node to evaluate
     */
    private void evaluateExpressie(Expressie expressie) {
        for (ASTNode node : expressie.getChildren()) {
            switch (node) {
                case SpelerSituatie spelerSituatie -> evaluateSpelerSituatie(spelerSituatie);
                case OmgevingSituatie omgevingSituatie -> evaluateOmgevingSituatie(omgevingSituatie);
                case VoegWoorden voegWoorden -> evaluateVoegWoorden(voegWoorden);
                default -> throw new IllegalArgumentException("Unknown expressie type: " + node.getClass());
            }
        }
    }

    /**
     * Evaluates a {@code VoegWoorden} node and generates corresponding Java code.
     *
     * @param voegWoorden the {@code VoegWoorden} node to evaluate
     */
    private void evaluateVoegWoorden(VoegWoorden voegWoorden) {
        switch (voegWoorden) {
            case EnOperatie en -> agentClass.append(" && ");
            case OfOperatie of -> agentClass.append(" || ");
            default -> throw new IllegalArgumentException("Unknown voeg woorden type: " + voegWoorden.getClass());
        }
    }

    /**
     * Evaluates an {@code OmgevingSituatie} node and generates corresponding Java code.
     *
     * @param omgevingSituatie the {@code OmgevingSituatie} node to evaluate
     */
    private void evaluateOmgevingSituatie(OmgevingSituatie omgevingSituatie) {
        for (ASTNode node : omgevingSituatie.getChildren()) {
            switch (node) {
                case KamerObservatie kamerObservatie -> evaluateObservatie(kamerObservatie);
                case GangObservatie gangObservatie -> evaluateGangObservatie(gangObservatie);
                default -> throw new IllegalArgumentException("Unknown omgevingssituatie type: " + node.getClass());
            }
        }
    }

    /**
     * Evaluates a {@code GangObservatie} node and generates corresponding Java code.
     *
     * @param corridorObservation the {@code GangObservatie} node to evaluate
     */
    private void evaluateGangObservatie(GangObservatie corridorObservation) {
        if (corridorObservation.getChildren().isEmpty()) {
            agentClass.append("!inRoom");
        } else {
            evaluateObservatie(corridorObservation);
        }
    }

    /**
     * Evaluates an {@code Expressie} node representing a room observation and generates corresponding Java code.
     *
     * @param roomObservation the {@code Expressie} node to evaluate
     */
    private void evaluateObservatie(Expressie roomObservation) {
        for (ASTNode node : roomObservation.getChildren()) {
            switch (node) {
                case DeurAttribuut deurAttribuut -> agentClass.append("door");
                case SpelerAttribuut spelerAttribuut -> agentClass.append("player");
                case TrapAttribuut trapAttribuut -> agentClass.append("stair");
                default -> {}
            }
        }
        if (roomObservation.getChildren().size() == 2) {
            agentClass.append(" == ");
        } else if (roomObservation.getChildren().size() == 3) {
            for (ASTNode node : roomObservation.getChildren()) {
                if (node instanceof Operatie operatie) {
                    evaluateOperatie(operatie);
                }
            }
        }
        for (ASTNode node : roomObservation.getChildren()) {
            if (node instanceof IntegerLiteral integerLiteral) {
                evaluateIntegerLiteral(integerLiteral);
            }
        }
    }

    /**
     * Evaluates a {@code SpelerSituatie} node and generates corresponding Java code.
     *
     * @param spelerSituatie the {@code SpelerSituatie} node to evaluate
     */
    private void evaluateSpelerSituatie(SpelerSituatie spelerSituatie) {
        for (ASTNode node : spelerSituatie.getChildren()) {
            switch (node) {
                case Attribuut attribuut -> evaluateSpelerAttribuut(attribuut);
                case Operatie operatie -> evaluateOperatie(operatie);
                case IntegerLiteral integerLiteral -> evaluateIntegerLiteral(integerLiteral);
                default -> throw new IllegalArgumentException("Unknown speler situatie type: " + node.getClass());
            }
        }
    }

    /**
     * Evaluates an {@code IntegerLiteral} node and generates corresponding Java code.
     *
     * @param integerLiteral the {@code IntegerLiteral} node to evaluate
     */
    private void evaluateIntegerLiteral(IntegerLiteral integerLiteral) {
        agentClass.append(integerLiteral.getValue());
    }

    /**
     * Evaluates an {@code Operatie} node and generates corresponding Java code.
     *
     * @param operatie the {@code Operatie} node to evaluate
     */
    private void evaluateOperatie(Operatie operatie) {
        switch (operatie) {
            case MeerDanOperatie me -> agentClass.append(" > ");
            case MinderDanOperatie mi -> agentClass.append(" < ");
            case GelijkAanOperatie g -> agentClass.append(" == ");
            case NietGelijkAanOperatie n -> agentClass.append( " != ");
            default -> throw new IllegalArgumentException("Unknown operatie type: " + operatie.getClass());
        }
    }

    /**
     * Evaluates an {@code Attribuut} node representing a player attribute and generates corresponding Java code.
     *
     * @param playerAttribute the {@code Attribuut} node to evaluate
     */
    private void evaluateSpelerAttribuut(Attribuut playerAttribute) {
        switch (playerAttribute) {
            case AanvalsKrachtAttribuut a -> agentClass.append("atk");
            case LevenspuntenAttribuut l -> agentClass.append("hp");
            case MaximaleLevensAttribuut m -> agentClass.append("maxHp");
            case VerdedigingsKrachtAttribuut v -> agentClass.append("def");
            default -> throw new IllegalArgumentException("Unknown attribute type: " + playerAttribute.getClass());
        }
    }

    /**
     * Evaluates an {@code Actie} node and generates corresponding Java code.
     *
     * @param actie the {@code Actie} node to evaluate
     */
    private void evaluateActie(Actie actie) {
        agentClass.append("return \"");
        for (ASTNode node : actie.getChildren()) {
            switch (node) {
                case InteracteerActie interacteerActie -> evaluateInteracteerActie(interacteerActie);
                case LoopActie loopActie -> evaluateLoopActie(loopActie);
                case AanvalsActie aanvalsActie -> evaluateAanvalsActie(aanvalsActie);
                default -> throw new IllegalArgumentException("Unknown actie type: " + node.getClass());
            }
        }
        agentClass.append("\";");
    }

    /**
     * Evaluates an {@code AanvalsActie} node and generates corresponding Java code.
     *
     * @param actie the {@code AanvalsActie} node to evaluate
     */
    private void evaluateAanvalsActie(AanvalsActie actie) {
        agentClass.append("attack ");
        for (ASTNode attack : actie.getChildren()) {
            switch (attack) {
                case NabijheidsLocatie nabijheidsLocatie -> evaluateNabijheidsLocatie(nabijheidsLocatie);
                case Attribuut attribuut -> evaluateOmgevingsAttribuut(attribuut);
                default -> throw new IllegalArgumentException("Unknown attack type: " + attack.getClass());
            }
        }
    }

    /**
     * Evaluates a {@code NabijheidsLocatie} node and generates corresponding Java code.
     *
     * @param nabijheidsLocatie the {@code NabijheidsLocatie} node to evaluate
     */
    private void evaluateNabijheidsLocatie(NabijheidsLocatie nabijheidsLocatie) {
        for (ASTNode proximityLocation : nabijheidsLocatie.getChildren()) {
            switch (proximityLocation) {
                case Nabijheid nabijheid -> evaluateNabijheid(nabijheid);
                case Attribuut attribuut -> evaluateOmgevingsAttribuut(attribuut);
                default -> throw new IllegalArgumentException("Unknown nabijheidslocatie type: " + proximityLocation.getClass());
            }
        }
    }

    /**
     * Evaluates a {@code Nabijheid} node and generates corresponding Java code.
     *
     * @param nabijheid the {@code Nabijheid} node to evaluate
     */
    private void evaluateNabijheid(Nabijheid nabijheid) {
        switch (nabijheid) {
            case Dichtstbijzijnde d -> agentClass.append("closest ");
            case Verste v -> agentClass.append("farthest ");
            default -> throw new IllegalArgumentException("Unknown nabijheid type: " + nabijheid.getClass());
        }
    }

    /**
     * Evaluates a {@code LoopActie} node and generates corresponding Java code.
     *
     * @param actie the {@code LoopActie} node to evaluate
     */
    private void evaluateLoopActie(LoopActie actie) {
        agentClass.append("move ");
        for (ASTNode direction : actie.getChildren()) {
            switch (direction) {
                case GangRichting gangRichting -> evaluateGangRichting();
                case NaarRichting naarRichting -> evaluateNaarRichting(naarRichting);
                case WegRichting wegRichting -> evaluateWegRichting(wegRichting);
                default -> throw new IllegalArgumentException("Unknown loop actie type: " + direction.getClass());
            }
        }
    }

    /**
     * Evaluates a {@code WegRichting} node and generates corresponding Java code.
     *
     * @param wegRichting the {@code WegRichting} node to evaluate
     */
    private void evaluateWegRichting(WegRichting wegRichting) {
        agentClass.append("away from ");
        for (ASTNode interaction : wegRichting.getChildren()) {
            switch (interaction) {
                case NabijheidsLocatie nabijheidsLocatie -> evaluateNabijheidsLocatie(nabijheidsLocatie);
                case Attribuut attribuut -> evaluateOmgevingsAttribuut(attribuut);
                default -> throw new IllegalArgumentException("Unknown weg richting interactie type: " + interaction.getClass());
            }
        }
    }

    /**
     * Evaluates a {@code NaarRichting} node and generates corresponding Java code.
     *
     * @param towardsDirection the {@code NaarRichting} node to evaluate
     */
    private void evaluateNaarRichting(NaarRichting towardsDirection) {
        agentClass.append("towards ");
        for (ASTNode interaction : towardsDirection.getChildren()) {
            switch (interaction) {
                case NabijheidsLocatie nabijheidsLocatie -> evaluateNabijheidsLocatie(nabijheidsLocatie);
                case Attribuut attribuut -> evaluateOmgevingsAttribuut(attribuut);
                default -> throw new IllegalArgumentException("Unknown interactie type: " + interaction.getClass());
            }
        }
    }

    /**
     * Appends the string "through corridor" to the agentClass.
     */
    private void evaluateGangRichting() {
        agentClass.append("through corridor");
    }

    /**
     * Evaluates an {@code InteracteerActie} node and generates corresponding Java code.
     *
     * @param action the {@code InteracteerActie} node to evaluate
     */
    private void evaluateInteracteerActie(InteracteerActie action) {
        agentClass.append("interact ");
        for (ASTNode interaction : action.getChildren()) {
            switch (interaction) {
                case NabijheidsLocatie nabijheidsLocatie -> evaluateNabijheidsLocatie(nabijheidsLocatie);
                case Attribuut attribuut -> evaluateOmgevingsAttribuut(attribuut);
                default -> throw new IllegalArgumentException("Unknown interactie type: " + interaction.getClass());
            }
        }
    }

    /**
     * Evaluates an {@code Attribuut} node representing a surroundings attribute and generates corresponding Java code.
     *
     * @param omgevingssituatie the {@code Attribuut} node to evaluate
     */
    private void evaluateOmgevingsAttribuut(Attribuut omgevingssituatie) {
        switch (omgevingssituatie) {
            case DeurAttribuut deurAttribuut -> agentClass.append("door");
            case SpelerAttribuut spelerAttribuut -> agentClass.append("player");
            case TrapAttribuut trapAttribuut -> agentClass.append("stairs");
            default -> throw new IllegalArgumentException("Unknown omgevingssituatie attribute type: " + omgevingssituatie.getClass());
        }
    }
}