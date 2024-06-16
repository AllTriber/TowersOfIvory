package com.han.towersofivory.agent.businesslayer.checker;

import com.han.towersofivory.agent.businesslayer.ast.*;
import com.han.towersofivory.agent.businesslayer.ast.acties.AanvalsActie;
import com.han.towersofivory.agent.businesslayer.ast.acties.InteracteerActie;
import com.han.towersofivory.agent.businesslayer.ast.literal.IntegerLiteral;
import com.han.towersofivory.agent.businesslayer.ast.observatie.GangObservatie;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.DeurAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.SpelerAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.omgevingsattribuut.TrapAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.AanvalsKrachtAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.LevenspuntenAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.MaximaleLevensAttribuut;
import com.han.towersofivory.agent.businesslayer.ast.spelerattributen.VerdedigingsKrachtAttribuut;

import java.util.*;
import java.util.function.Consumer;


/**
 * This class is responsible for checking the AST for logical errors.
 *
 * @UseCase: UC3: Configureren agent, UC6: Inschakelen Agent
 * @Task: checker <a href="https://jira.aimsites.nl/browse/ASDS2G2-428">Jira Issue</a>
 * @Author: Ivar de Vries, Silke Bertisen, Pepijn van den Ende en Nigel de Graaff
 */
public class AgentChecker {

    /**
     * Maps attribute types derived from {@link Attribuut} to their maximum allowable values.
     * This map is used by {@link AgentChecker} to validate attribute values against these predefined limits.
     */
    private static final Map<Class<? extends Attribuut>, Integer> MAX_VALUES = Map.of(
            LevenspuntenAttribuut.class, 500,
            AanvalsKrachtAttribuut.class, 40,
            VerdedigingsKrachtAttribuut.class, 10,
            MaximaleLevensAttribuut.class, 500
    );

    /**
     * A map of handlers for different node types.
     * The handlers are used to check the AST for specific conditions based on the node type.
     */
    private final Map<Class<?>, Consumer<ASTNode>> handlers;

    public AgentChecker() {
        handlers = Map.of(
                SpelerSituatie.class, this::checkSpelerAttributen,
                InteracteerActie.class, node -> checkNodeForInvalidAttributen(node, Set.of(DeurAttribuut.class, SpelerAttribuut.class), "Er kan niet met een %s geïnteracteerd worden."),
                AanvalsActie.class, node -> checkNodeForInvalidAttributen(node, Set.of(DeurAttribuut.class, TrapAttribuut.class), "Een %s kan niet aangevallen worden"),
                GangObservatie.class, this::checkGangObservatie
        );
    }

    /**
     * Processes an Abstract Syntax Tree (AST) by applying specific validation rules to each node
     * based on its type. This method systematically traverses the AST starting from the root node,
     * applying the appropriate validation or check as defined in the {@code handlers} map for each node type.
     *
     * @param ast the AST to be checked; must not be null.
     * @Author: Nigel de Graaff
     */
    public void check(AST ast) {
        Deque<ASTNode> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(ast.getRoot());

        while (!nodeQueue.isEmpty()) {
            ASTNode currentNode = nodeQueue.poll();
            handlers.getOrDefault(currentNode.getClass(), n -> {
            }).accept(currentNode);
            nodeQueue.addAll(currentNode.getChildren());
        }
    }

    /**
     * Checks a given AST node for the presence of any invalid attributes as specified by the types in the invalidTypes set.
     * If an invalid attribute is found, an error message is set on the parent node with details about the invalid attribute.
     *
     * @param parent             the AST node to check for invalid attributes
     * @param invalidTypes       a set of classes representing the types of attributes considered invalid
     * @param errorMessageFormat a format string for the error message; it should include a placeholder (%s) for the attribute's node label
     * @example  <pre>
     * {@code
     * checkNodeForInvalidAttributen(currentNode, Set.of(DeurAttribuut.class, SpelerAttribuut.class), "Er kan niet met een %s geïnteracteerd worden.");
     * }
     * </pre>
     * @Author: Nigel de Graaff
     */
    private void checkNodeForInvalidAttributen(ASTNode parent, Set<Class<?>> invalidTypes, String errorMessageFormat) {
        Optional<ASTNode> optionalInvalidAttribuut = parent.getChildren().stream()
                .filter(NabijheidsLocatie.class::isInstance)
                .flatMap(nabijheidNode -> nabijheidNode.getChildren().stream())
                .filter(attribuutNode -> invalidTypes.stream().anyMatch(type -> type.isInstance(attribuutNode)))
                .findFirst();

        if (optionalInvalidAttribuut.isPresent()) {
            ASTNode foundNode = optionalInvalidAttribuut.get();
            parent.setError(String.format(errorMessageFormat, foundNode.getNodeLabel()));
        }
    }

    /**
     * Checks the "GangObservatie" node for specific conditions:
     * 1. Checks if there is an interaction with a stair - stairs are not allowed in a corridor.
     * 2. Checks if there is a corridor with a single door - corridors must have at least two doors if there is one door.
     *
     * @param parent the node to check
     * @Author: Nigel de Graaff
     */
    private void checkGangObservatie(ASTNode parent) {
        checkNodeForSpecificCondition(parent, TrapAttribuut.class, "Een gang kan geen trap bevatten.");
        checkCorridorDoorConditions(parent);
    }

    /**
     * Checks a specific condition on a given AST node related to invalid attributes. This method can be reused
     * for any node type to check against any single attribute considered invalid.
     *
     * @param parent       the AST node to check for invalid attributes.
     * @param invalidType  the Class type representing the invalid attribute to check for.
     * @param errorMessage the error message to set if the invalid attribute is found.
     * @example To check for a "TrapAttribuut" in a "GangObservatie" node:
     * <pre>
     * {@code
     * checkNodeForSpecificCondition(currentNode, TrapAttribuut.class, "Een gang kan geen trap bevatten.");
     * }
     * </pre>
     * @Author: Nigel de Graaff
     */
    private void checkNodeForSpecificCondition(ASTNode parent, Class<?> invalidType, String errorMessage) {
        boolean foundInvalid = parent.getChildren().stream()
                .anyMatch(invalidType::isInstance);

        if (foundInvalid) {
            parent.setError(errorMessage);
        }
    }

    /**
     * Checks the conditions related to doors within a corridor node. Specifically, it ensures that if there is a door,
     * the number of doors is at least two. This method retrieves the expected number of doors from an integer literal
     * associated with the corridor node and compares it to the actual number of doors present.
     *
     * @param parent the corridor node to check for door conditions.
     * @Author: Nigel de Graaff
     */
    private void checkCorridorDoorConditions(ASTNode parent) {
        List<ASTNode> children = parent.getChildren();
        boolean hasDoor = children.stream().anyMatch(DeurAttribuut.class::isInstance);

        getIntegerValueFromParent(parent)
                .ifPresent(
                        value -> {
                            if (hasDoor && value < 2) {
                                parent.setError("Een gang heeft ten minste twee deuren.");
                            }
                        }
                );
    }


    /**
     * Checks the attributes of a player ('Speler') node. It retrieves an integer value associated with the node
     * and validates each attribute child node against this value. Only attributes that are instances of {@link Attribuut}
     * are considered for validation.
     *
     * @param node the player node whose attributes are to be validated.
     * @see #validateAttribute(ASTNode, int) for details on how individual attributes are validated.
     * @Author: Nigel de Graaff
     */
    private void checkSpelerAttributen(ASTNode node) {
        getIntegerValueFromParent(node)
                .ifPresent(
                        value -> node.getChildren().stream()
                                .filter(Attribuut.class::isInstance)
                                .forEach(child -> validateAttribute(child, value))
                );
    }

    /**
     * Get the integer value from the parent node.
     *
     * @param node the node to get the value from
     * @return the integer value
     * @Author: Nigel de Graaff
     */
    private Optional<Integer> getIntegerValueFromParent(ASTNode node) {
        return node.getChildren().stream()
                .filter(IntegerLiteral.class::isInstance)
                .map(IntegerLiteral.class::cast)
                .findFirst()
                .map(IntegerLiteral::getValue);
    }


    /**
     * Validates an attribute of a node against predefined maximum values. It sets an error message
     * on the node if the attribute value exceeds the maximum allowed or is less than or equal to zero.
     *
     * @param node  the node representing an attribute to be validated; must be an instance of {@link Attribuut}.
     * @param value the actual value of the attribute to be checked.
     * @Author: Nigel de Graaff
     */
    private void validateAttribute(ASTNode node, int value) {
        Attribuut attribuut = (Attribuut) node;
        Integer maxValue = MAX_VALUES.get(attribuut.getClass());
        if (value > maxValue) {
            attribuut.setError(String.format("%s mag niet meer dan %d zijn", attribuut.getNodeLabel(), maxValue));
        } else if (value <= 0) {
            attribuut.setError(String.format("%s mag niet minder of gelijk aan 0 zijn", attribuut.getNodeLabel()));
        }
    }
}
