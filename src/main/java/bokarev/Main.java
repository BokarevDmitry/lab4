package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.concurrent.CompletableFuture;

import static bokarev.StoreActor.Msg.GREET;
import static com.sun.org.apache.xml.internal.serialize.Method.TEXT;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test");

        ActorRef readingActor = system.actorOf(
                ReadingActor.props(TEXT),
                "reading-Actor"
        );

        readingActor.tell(new ReadingActor().Readlines(),
                ActorRef.noSender());

        CompletableFuture<Object> future = ast
    }
}