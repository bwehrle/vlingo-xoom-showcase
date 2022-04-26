package io.vlingo.xoom.examples.benchmark.actors;

public interface ExecuteUntil {

    void happened();

    int awaitUntilComplete();
}
