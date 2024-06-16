package com.han.towersofivory.network.businesslayer.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IPacketHandler is an annotation used to mark methods that handle packets.
 * It provides a priority setting for the handling of packets.
 * This annotation can be applied to methods and types.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IPacketHandler {
    /**
     * Sets the priority of the packet handler.
     * The default priority is LOWEST.
     *
     * @return The priority of the packet handler.
     */
    PacketPriority priority() default PacketPriority.LOWEST;
}