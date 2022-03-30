package net.dariolucic.codec;

import io.vertx.core.buffer.Buffer;

public interface Codec {

    byte[] encode(Message msg);
    Message decode(Buffer buffer);
}
