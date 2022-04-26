package io.vlingo.xoom.examples.benchmark.actors;

import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.TestUntil;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@State(Scope.Benchmark)
@Measurement(iterations = 3)
@Warmup(iterations = 1)
public class EchoActorMessageBenchmark {
  private World world;
  private static final int MaxCount = 100_000;
  private int countReceived;

  @Setup
  public void setup() {
    world =  World.start("ArrayQueueBenchmark", getConfiguration());
  }

  private Configuration getConfiguration() {
    return Configuration.define();
  }

  @TearDown
  public void teardown() {
    System.out.println("Closing world-shutdown");
    world.stage().stop();
    world.terminate();
  }


  //@Benchmark
  @Threads(1)
  public void throughputInteger() {
    final TestResults testResults = new TestResults(MaxCount);
    String id = String.format("id-%d", (int) (Math.random() * 10.0));
    final EchoServer echoServer = world.actorFor(
            EchoServer.class,
            Definition.has(EchoServerTestResultActor.class,
                    Definition.parameters(testResults),
                    "arrayQueueMailbox",
                    id));

    for (int i = 0; i < MaxCount; i++) {
      echoServer.echoCount(i);
    }
    countReceived = testResults.awaitUntilComplete();
  }

  //@Benchmark
  @Threads(1)
  public void throughputObject() {
    final TestResults testResults = new TestResults(MaxCount);
    final Object testObj = new Object();
    final String id = String.format("id-%d", (int) (Math.random() * 10.0));
    final EchoServer echoServer = world.actorFor(
            EchoServer.class,
            Definition.has(EchoServerTestResultActor.class,
                    Definition.parameters(testResults),
                    "arrayQueueMailbox",
                    id));

    for (int i = 0; i < MaxCount; i++) {
      echoServer.echoObj(testObj);
    }
    countReceived = testResults.awaitUntilComplete();
  }

  // Need to write more tests to see why some messages are getting dropped or processed twice
  // Test is not passing
  @Benchmark
  @Threads(12)
  public void throughputParallelObject() {
    final ExecuteTestUntil executeTestUntil = new ExecuteTestUntil(MaxCount);
    final Object testObj = new Object();
    final List<EchoServer> echoServerList = IntStream.range(0,100)
            .mapToObj( i -> world.actorFor(
                    EchoServer.class,
                    Definition.has(EchoServerTestResultActor.class, Definition.parameters(executeTestUntil)))
                    ).collect(Collectors.toList());

    for (int i = 0; i < MaxCount; i++) {
      EchoServer echoServer = echoServerList.get( i % 100);
      echoServer.echoObj(testObj);
    }
    executeTestUntil.awaitUntilComplete();
  }
}