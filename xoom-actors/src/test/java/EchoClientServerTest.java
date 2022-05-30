import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.plugin.PluginProperties;
import io.vlingo.xoom.actors.plugin.completes.PooledCompletesPlugin;
import io.vlingo.xoom.actors.plugin.mailbox.agronampscarrayqueue.ManyToOneConcurrentArrayQueuePlugin;
import io.vlingo.xoom.actors.testkit.TestUntil;
import io.vlingo.xoom.examples.benchmark.actors.EchoClient;
import io.vlingo.xoom.examples.benchmark.actors.EchoClientActor;
import io.vlingo.xoom.examples.benchmark.actors.EchoServer;
import io.vlingo.xoom.examples.benchmark.actors.EchoServerActor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Supplier;

public class EchoClientServerTest {

    private World world;

    @Before
    public void setup() throws IOException {
        world = World.start("testworld");
    }

    @Test
    public void client_response() {
        Supplier<String> addressSupplier = () -> world.addressFactory().uniquePrefixedWith("ea-").idString();

        EchoServer echoServer = world.actorFor(
                EchoServer.class,
                Definition.has(EchoServerActor.class,
                        Definition.parameters(addressSupplier.get()),
                        "arrayQueueMailbox1",
                        "id1"));
        EchoClient echoClient = world.actorFor(
                EchoClient.class,
                Definition.has(EchoClientActor.class, Definition.parameters(echoServer),
                        "arrayQueueMailbox2",
                        "id2"));

        TestUntil testUntil = TestUntil.happenings(1);
        echoClient.doEcho().andThenConsume( s -> testUntil.happened());
        testUntil.completes();
    }
}
