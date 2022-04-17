package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Completes;

public class EchoServerTestResultActor extends Actor implements EchoServer {

    private final TestResults testResults;
    private int counterValue;
    private String echoString;
    private Object echoObj;

    public EchoServerTestResultActor(TestResults testResults) {
        this.testResults = testResults;
    }

    @Override
    public void echoCount(int counter) {
        counterValue = counter;
        testResults.increment();
    }

    @Override
    public void echoString(String stringValue) {
        echoString = stringValue;
        testResults.increment();
    }

    @Override
    public void echoObj(Object message) {
        echoObj = message;
        testResults.increment();
    }

    @Override
    public Completes<Integer> countEchoObject(Object obj) {
        testResults.increment();
        counterValue++;
        return completes().with(counterValue);
    }
}
