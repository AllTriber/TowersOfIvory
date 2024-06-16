package com.han.towersofivory.ui.businesslayer.services;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.ui.businesslayer.services.renderers.FieldOfViewRenderer;
import com.han.towersofivory.ui.businesslayer.services.renderers.DefaultRenderer;
import com.han.towersofivory.ui.businesslayer.services.renderers.FogOfWarRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Service class for the ASCII world
 * A class that provides the ASCII representation of the world
 */
public class ASCIIWorldService {

    private static final Logger LOGGER = LogManager.getLogger(ASCIIWorldService.class);

    /**
     * Renders the world based on the chosen configuration, is associated with concern C18 of stakeholder C3: ASD-studenten
     *
     * @param world The world to render
     * @return The ASCII representation of the world
     */
    public String render(World world) {
        return switch (getRendererFromProperties()) {
            case "Default" -> {
                DefaultRenderer defaultRenderer = new DefaultRenderer(12, 12);
                yield defaultRenderer.render(world);
            }
            case "FogOfWar" -> {
                FogOfWarRenderer fogOfWarRenderer = new FogOfWarRenderer(24, 12);
                yield fogOfWarRenderer.render(world);
            }
            case "FieldOfView" -> {
                FieldOfViewRenderer fieldOfViewRenderer = new FieldOfViewRenderer(12, 12);
                yield fieldOfViewRenderer.render(world);}
            default -> {
                LOGGER.error("No valid renderer found");
                yield "Error rendering world";
            }
        };
    }

    private String getRendererFromProperties() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("src\\main\\resources\\configuration.properties")) {
            prop.load(input);
            return prop.getProperty("UI_RENDERER");
        } catch (IOException e) {
            LOGGER.error("Could not read configuration file", e);
            return "";
        }
    }
}
