package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Completes;

public class EchoServerTestResultActor extends Actor implements EchoServer {

    private final ExecuteUntil executeUntil;
    private int counterValue;
    private String echoString;
    private Object echoObj;

    public EchoServerTestResultActor(ExecuteUntil executeUntil) {
        this.executeUntil = executeUntil;
    }

    @Override
    public void echoCount(int counter) {
        counterValue = counter;
        executeUntil.happened();
    }

    @Override
    public void echoString(String stringValue) {
        echoString = stringValue;
        executeUntil.happened();
    }

    @Override
    public void echoObj(Object message) {
        echoObj = message;
        executeUntil.happened();
    }

    @Override
    public Completes<Integer> countEchoObject(Object obj) {
        executeUntil.happened();
        counterValue++;
        return completes().with(counterValue);
    }
}
