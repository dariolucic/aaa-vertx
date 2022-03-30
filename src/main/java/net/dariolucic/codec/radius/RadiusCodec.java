package net.dariolucic.codec.radius;

import io.vertx.core.buffer.Buffer;
import net.dariolucic.codec.Codec;
import net.dariolucic.codec.Message;

public class RadiusCodec implements Codec {

    public byte[] encode(Message msg) {
        return new byte[] {};
    }

    public Message decode(Buffer buffer) {
        return new RadiusMessage();
    }

}
