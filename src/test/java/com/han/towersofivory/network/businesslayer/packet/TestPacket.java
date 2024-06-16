package com.han.towersofivory.network.businesslayer.packet;

public class TestPacket extends BasePacket {
    private final String test;

    public TestPacket(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
