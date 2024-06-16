package com.han.towersofivory.agent.businesslayer;

import com.han.towersofivory.agent.businesslayer.generator.AgentGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentLoaderTest {

    @Test
    void testFindExistingClass() throws ClassNotFoundException {
        // Arrange
        AgentGenerator agentGenerator = new AgentGenerator();
        String className = "outputs";
        String validJavaCode = "public class " + className + " { public static void main(String[] args) { System.out.println(\"Hello, World\"); } }";
        agentGenerator.generateByteCode(validJavaCode, className);
        AgentLoader classLoader = new AgentLoader();

        // Act
        Class<?> loadedClass = null;
        try {
            loadedClass = classLoader.findClass(className);
        } catch (ClassNotFoundException e) {
            fail("Class should have been found.");
        }

        // Assert
        assertNotNull(loadedClass);
        assertEquals(className, loadedClass.getName());
    }

    @Test
    void testFindNonExistingClass() {
        // Arrange
        AgentLoader classLoader = new AgentLoader();

        // Act & Assert
        String nonExistingClassName = "AnyBadCodeWhatsoever";
        assertThrows(ClassNotFoundException.class, () -> classLoader.findClass(nonExistingClassName));
    }
}
