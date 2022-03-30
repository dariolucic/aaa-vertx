package net.dariolucic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import net.dariolucic.codec.Message;

public class Main {

    private static DiameterServer tcpVertex = new DiameterServer();

    public static void main(String[] args) throws InterruptedException {

        //Vertx vertx = Vertx.factory.vertx();
        Vertx vertx = Vertx.vertx();

/*
        vertx.deployVerticle(vertxtest, asyncResult -> {
            if(asyncResult.succeeded()) {
                final String verticleID = asyncResult.result();
                vertxtest.setDeploymentID(verticleID);
                System.out.println("Deployed verticle with deploymentID = " + vertxtest.getDeploymentID());
            }
        });
*/

/*
        vertx.deployVerticle(vertxtest2, asyncResult -> {
            if(asyncResult.succeeded()) {
                vertxtest2.setDeploymentID(asyncResult.result());
                System.out.println("Deployed verticle with deploymentID = " + vertxtest2.getDeploymentID());
            }
        });
*/

        vertx.deployVerticle(tcpVertex, asyncResult -> {
            if(asyncResult.succeeded()) {
                tcpVertex.setDeploymentID(asyncResult.result());
                System.out.println("Deployed verticle with deploymentID = " + tcpVertex.getDeploymentID());
            } else {
                System.out.println("TcpVert.x start failure");
                System.out.println(asyncResult.cause().getMessage());
            }
        });

        Thread.sleep(3000); //wait for vertx.deployVerticle(...) to finish

        //System.out.println("Http1 id: " + vertxtest.getDeploymentID());
        //System.out.println("Http2 id: " + vertxtest2.getDeploymentID());
        System.out.println("TcpVertex id: " + tcpVertex.getDeploymentID());

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
                    //buff.appendInt(i).appendString("Hello");
                    int firstbyte = 0;
                    int version = 1;
                    firstbyte = version << 24;
                    firstbyte += 8;
                    buff.appendInt(firstbyte);
                    buff.appendInt(130);
                    socket.write(buff);
                    System.out.println("socket write");
                }
            } else {
                System.out.println("Failed to connect: " + asyncResult.cause().getMessage());
            }
        });
    }

}

class DiameterServer extends AbstractVerticle {

    private String deploymentID;

    private NetServer tcpServer;

    public String getDeploymentID() {
        return deploymentID;
    }

    public void setDeploymentID(String deploymentID) {
        this.deploymentID = deploymentID;
    }

    public void start() {
        /*
         * Initialize server here before start accepting incoming request (just load codec for now)
         */

        System.out.println("Tcp Verticle started");
        tcpServer = vertx.createNetServer();

        tcpServer
                .connectHandler(socketHandler -> {
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

    private void getFromWire(Buffer buffer) {

        //handle received data here
        //use codec to decode raw bytes and create request object

        if (buffer.length() >= 4) {

            int tmp = buffer.getInt(0);
            int length = tmp & 0xffffff;

            if (buffer.length() < length) {
                System.out.println("Not enough data in buffer");
                return;
            }
            // copy buffer content and delegate handling of received data to worker verticle
            Buffer rawRequest = buffer.copy();
            Message msg = decode(buffer);
        }

    }

    private Message decode(Buffer buffer) {

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
