package com.han.towersofivory.agent.businesslayer.services;

import com.han.towersofivory.agent.businesslayer.ast.AST;
import com.han.towersofivory.agent.businesslayer.checker.AgentChecker;
import com.han.towersofivory.agent.businesslayer.generator.AgentGenerator;
import com.han.towersofivory.agent.businesslayer.listeners.AgentGrammarLexer;
import com.han.towersofivory.agent.businesslayer.listeners.AgentGrammarParser;
import com.han.towersofivory.agent.businesslayer.parser.AgentParser;
import com.han.towersofivory.agent.businesslayer.transformer.AgentTransformer;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.exceptions.AgentCompilerException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class AgentService implements ANTLRErrorListener {
    private List<String> errors;

    private static final Logger LOGGER = LogManager.getLogger(AgentService.class);
    private final String configurationsPath;
    private static final String NOT_IMPLEMENTED = "Not implemented yet";
    AgentChecker agentChecker;
    AgentTransformer agentTransformer;
    AgentGenerator agentGenerator;

    public AgentService(String configurationsPath) {
        errors = new ArrayList<>();
        this.configurationsPath = configurationsPath;
        this.agentChecker = new AgentChecker();
        this.agentGenerator = new AgentGenerator();
    }

    /**
     * UC3: Configureren agent
     * This method saves the configuration file to the configuration's folder.
     */
    public void saveConfiguration(AgentConfigurationText agentConfigurationText) {
        try {
            String newFileName = agentConfigurationText.getTitle() + ".txt";
            String filePath = configurationsPath + newFileName;

            Files.write(Paths.get(filePath), agentConfigurationText.getConfiguration().getBytes());
        } catch (IOException e) {
            LOGGER.error("An error occurred while saving configuration file. Error: {}", e.getMessage());
        }
    }

    public void generateFile(AgentConfigurationText agentConfigurationText) throws AgentCompilerException {
        AST ast = parseAST(agentConfigurationText);
        checkAST(ast);
        if (!errors.isEmpty()) {
            throw new AgentCompilerException(errors);
        }
        agentGenerator.generateByteCode(transformAST(ast, agentConfigurationText), agentConfigurationText.getTitle());
    }

    /**
     * Check the agent configuration text for errors
     *
     * @param agentConfigurationText the agent configuration text to check
     * @UseCase: UC3: Configureren agent
     * @Task: Error handling voor Grammatica <a href="https://jira.aimsites.nl/browse/ASDS2G2-503">Jira Issue</a>
     * @author Pepijn van den Ende
     */
    public void checkConfiguration(AgentConfigurationText agentConfigurationText) throws AgentCompilerException {
        AST ast = parseAST(agentConfigurationText);
        checkAST(ast);
        if (!errors.isEmpty()) {
            throw new AgentCompilerException(errors);
        }
    }

    /**
     * Parse the agent configuration text to an AST
     *
     * @param agentConfigurationText the agent configuration text to parse
     * @return AST
     * @UseCase: UC3: Configureren agent
     * @Task: Error handling voor Grammatica <a href="https://jira.aimsites.nl/browse/ASDS2G2-503">Jira Issue</a>
     * @author Pepijn van den Ende
     */
    private AST parseAST(AgentConfigurationText agentConfigurationText) {
        CharStream inputStream = CharStreams.fromString(agentConfigurationText.getConfiguration());
        AgentGrammarLexer lexer = new AgentGrammarLexer(inputStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(this);
        errors.clear();
        try {
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            AgentGrammarParser parser = new AgentGrammarParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(this);

            ParseTree parseTree = parser.agent();

            AgentParser listener = new AgentParser();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, parseTree);

            return listener.getAST();

        } catch (RecognitionException | ParseCancellationException e) {
            return new AST();
        }
    }

    /**
     * Check the AST for errors
     *
     * @param ast the AST to check
     */
    private void checkAST(AST ast) {
        agentChecker.check(ast);
        if (!ast.getErrors().isEmpty()) {
            errors.add(ast.getErrors().toString());
        }
    }


    private String transformAST(AST ast, AgentConfigurationText agentConfigurationText) {
        return agentTransformer.transformAST(ast, agentConfigurationText.getTitle());
    }

    @SuppressWarnings("unused")
    public String callAgent(String agentName, Character character) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * UC3: Configureren agent
     * This method loads all configuration files from the configurations folder.
     *
     * @return File[] - An array of files
     */
    public File[] loadAllConfigurations() {
        File directory = new File(configurationsPath);

        // Retrieve a list of files in the folder
        File[] files = directory.listFiles();

        // Ensure files array is not null
        if (files == null) {
            LOGGER.error("An error occurred while loading configurations. Error: No files found in the directory.");
            return new File[0];
        }
        return files;
    }

    public AgentConfigurationText getAgentConfiguration(String configName) {
        File directory = new File(configurationsPath);

        // Check if the directory exists and is indeed a directory
        if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }

        // Define the path to the specific configuration file
        File configFile = new File(directory, configName);

        // Check if the configuration file exists
        if (configFile.exists() && configFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                // Create and return AgentConfigurationText object with file contents as string
                return new AgentConfigurationText(configName, stringBuilder.toString());
            } catch (IOException e) {
                // Handle IOException
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * UC3: Configureren agent
     * This method deletes the configuration file with the given name.
     *
     */
    public void deleteConfiguration(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(configurationsPath + fileName));
        } catch (IOException e) {
            LOGGER.error("An error occurred while deleting the file. Error: {}", e.getMessage());
        }
    }

    //Catch ANTLR errors
    @Override
    public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3,
                                boolean arg4, BitSet arg5, ATNConfigSet arg6) {
        //Report Ambiguity
    }

    @Override
    public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2,
                                            int arg3, BitSet arg4, ATNConfigSet arg5) {
        //Report Attempting Full Context
    }

    @Override
    public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2,
                                         int arg3, int arg4, ATNConfigSet arg5) {
        //Report Context Sensitivity
    }

    @Override
    public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2,
                            int arg3, String arg4, RecognitionException arg5) {
        errors.add("Er is iets mis met de grammatica van de agent.\nError: " + arg4);
    }
}
