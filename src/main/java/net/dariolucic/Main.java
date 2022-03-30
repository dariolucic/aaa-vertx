package net.dariolucic;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import net.dariolucic.codec.Codec;
import net.dariolucic.codec.diameter.DiameterCodec;
import net.dariolucic.dictionary.DiameterDictionary;
import net.dariolucic.dictionary.Dictionary;
import net.dariolucic.server.DiameterServer;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        /*
         * Initialize server here before deploying verticles and start accepting incoming request (just load codec and dictionary for now)
         */

        Dictionary diameterDictionary = new DiameterDictionary();
        Codec diameterCodec = new DiameterCodec(diameterDictionary);

        DiameterServer diameterServer = new DiameterServer(diameterCodec);

        //Vertx vertx = Vertx.factory.vertx();
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(diameterServer, asyncResult -> {
            if(asyncResult.succeeded()) {
                diameterServer.setDeploymentID(asyncResult.result());
                System.out.println("Deployed verticle with deploymentID = " + diameterServer.getDeploymentID());
            } else {
                System.out.println("TcpVert.x start failure");
                System.out.println(asyncResult.cause().getMessage());
            }
        });

        //TODO: remove tcp client section
        Thread.sleep(3000); //wait for vertx.deployVerticle(...) to finish

        System.out.println("TcpVertex id: " + diameterServer.getDeploymentID());

        //extract this in separate verticle
        NetClient tcpClient = vertx.createNetClient();
        tcpClient.connect(13871, "localhost", asyncResult -> {
            if(asyncResult.succeeded()) {
                int data= 0;
                NetSocket socket = asyncResult.result();
                for (int i = 0; i < 1; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Client connected: " + i);
                    Buffer buff = Buffer.buffer();
                    //int firstbyte = 0;
                    int version = 1;
                    int firstbyte = version << 24;
                    firstbyte += 8;
                    buff.appendInt(firstbyte);
                    buff.appendInt(130);
                    buff.appendInt(227);
                    buff.appendInt(345);
                    buff.appendInt(111);
                    socket.write(buff);
                    System.out.println("socket write");
                }
            } else {
                System.out.println("Failed to connect: " + asyncResult.cause().getMessage());
            }
        });
    }
}