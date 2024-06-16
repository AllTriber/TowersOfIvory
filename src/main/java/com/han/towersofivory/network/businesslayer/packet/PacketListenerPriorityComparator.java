package com.han.towersofivory.network.businesslayer.packet;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * PacketListenerPriorityComparator is a Comparator for Methods that uses the priority of the IPacketHandler annotation.
 * It is used to sort packet handling methods based on their priority.
 */
public class PacketListenerPriorityComparator implements Comparator<Method> {
    /**
     * Compares two Methods based on the priority of their IPacketHandler annotation.
     *
     * @param one The first method to compare.
     * @param two The second method to compare.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Method one, Method two) {
        return one.getAnnotation(IPacketHandler.class).priority().getPriority() - two.getAnnotation(IPacketHandler.class).priority().getPriority();
    }
}