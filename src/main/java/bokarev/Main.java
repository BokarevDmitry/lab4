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
        ActorRef StorageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");

        //StorageActorRef.tell(new StorageActor.TestResultClass(1), ActorRef.noSender());
        //StorageActorRef.tell(new StorageActor.TestResultClass(7), ActorRef.noSender());
        //StorageActorRef.tell(new StorageActor.TestResultClass(2), ActorRef.noSender());

        //StorageActorRef.tell(new StorageActor.getTestsClass(11), ActorRef.noSender());


        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        ActorRef TestPasserActorRef = system.actorOf(TestPasserActor.props(), "TestPasser-Actor");
        TestPasserActorRef.tell(new TestPasserActor.Test(
                11, "var divideFn = function(a,b) { return a/b} ",
                "divideFn", "test1", 2.0, arr), ActorRef.noSender());
    }
}