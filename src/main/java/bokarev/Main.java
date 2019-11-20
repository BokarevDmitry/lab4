package bokarev;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

//import static bokarev.StoreActor.Msg.GREET;


public class Main {
    public static void main(String[] args) throws InterruptedException{

        ActorSystem system = ActorSystem.create("test");
        ActorRef storageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");

        //storageActorRef.tell(new RouterActor.TestResult(1), ActorRef.noSender());
        //storageActorRef.tell(new RouterActor.TestResult(7), ActorRef.noSender());
        //storageActorRef.tell(new RouterActor.TestResult(2), ActorRef.noSender());

        //storageActorRef.tell(new RouterActor.getTestsClass(11), ActorRef.noSender());


        Object[] params = {2,1};
        ActorRef testPasserActorRef = system.actorOf(TestPasserActor.props(), "TestPasser-Actor");
        testPasserActorRef.tell(new TestPasserActor.Test(
                11, "var divideFn = function(a,b) {return a/b}",
                "divideFn", "test1", 2.0, params), storageActorRef);


        Thread.sleep(1000);
        storageActorRef.tell(new StorageActor.getTestsClass(11), ActorRef.noSender());
    }
}