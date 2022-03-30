package net.dariolucic.server;

import io.vertx.core.buffer.Buffer;
import net.dariolucic.codec.Codec;
import net.dariolucic.codec.Message;

public class DiameterServer extends AbstractServer {

    public DiameterServer(Codec diameterCodec) {
        this.codec = diameterCodec;
    }

    public void start() {

        System.out.println("Tcp Verticle started");
        tcpServer = vertx.createNetServer();

        tcpServer.connectHandler(socketHandler -> {
                    System.out.println("SocketHandler: " + socketHandler.remoteAddress().host());
                    socketHandler.handler(this::getFromWire); })
                .listen(13871, "localhost", asyncRes -> {
                    System.out.println("Async result: " + asyncRes.succeeded());
                    if(asyncRes.succeeded()) {
                        System.out.println("Server is now listening ");
                    } else {
                        System.out.println("Server bind failure.");
                    }});
    }

    public void stop() {
        System.out.println("Verticle stopped");
    }

    protected void getFromWire(Buffer buffer) {

        //handle received data here
        //use codec to decode raw bytes and create request object

        if (buffer.length() >= 4) {

            int tmp = buffer.getInt(0);
            int length = tmp & 0xffffff;

            if (buffer.length() < length) {
                System.out.println("Not enough data in buffer");
                return;
            }

            Message msg = codec.decode(buffer);
            //pass message to worker verticle for further processing
        }

    }

}
