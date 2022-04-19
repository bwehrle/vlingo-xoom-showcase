package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Properties;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.plugin.PluginConfiguration;
import io.vlingo.xoom.actors.plugin.PluginProperties;
import io.vlingo.xoom.actors.plugin.mailbox.agronampscarrayqueue.ManyToOneConcurrentArrayQueuePlugin;
import io.vlingo.xoom.actors.plugin.mailbox.agronampscarrayqueue.ManyToOneConcurrentArrayQueuePlugin.ManyToOneConcurrentArrayQueuePluginConfiguration;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@State(Scope.Benchmark)
public class EchoServerActorMessageBenchmark {
  private World world;
  private static final int MaxCount = 100;  /* NOTE THIS IS 100, not 100 x 10^6) */
  private int countReceived;

  @Setup
  public void setup() {
    world =  World.start("EchoServerActorMessageBenchmark");
  }

  @TearDown
  public void teardown() {
    if (countReceived != MaxCount) {
      throw new IllegalArgumentException();
    }

    System.out.println("Closing world-shutdown");
    world.stage().stop();
    world.terminate();
  }


  @Benchmark
  @Threads(1)
  public void throughputBenchmark() {
    int countUntil = MaxCount;
    TestResults testResults = new TestResults(countUntil);
    String id1 = String.format("id-%d", (int) (Math.random() * 10.0));
    String id2 = String.format("id-%d", (int) (Math.random() * 10.0));

    final EchoServer echoServer = world.actorFor(
            EchoServer.class,
            Definition.has(EchoServerTestResultActor.class,
                    Definition.parameters(testResults),
                    "Ss",
                    id1));

    final EchoClient echoClient = world.actorFor(
            EchoClient.class,
            Definition.has(EchoClientActor.class,
                    Definition.parameters(echoServer),
                    "ssaSS",
                    id2));

    for (int i = 0; i < countUntil; i++) {
      echoClient.doEchoNoCompletes();
    }
    countReceived = testResults.waitForExpectedMessages();
  }
}