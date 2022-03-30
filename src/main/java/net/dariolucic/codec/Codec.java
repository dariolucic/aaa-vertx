package net.dariolucic.codec;

public interface Codec {

    byte[] encode(Message msg);
    Message decode(byte[] buffer);
}
