package com.han.towersofivory.agent.exceptions;

import java.util.List;

public class AgentCompilerException extends Exception{
    public AgentCompilerException(List<String> errors) {
        super(String.join("\n", errors));
    }
}
