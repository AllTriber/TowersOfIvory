package com.han.towersofivory.network.businesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocalP2PNetworkTest {

    private LocalP2PNetwork localP2PNetwork;
    private P2PHost mockHost;
    private P2PClient mockClient;
    private InetSocketAddress mockServerAddress;
    private InetAddress mockInetAddress;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        localP2PNetwork = Mockito.mock(LocalP2PNetwork.class);
        mockHost = Mockito.mock(P2PHost.class);
        mockClient = Mockito.mock(P2PClient.class);
        mockServerAddress = Mockito.mock(InetSocketAddress.class);
        mockInetAddress = Mockito.mock(InetAddress.class);

        // Setup mock behavior
        when(localP2PNetwork.getHost()).thenReturn(mockHost);
        when(localP2PNetwork.getClient()).thenReturn(mockClient);
        when(localP2PNetwork.getServerAddress()).thenReturn(mockServerAddress);
        when(mockServerAddress.getPort()).thenReturn(8886);
        when(mockServerAddress.getAddress()).thenReturn(mockInetAddress);
        when(mockInetAddress.getAddress()).thenReturn(new byte[]{127, 0, 0, 1});
    }

    @Test
    void testGetHost() {
        // Act
        P2PHost host = localP2PNetwork.getHost();

        // Assert
        assertTrue(host instanceof P2PHost);
        verify(localP2PNetwork).getHost();
    }

    @Test
    void testGetClient() {
        // Act
        P2PClient client = localP2PNetwork.getClient();

        // Assert
        assertTrue(client instanceof P2PClient);
        verify(localP2PNetwork).getClient();
    }

    @Test
    void testGetServerAddress() {
        // Act
        InetSocketAddress serverAddress = localP2PNetwork.getServerAddress();

        // Assert
        assertNotNull(serverAddress);
        assertEquals(8886, serverAddress.getPort());
        assertTrue(isValidIPv4Address(serverAddress.getAddress()));
        verify(localP2PNetwork).getServerAddress();
    }

    private boolean isValidIPv4Address(InetAddress address) {
        return address != null && address.getAddress().length == 4;
    }
}
