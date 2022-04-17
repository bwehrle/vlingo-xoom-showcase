package io.vlingo.xoom.examples.benchmark.actors;


import io.vlingo.xoom.common.Completes;

import java.util.function.Consumer;

public interface EchoServer {

    void echoCount(int counter);

    void echoString(String message);

    void echoObj(Object message);

    Completes<Integer> countEchoObject(Object obj);
}
