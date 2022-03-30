package net.dariolucic.codec.diameter;

/*
               0                   1                   2                   3
               0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
               |    Version    |                 Message Length                |
               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
               | Command Flags |                  Command Code                 |
               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
               |                         Application-ID                        |
               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
               |                      Hop-by-Hop Identifier                    |
               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
               |                      End-to-End Identifier                    |
               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
               |  AVPs ...
               +-+-+-+-+-+-+-+-+-+-+-+-+-
*/

import io.vertx.core.buffer.Buffer;
import net.dariolucic.codec.Codec;
import net.dariolucic.codec.Message;
import net.dariolucic.dictionary.Dictionary;

public class DiameterCodec implements Codec {

    private Dictionary dict;

    public DiameterCodec(){}

    public DiameterCodec(Dictionary dict) {
        this.dict = dict;
    }

    public Dictionary getDict() {
        return dict;
    }

    public void setDict(Dictionary dict) {
        this.dict = dict;
    }

    @Override
    public byte[] encode(Message msg) {
        return new byte[0];
    }

    @Override
    public Message decode(Buffer buffer) {
        int tmp = buffer.getInt(0);
        byte version = (byte) (tmp >> 24);
        int length = tmp & 0xffffff;
        System.out.println("version: " + version);
        System.out.println("length: " + length);

        tmp = buffer.getInt(4);
        short flags = (short) ((tmp >> 24) & 0xff);
        int commandCode = tmp & 0xffffff;
        System.out.println("command code: " + commandCode);

        long applicationId = ((long) buffer.getInt(8) << 32) >>> 32;
        System.out.println("applicationId: " + applicationId);

        long hopByHop = ((long) buffer.getInt(12) << 32) >>> 32;
        System.out.println("hopByHop: " + hopByHop);

        long endToEnd = ((long) buffer.getInt(16) << 32) >>> 32;
        System.out.println("endToEnd: " + endToEnd);

        return null;
    }
}
