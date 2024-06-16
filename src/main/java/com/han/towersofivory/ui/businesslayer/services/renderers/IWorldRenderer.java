package com.han.towersofivory.ui.businesslayer.services.renderers;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

/**
 * Interface for the world renderer,
 * contains the method to render the world
 */
public interface IWorldRenderer {

    /**
     * Renders the world.
     *
     * @param world The world to render
     * @return The ASCII representation of the world (as a string)
     */
    String render(World world);
}
