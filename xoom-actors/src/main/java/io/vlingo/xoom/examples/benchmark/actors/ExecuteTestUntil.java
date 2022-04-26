package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.testkit.TestUntil;

public class ExecuteTestUntil implements ExecuteUntil {

    private TestUntil testUntil;
    private int expectedTimes;

    public ExecuteTestUntil(int times) {
        expectedTimes = times;
        testUntil = TestUntil.happenings(times);
    }

    @Override
    public void happened() {
        testUntil.happened();
    }

    @Override
    public int awaitUntilComplete()
    {
        testUntil.completes();
        return expectedTimes - testUntil.remaining();
    }
}
