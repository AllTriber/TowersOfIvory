package com.han.towersofivory.agent.businesslayer.ast;

import com.han.towersofivory.agent.businesslayer.checker.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {

    private SemanticError error = null;

    /*
     Different AST nodes use different attributes to store their children.
     This method provides a unified interface.
     */
    public List<ASTNode> getChildren() {
        return new ArrayList<>();
    }

    /*
    By implementing this method in a subclass you can easily create AST nodes
      incrementally.
    */
    @SuppressWarnings("unused")
    public ASTNode addChild(ASTNode child) {
        return this;
    }

    /*
     * By implementing this method you can easily make transformations that prune the AST.
     */
    @SuppressWarnings("unused")
    public ASTNode removeChild(ASTNode child) {
        return this;
    }

    public SemanticError getError() {
        return this.error;
    }

    public void setError(String description) {
        this.error = new SemanticError(description);
    }

    public boolean hasError() {
        return error != null;
    }

    /*
     This method is used in the GUI to create an appropriate label
    in the tree visualisation.
  */
    public String getNodeLabel() {
        return "ASTNode";
    }

    private void toString(StringBuilder builder) {
        builder.append("[");
        builder.append("|");
        for (ASTNode child : getChildren()) {
            child.toString(builder);
        }
        builder.append("]");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ASTNode)) return false;
        //Compare all children
        List<ASTNode> thisChildren = this.getChildren();
        List<ASTNode> otherChildren = ((ASTNode) o).getChildren();
        if (otherChildren.size() != thisChildren.size()) return false;
        for (int i = 0; i < thisChildren.size(); i++) {
            if (!thisChildren.get(i).equals(otherChildren.get(i))) {
                return false;
            }
        }
        return true;
    }
}
