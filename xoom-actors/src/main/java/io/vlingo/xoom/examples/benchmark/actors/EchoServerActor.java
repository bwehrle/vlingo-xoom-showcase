package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Completes;

public class EchoServerActor extends Actor implements EchoServer {

    private final String echoerId;
    private int counter;

    public EchoServerActor(String id) {
        this.echoerId = id;
        this.counter = 0;
    }

    @Override
    public void echoCount(int counter) {
        this.counter = counter;
    }

    @Override
    public void echoString(String stringValue) {
    }

    @Override
    public void echoObj(Object message) {
    }

    @Override
    public Completes<Integer> countEchoObject(final Object message) {
        this.counter++;
        return completes().with(counter);
    }
}
