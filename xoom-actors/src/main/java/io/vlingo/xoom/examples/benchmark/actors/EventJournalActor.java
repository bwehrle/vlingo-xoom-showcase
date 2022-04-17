package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorProxy;
import io.vlingo.xoom.actors.Proxy;

import java.util.LinkedList;
import java.util.List;

public class EventJournalActor extends Actor implements EventJournal {

    private final List<Subscriber> subscribers;

    public EventJournalActor() {
        subscribers = new LinkedList<>();
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void logEvent(String s) {
        for (Subscriber sub : subscribers) {
            sub.notifyEvent(s);
        }
    }
}
