package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

//import static bokarev.StoreActor.Msg.GREET;
import static com.sun.org.apache.xml.internal.serialize.Method.TEXT;

public class Main {
    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("test");
        ActorRef storageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");

        //storageActorRef.tell(new StorageActor.TestResult(1), ActorRef.noSender());
        //storageActorRef.tell(new StorageActor.TestResult(7), ActorRef.noSender());
        //storageActorRef.tell(new StorageActor.TestResult(2), ActorRef.noSender());

        //storageActorRef.tell(new StorageActor.getTestsClass(11), ActorRef.noSender());


        Object[] params = {2,1};
        ActorRef testPasserActorRef = system.actorOf(TestPasserActor.props(), "TestPasser-Actor");
        testPasserActorRef.tell(new TestPasserActor.Test(
                11, "var divideFn = function(a,b) {return a/b}",
                "divideFn", "test1", 2.0, params), storageActorRef);


        Thread.sleep(
        storageActorRef.tell(new StorageActor.getTestsClass(11), ActorRef.noSender());
    }
}