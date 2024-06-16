package com.han.towersofivory.network.businesslayer.packet;

/**
 * PacketPriority is an enumeration that defines the priority levels for packets.
 * It is used to prioritize the handling of packets in the network layer.
 * The priority levels are defined as follows:
 * LOWEST: The lowest priority level.
 * LOW: A low priority level.
 * NORMAL: The normal priority level.
 * HIGH: A high priority level.
 * HIGHEST: The highest priority level.
 * MONITOR: A special priority level used for monitoring purposes.
 */
public enum PacketPriority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4),
    MONITOR(5);

    private final int priority;

    /**
     * Constructor for the PacketPriority enumeration.
     *
     * @param priority The priority level as an integer.
     */
    PacketPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Retrieves the priority level as an integer.
     *
     * @return The priority level as an integer.
     */
    public int getPriority() {
        return priority;
    }
}