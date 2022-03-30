package net.dariolucic.codec.diameter;

import net.dariolucic.codec.Codec;
import net.dariolucic.codec.Message;

public class DiameterCodec implements Codec {
    @Override
    public byte[] encode(Message msg) {
        return new byte[0];
    }

    @Override
    public Message decode(byte[] buffer) {
        return null;
    }
}
