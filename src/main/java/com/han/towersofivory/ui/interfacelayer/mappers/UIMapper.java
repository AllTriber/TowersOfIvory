package com.han.towersofivory.ui.interfacelayer.mappers;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.ui.businesslayer.services.UIService;
import com.han.towersofivory.ui.interfacelayer.interfaces.IUpdateUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UIMapper implements IUpdateUI {
    private static final Logger LOGGER = LogManager.getLogger(UIMapper.class);
    private final UIService uiService;

    public UIMapper(UIService uiService) {
        this.uiService = uiService;
    }

    @Override
    public void updateUI(World world, BasePacket packet) {
        LOGGER.info("Updating UI");
        uiService.updateUI(world, packet);
    }

}
