package net.dariolucic.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import net.dariolucic.codec.Codec;
import net.dariolucic.dictionary.Dictionary;

public abstract class AbstractServer extends AbstractVerticle {

    protected Dictionary dict;

    protected Codec codec;

    protected String deploymentID;

    protected NetServer tcpServer;

    public String getDeploymentID() {
        return deploymentID;
    }

    public void setDeploymentID(String deploymentID) {
        this.deploymentID = deploymentID;
    }

    protected abstract void getFromWire(Buffer buffer);

}
