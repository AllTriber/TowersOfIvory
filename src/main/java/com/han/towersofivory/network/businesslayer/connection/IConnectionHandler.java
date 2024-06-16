package com.han.towersofivory.network.businesslayer.connection;

public interface IConnectionHandler {
    void onTimeout(BaseConnection baseConnection, Exception e);

    void onDisconnect(BaseConnection baseConnection, Exception e);

    void onConnect(BaseConnection baseConnection, Exception e);

    void onReceiveFailed(BaseConnection baseConnection, Exception e);

    void onSendFailed(BaseConnection baseConnection, Exception e);
}
