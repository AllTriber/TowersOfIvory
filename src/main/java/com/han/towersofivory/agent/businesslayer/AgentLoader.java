package com.han.towersofivory.agent.businesslayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * UC 6
 * A custom class loader for loading agent classes dynamically from byte code files.
 */
public class AgentLoader extends ClassLoader {

    /**
     * Finds and loads the class with the specified name from the byte code file.
     *
     * @param name the name of the class to be loaded.
     * @return the {@code Class} object that represents the loaded class.
     * @throws ClassNotFoundException if the class could not be found.
     */
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classData = loadClassFromFile(name);
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class " + name, e);
        }
    }

    /**
     * Loads the byte code of the class with the specified name from the file system.
     *
     * @param className the name of the class.
     * @return the byte array containing the byte code of the class.
     * @throws IOException if an I/O error occurs while reading the class file.
     */
    private byte[] loadClassFromFile(String className) throws IOException {
        String classFilePath = classNameToFilePath(className);
        Path path = Paths.get(classFilePath);
        return Files.readAllBytes(path);
    }

    /**
     * Converts the class name to the corresponding file path in the file system.
     *
     * @param className the name of the class.
     * @return the file path representing the location of the class file.
     */
    private String classNameToFilePath(String className) {
        String separator = System.getProperty("file.separator");
        return "src" + separator + "main" + separator + "resources" +
                separator + "agentJavaByteCode" + separator + className.replace('.', separator.charAt(0)) + ".class";
    }
}
