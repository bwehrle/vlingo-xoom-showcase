package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.World;
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
    ExecuteUntil executeUntil = new ExecuteTestUntil(countUntil);
    String id1 = String.format("id-%d", (int) (Math.random() * 10.0));
    String id2 = String.format("id-%d", (int) (Math.random() * 10.0));

    final EchoServer echoServer = world.actorFor(
            EchoServer.class,
            Definition.has(EchoServerTestResultActor.class,
                    Definition.parameters(executeUntil),
                    "arrayQueueMailbox1",
                    id1));

    final EchoClient echoClient = world.actorFor(
            EchoClient.class,
            Definition.has(EchoClientActor.class,
                    Definition.parameters(echoServer),
                    "arrayQueueMailbox2",
                    id2));

    for (int i = 0; i < countUntil; i++) {
      echoClient.doEchoNoCompletes();
    }
    countReceived = executeUntil.awaitUntilComplete();
  }
}