package com.han.towersofivory.network.businesslayer.threading.operation;

public interface IOperation {
    void execute();

    long getTimeout();

    int getPriority();

    void interrupt();
}