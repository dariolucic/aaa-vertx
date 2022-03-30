package net.dariolucic.codec.radius;

import net.dariolucic.codec.Codec;
import net.dariolucic.codec.Message;

public class RadiusCodec implements Codec {

    public byte[] encode(Message msg) {
        return new byte[] {};
    }

    public Message decode(byte[] buffer) {
        return new RadiusMessage();
    }

}
