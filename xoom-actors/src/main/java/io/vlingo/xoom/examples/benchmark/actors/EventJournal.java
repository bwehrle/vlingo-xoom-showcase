package io.vlingo.xoom.examples.benchmark.actors;

public interface EventJournal {
    void subscribe(Subscriber subscriber);

    void logEvent(String s);
}
