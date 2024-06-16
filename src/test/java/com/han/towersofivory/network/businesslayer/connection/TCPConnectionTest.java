package com.han.towersofivory.network.businesslayer.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.TestPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TCPConnectionTest {

    private SocketChannel socketChannelMock;
    private TCPConnection spyTcpConnection;
    private IConnectionHandler connectionHandlerMock;

    @BeforeEach
    void setUp() throws ConnectionFailedException, IOException {
        socketChannelMock = mock(SocketChannel.class);

        Socket socket = mock(Socket.class);
        when(socketChannelMock.socket()).thenReturn(socket);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        when(socketChannelMock.getLocalAddress()).thenReturn(inetSocketAddress);

        connectionHandlerMock = mock(IConnectionHandler.class);

        spyTcpConnection = spy(new TCPConnection(socketChannelMock, false, connectionHandlerMock));
    }

    @Test
    void testDoSendNoConnection() {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(false);

        // Act
        spyTcpConnection.doSend(null);

        // Assert
        verify(spyTcpConnection, times(1)).handleSendException(any(ClosedChannelException.class));
    }

    @Test
    void testDoSendNullPacket() {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);

        // Act
        spyTcpConnection.doSend(null);

        // Assert
        verify(spyTcpConnection, times(1)).handleSendException(any(SendFailedException.class));
    }

    @Test
    void testDoSendSuccessful() throws IOException {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);
        TestPacket testPacket = new TestPacket("Test");

        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", testPacket.getClass().getName());
        expectedJson.addProperty("data", new Gson().toJson(testPacket));

        doReturn(expectedJson).when(spyTcpConnection).createPacketJson(any(BasePacket.class));

        ByteBuffer capturedBuffer = ByteBuffer.allocate(8096);

        doAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            capturedBuffer.put(buffer);
            capturedBuffer.flip();
            return capturedBuffer.remaining();
        }).when(socketChannelMock).write(any(ByteBuffer.class));

        // Act
        spyTcpConnection.doSend(testPacket);

        // Assert
        byte[] expectedData = (expectedJson + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
        byte[] actualData = new byte[capturedBuffer.remaining()];
        capturedBuffer.get(actualData);

        assertArrayEquals(expectedData, actualData);
        verify(spyTcpConnection, times(1)).createPacketJson(testPacket);
        verify(socketChannelMock, times(1)).write(any(ByteBuffer.class));
    }

    @Test
    void testReceiveNoConnection() {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(false);

        // Act
        String receivedPacket = spyTcpConnection.receive();

        // Assert
        assertNull(receivedPacket);
        verify(spyTcpConnection, times(1)).handleReceiveException(any(ClosedChannelException.class));
    }

    @Test
    void testReceiveEmptyPacket() throws IOException {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);
        ByteBuffer readBuffer = ByteBuffer.allocate(8096);
        when(socketChannelMock.read(readBuffer)).thenReturn(0);

        // Act
        String receivedPacket = spyTcpConnection.receive();

        // Assert
        assertTrue(receivedPacket.isEmpty());
    }

    @Test
    void testReceiveSuccessful() throws IOException, ClassNotFoundException {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);

        TestPacket expectedTestPacket = new TestPacket("Test");
        JsonObject expectedJson = spyTcpConnection.createPacketJson(expectedTestPacket);
        String expectedRawJson = spyTcpConnection.jsonToJSONString(expectedJson);

        ByteBuffer readBuffer = ByteBuffer.allocate(8096);
        readBuffer.put(expectedRawJson.getBytes(StandardCharsets.UTF_8));
        readBuffer.flip();

        when(socketChannelMock.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            int remaining = readBuffer.remaining();
            buffer.put(readBuffer);
            return remaining;
        });

        doReturn(expectedTestPacket).when(spyTcpConnection).parsePacket(anyString());

        // Act
        String receivedPacket = spyTcpConnection.receive();

        // Assert
        assertNotNull(receivedPacket);
        assertEquals(expectedTestPacket, spyTcpConnection.parsePacket(receivedPacket));
        verify(spyTcpConnection, times(1)).parsePacket(expectedRawJson);
    }

    @Test
    void testReceiveIOException() throws IOException {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);
        doThrow(new IOException()).when(socketChannelMock).read(any(ByteBuffer.class));

        // Act
        String receivedPacket = spyTcpConnection.receive();

        // Assert
        assertNull(receivedPacket);
        verify(spyTcpConnection, times(1)).handleReceiveException(any(IOException.class));
    }

    @Test
    void testCloseConnection() throws ConnectionFailedException, IOException {
        // Arrange
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 0));

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",
                    serverSocketChannel.socket().getLocalPort()))) {
                serverSocketChannel.accept();

                TCPConnection tcpConnection = spy(new TCPConnection(socketChannel, true));

                // Act
                tcpConnection.close();

                // Assert
                assertFalse(tcpConnection.isConnected());
                verify(tcpConnection, times(1)).notifyDisconnect();
            }
        }
    }

    @Test
    void testCloseAlreadyClosedConnection() throws ConnectionFailedException, IOException {
        // Arrange
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 0));

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",
                    serverSocketChannel.socket().getLocalPort()))) {
                serverSocketChannel.accept();

                TCPConnection tcpConnection = spy(new TCPConnection(socketChannel, true));
                tcpConnection.close();

                // Act
                tcpConnection.close();

                // Assert
                assertFalse(tcpConnection.isConnected());
                verify(tcpConnection, times(1)).notifyDisconnect();
            }
        }
    }

    @Test
    void testCloseNotConnected() throws ConnectionFailedException, IOException {
        // Arrange
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 0));

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",
                    serverSocketChannel.socket().getLocalPort()))) {
                serverSocketChannel.accept();

                TCPConnection tcpConnection = spy(new TCPConnection(socketChannel, true));
                socketChannel.close();  // Close the socket to simulate not connected state

                // Act
                tcpConnection.close();

                // Assert
                assertFalse(tcpConnection.isConnected());
                verify(tcpConnection, times(0)).notifyDisconnect();
            }
        }
    }

    @Test
    void testGetIpWhenConnected() {
        // Arrange
        String expectedIp = "/127.0.0.1:8080";
        when(spyTcpConnection.isConnected()).thenReturn(true);

        // Act
        String actualIp = spyTcpConnection.getIp();

        // Assert
        assertEquals(expectedIp, actualIp);
    }

    @Test
    void testGetIpWhenNotConnected() {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(false);

        // Act
        String actualIp = spyTcpConnection.getIp();

        // Assert
        assertEquals("0.0.0.0", actualIp);
    }

    @Test
    void testGetPortWhenConnected() {
        // Arrange
        int expectedPort = 8080;
        when(spyTcpConnection.isConnected()).thenReturn(true);

        // Act
        int actualPort = spyTcpConnection.getPort();

        // Assert
        assertEquals(expectedPort, actualPort);
    }

    @Test
    void testGetPortWhenNotConnected() {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(false);

        // Act
        int actualPort = spyTcpConnection.getPort();

        // Assert
        assertEquals(0, actualPort);
    }

    @Test
    void testIsConnectionWhenNotConnected() throws ConnectionFailedException, IOException {
        // Arrange
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 0));

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",
                    serverSocketChannel.socket().getLocalPort()))) {
                serverSocketChannel.accept();

                TCPConnection tcpConnection = spy(new TCPConnection(socketChannel, true));
                socketChannel.close();  // Close the socket to simulate not connected state

                // Assert
                assertFalse(tcpConnection.isConnected());
            }
        }
    }

    @Test
    void testIsConnectionWhenConnected() throws ConnectionFailedException, IOException {
        // Arrange
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 0));

            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",
                    serverSocketChannel.socket().getLocalPort()))) {
                serverSocketChannel.accept();

                TCPConnection tcpConnection = spy(new TCPConnection(socketChannel, true));

                // Assert
                assertTrue(tcpConnection.isConnected());
            }
        }
    }

    @Test
    void testAccept() {
        // Arrange
        UUID expectedUUID = UUID.randomUUID();

        // Act
        spyTcpConnection.accept(expectedUUID);

        // Assert
        assertEquals(spyTcpConnection.getUuid(), expectedUUID);
        verify(spyTcpConnection, times(1)).notifyConnect();
    }

    @Test
    void testSendTimeout() throws IOException {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);
        spyTcpConnection.accept(UUID.randomUUID());
        TestPacket testPacket = new TestPacket("Test");

        doThrow(new SocketTimeoutException()).when(socketChannelMock).write(any(ByteBuffer.class));

        // Act
        spyTcpConnection.doSend(testPacket);

        // Assert
        verify(spyTcpConnection, times(1)).handleSendException(any(SocketTimeoutException.class));
        verify(connectionHandlerMock, times(1)).onTimeout(any(BaseConnection.class), any(SocketTimeoutException.class));
    }

    @Test
    void testReceiveTimeout() throws IOException {
        // Arrange
        when(spyTcpConnection.isConnected()).thenReturn(true);
        spyTcpConnection.accept(UUID.randomUUID());

        doThrow(new SocketTimeoutException()).when(socketChannelMock).read(any(ByteBuffer.class));

        // Act
        String receivedPacket = spyTcpConnection.receive();

        // Assert
        assertNull(receivedPacket);
        verify(spyTcpConnection, times(1)).handleReceiveException(any(SocketTimeoutException.class));
        verify(connectionHandlerMock, times(1)).onTimeout(any(BaseConnection.class), any(SocketTimeoutException.class));
    }
}
