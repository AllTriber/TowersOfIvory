package com.han.towersofivory.agent.businesslayer.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AgentGeneratorTest {

    private AgentGenerator agentGenerator;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        agentGenerator = new AgentGenerator(tempDir.toString());
    }

    @Test
    void testGenerateByteCode_successfulCompilation() {
        String className = "TestClass";
        String validJavaCode = "public class " + className + " { public static void main(String[] args) { System.out.println(\"Hello, World\"); } }";

        agentGenerator.generateByteCode(validJavaCode, className);

        File outputFile = tempDir.resolve(className + ".class").toFile();
        assertTrue(outputFile.exists(), "The class file should be generated.");
    }
}