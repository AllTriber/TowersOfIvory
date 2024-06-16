package com.han.towersofivory.network.businesslayer.packet;

import com.han.towersofivory.network.businesslayer.exceptions.DispatchPacketException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * PacketDispatcher class is responsible for dispatching packets to registered listeners.
 * It maintains a map of registered listeners and their corresponding packet handling methods.
 * It provides methods to register and unregister listeners, and to dispatch packets to the appropriate listeners.
 */
public class PacketDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(PacketDispatcher.class);

    private final PacketListenerPriorityComparator priority;
    private final Map<IPacketListener, List<Method>> registeredListeners;

    /**
     * Constructor for the PacketDispatcher class.
     * Initializes the registeredListeners map and the priority comparator.
     */
    public PacketDispatcher() {
        this.registeredListeners = new HashMap<>();
        this.priority = new PacketListenerPriorityComparator();
    }

    /**
     * Registers a packet listener.
     *
     * @param listener The listener to register.
     */
    public void register(IPacketListener listener) {
        registeredListeners.put(listener, getPacketHandlers(listener));
        LOGGER.info("Registered listener: {}", listener.getClass().getSimpleName());
    }

    /**
     * Unregisters a packet listener.
     *
     * @param listener The listener to unregister.
     */
    public void unregister(IPacketListener listener) {
        registeredListeners.remove(listener);
        LOGGER.info("Unregistered listener: {}", listener.getClass().getSimpleName());
    }

    /**
     * Dispatches a packet to all registered listeners.
     *
     * @param packet The packet to dispatch.
     * @throws DispatchPacketException If an error occurs while dispatching the packet.
     */
    public <T extends BasePacket> void dispatch(T packet) throws DispatchPacketException {
        LOGGER.info("Dispatching packet: {}", packet.getClass().getSimpleName());
        for (IPacketListener packetListener : registeredListeners.keySet()) {
            if (packetListener != null) {
                dispatchToListener(packet, packetListener);
            }
        }
    }

    /**
     * Dispatches a packet to a specific listener.
     *
     * @param packet   The packet to dispatch.
     * @param listener The listener to which the packet should be dispatched.
     * @throws DispatchPacketException If an error occurs while dispatching the packet.
     */
    private <T extends BasePacket> void dispatchToListener(T packet, IPacketListener listener) throws DispatchPacketException {
        List<Method> methods = registeredListeners.get(listener);


        for (Method method : methods) {
            if (method.getParameterCount() == 1 && packet.getClass().isAssignableFrom(method.getParameterTypes()[0])) {
                invokeMethod(listener, method, packet);
            }
        }
    }

    /**
     * Invokes a packet handling method on a listener.
     *
     * @param listener The listener on which to invoke the method.
     * @param method   The method to invoke.
     * @param packet   The packet to pass to the method.
     * @throws DispatchPacketException If an error occurs while invoking the method.
     */
    private <T extends BasePacket> void invokeMethod(IPacketListener listener, Method method, T packet) throws DispatchPacketException {
        if (method == null) {
            return;
        }

        try {
            method.invoke(listener, packet);
            LOGGER.info("Packet {} dispatched to {}", packet.getClass().getSimpleName(), listener.getClass().getSimpleName());
        } catch (InvocationTargetException e) {
            LOGGER.error("InvocationTargetException while dispatching packet to listener {}: {}", listener.getClass().getSimpleName(), e.getMessage(), e);
            throw new DispatchPacketException(String.format("%s in listener %s could not be dispatched due to InvocationTargetException", method.getName(), listener.getClass().getSimpleName()), e);
        } catch (IllegalAccessException e) {
            LOGGER.error("IllegalAccessException while dispatching packet to listener {}: {}", listener.getClass().getSimpleName(), e.getMessage(), e);
            throw new DispatchPacketException(String.format("%s in listener %s could not be dispatched due to IllegalAccessException", method.getName(), listener.getClass().getSimpleName()), e);
        }
    }


    /**
     * Retrieves the packet handling methods for a given listener.
     *
     * @param listener The listener for which to retrieve the packet handling methods.
     * @return A sorted list of packet handling methods.
     */
    private List<Method> getPacketHandlers(IPacketListener listener) {
        Method[] methods = listener.getClass().getDeclaredMethods();
        List<Method> result = new LinkedList<>();
        for (Method method : methods) {
            if (isHandler(method)) {
                result.add(method);
            }
        }
        return result.reversed();
    }

    /**
     * Checks if a method is a packet handler.
     *
     * @param method The method to check.
     * @return True if the method is a packet handler, false otherwise.
     */
    private boolean isHandler(Method method) {
        return method.getAnnotation(IPacketHandler.class) != null;
    }
}
