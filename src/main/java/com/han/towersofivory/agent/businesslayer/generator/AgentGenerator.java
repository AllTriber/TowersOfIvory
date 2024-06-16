package com.han.towersofivory.agent.businesslayer.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@SuppressWarnings("unused")
public class AgentGenerator {

    private static final Logger LOGGER = LogManager.getLogger(AgentGenerator.class);
    private String outputDirectory = "src/main/resources/agentJavaByteCode";

    public AgentGenerator() {
    }

    /**
     * UC6: Configureren agent <br>
     * This is a constructor to test this class.
     *
     * @param outputDir The output directory for the generated class file.
     *
     * @return void
     */
    AgentGenerator(String outputDir) {
        outputDirectory = outputDir;
    }

    /**
     * UC6: Configureren agent <br>
     * This method generates the byte code for the agent.
     *
     * @param javaCode The Java code in String to be compiled.
     *
     *@return void
     */
    public void generateByteCode(String javaCode, String className) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileObject file = new SimpleJavaFileObject(
                URI.create("string:///" + className + ".java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return javaCode;
            }
        };

        Iterable<? extends JavaFileObject> compilationUnits = List.of(file);
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        try {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(new File(outputDirectory)));
        } catch (IOException e) {
            LOGGER.error("An error occurred while setting the location of the class file. Error: {}", e.getMessage());
        }

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean success = task.call();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            LOGGER.error(diagnostic);
        }
    }
}
