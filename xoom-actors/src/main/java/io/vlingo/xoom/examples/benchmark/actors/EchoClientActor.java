package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorProxyBase;
import io.vlingo.xoom.common.Completes;

public class EchoClientActor extends Actor implements EchoClient {

    private final EchoServer echoServer;
    private final Object dummyObj;

    public EchoClientActor(EchoServer echoServer) {
        this.echoServer =  ActorProxyBase.thunk(stage(), echoServer);
        this.dummyObj = new Object();
    }

    @Override
    public Completes<Integer> doEcho() {
        // create a completes, return it, and complete it when the echo completes
        return answerFrom(echoServer.countEchoObject(dummyObj));
    }

    @Override
    public void doEchoNoCompletes() {
        echoServer.countEchoObject(dummyObj);
    }
}
