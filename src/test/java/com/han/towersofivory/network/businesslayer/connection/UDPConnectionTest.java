package com.han.towersofivory.network.businesslayer.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.TestPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UDPConnectionTest {

    private DatagramChannel datagramChannelMock;
    private UDPConnection spyUdpConnection;

    @BeforeEach
    void setUp() throws IOException {
        datagramChannelMock = mock(DatagramChannel.class);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        when(datagramChannelMock.getLocalAddress()).thenReturn(inetSocketAddress);

        IConnectionHandler connectionHandlerMock = mock(IConnectionHandler.class);

        spyUdpConnection = spy(new UDPConnection(datagramChannelMock, false, connectionHandlerMock));
    }

    @Test
    void testDoSendNoConnection() {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(false);

        // Act
        spyUdpConnection.doSend(null);

        // Assert
        verify(spyUdpConnection, times(1)).handleSendException(any(ClosedChannelException.class));
    }

    @Test
    void testDoSendNullPacket() {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(true);

        // Act
        spyUdpConnection.doSend(null);

        // Assert
        verify(spyUdpConnection, times(1)).handleSendException(any(SendFailedException.class));
    }

    @Test
    void testDoSendSuccessful() throws IOException {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(true);
        TestPacket testPacket = new TestPacket("Test");

        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", testPacket.getClass().getName());
        expectedJson.addProperty("data", new Gson().toJson(testPacket));

        doReturn(expectedJson).when(spyUdpConnection).createPacketJson(any(BasePacket.class));

        ByteBuffer capturedBuffer = ByteBuffer.allocate(8096);

        doAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            capturedBuffer.put(buffer);
            capturedBuffer.flip();
            return capturedBuffer.remaining();
        }).when(datagramChannelMock).write(any(ByteBuffer.class));

        // Act
        spyUdpConnection.doSend(testPacket);

        // Assert
        byte[] expectedData = (expectedJson + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
        byte[] actualData = new byte[capturedBuffer.remaining()];
        capturedBuffer.get(actualData);

        assertArrayEquals(expectedData, actualData);
        verify(spyUdpConnection, times(1)).createPacketJson(testPacket);
        verify(datagramChannelMock, times(1)).write(any(ByteBuffer.class));
    }

    @Test
    void testReceiveNoConnection() {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(false);

        // Act
        String receive = spyUdpConnection.receive();

        // Assert
        assertNull(receive);
        verify(spyUdpConnection, times(1)).handleReceiveException(any(ClosedChannelException.class));
    }

    @Test
    void testReceiveEmptyPacket() throws IOException {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(true);
        ByteBuffer readBuffer = ByteBuffer.allocate(8096);
        when(datagramChannelMock.receive(readBuffer)).thenReturn(null);

        // Act
        String receivedPacket = spyUdpConnection.receive();

        // Assert
        assertNull(receivedPacket);
    }

    @Test
    void testReceiveSuccessful() throws IOException, ClassNotFoundException {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(true);

        TestPacket expectedTestPacket = new TestPacket("Test");
        JsonObject expectedJson = spyUdpConnection.createPacketJson(expectedTestPacket);
        String expectedRawJson = spyUdpConnection.jsonToJSONString(expectedJson);

        ByteBuffer readBuffer = ByteBuffer.allocate(8096);
        readBuffer.put(expectedRawJson.getBytes(StandardCharsets.UTF_8));
        readBuffer.flip();

        when(datagramChannelMock.receive(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            buffer.put(readBuffer);
            return mock(SocketAddress.class);
        });

        doReturn(expectedTestPacket).when(spyUdpConnection).parsePacket(anyString());

        // Act
        String receivedPacket = spyUdpConnection.receive();

        // Assert
        assertNotNull(receivedPacket);
        assertEquals(expectedTestPacket, spyUdpConnection.parsePacket(receivedPacket));
        verify(spyUdpConnection, times(1)).parsePacket(expectedRawJson);
    }

    @Test
    void testReceiveIOException() throws IOException {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(true);
        doThrow(new IOException()).when(datagramChannelMock).receive(any(ByteBuffer.class));

        // Act
        String receivedPacket = spyUdpConnection.receive();

        // Assert
        assertNull(receivedPacket);
        verify(spyUdpConnection, times(1)).handleReceiveException(any(IOException.class));
    }

    @Test
    void testCloseConnection() throws IOException {
        // Arrange
        DatagramChannel datagramChannel = DatagramChannel.open();
        UDPConnection udpConnection = spy(new UDPConnection(datagramChannel, true));

        // Act
        udpConnection.close();

        // Assert
        assertFalse(udpConnection.isConnected());
        verify(udpConnection, times(1)).notifyDisconnect();
    }

    @Test
    void testCloseAlreadyClosedConnection() throws IOException {
        // Arrange
        DatagramChannel datagramChannel = DatagramChannel.open();
        UDPConnection udpConnection = spy(new UDPConnection(datagramChannel, true));
        udpConnection.close();

        // Act
        udpConnection.close();

        // Assert
        assertFalse(udpConnection.isConnected());
        verify(udpConnection, times(1)).notifyDisconnect();
    }

    @Test
    void testCloseNotConnected() throws IOException {
        // Arrange
        DatagramChannel datagramChannel = DatagramChannel.open();
        UDPConnection udpConnection = spy(new UDPConnection(datagramChannel, true));
        datagramChannel.close();  // Close the channel to simulate not connected state

        // Act
        udpConnection.close();

        // Assert
        assertFalse(udpConnection.isConnected());
        verify(udpConnection, times(0)).notifyDisconnect();
    }

    @Test
    void testGetIpWhenConnected() {
        // Arrange
        String expectedIp = "127.0.0.1";
        when(spyUdpConnection.isConnected()).thenReturn(true);

        // Act
        String actualIp = spyUdpConnection.getIp();

        // Assert
        assertEquals(expectedIp, actualIp);
    }

    @Test
    void testGetIpWhenNotConnected() {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(false);

        // Act
        String actualIp = spyUdpConnection.getIp();

        // Assert
        assertEquals("0.0.0.0", actualIp);
    }

    @Test
    void testGetPortWhenConnected() {
        // Arrange
        int expectedPort = 8080;
        when(spyUdpConnection.isConnected()).thenReturn(true);

        // Act
        int actualPort = spyUdpConnection.getPort();

        // Assert
        assertEquals(expectedPort, actualPort);
    }

    @Test
    void testGetPortWhenNotConnected() {
        // Arrange
        when(spyUdpConnection.isConnected()).thenReturn(false);

        // Act
        int actualPort = spyUdpConnection.getPort();

        // Assert
        assertEquals(0, actualPort);
    }

    @Test
    void testIsConnectionWhenNotConnected() throws IOException {
        // Arrange
        DatagramChannel datagramChannel = DatagramChannel.open();
        UDPConnection udpConnection = spy(new UDPConnection(datagramChannel, true));
        datagramChannel.close();  // Close the channel to simulate not connected state

        // Act & Assert
        assertFalse(udpConnection.isConnected());
    }

    @Test
    void testIsConnectionWhenConnected() throws IOException {
        // Arrange
        DatagramChannel datagramChannel = DatagramChannel.open();
        UDPConnection udpConnection = spy(new UDPConnection(datagramChannel, true));

        // Act & Assert
        assertTrue(udpConnection.isConnected());
    }
}
