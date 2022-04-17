package io.vlingo.xoom.examples.benchmark.actors;
import io.vlingo.xoom.common.Completes;

public interface EchoClient {
    Completes<Integer> doEcho();

    void doEchoNoCompletes();
}
