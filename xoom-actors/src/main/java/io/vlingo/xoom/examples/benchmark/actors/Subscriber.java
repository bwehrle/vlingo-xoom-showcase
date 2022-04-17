package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Mailbox;

import java.util.List;

public interface Subscriber {

    void notifyEvent(String msg);
}

