package bokarev;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    ActorSystem system = ActorSystem.create("test");
    ActorRef storeActor = system.actorOf(
            Props.create(StoreActor.class)
    );

    

    storeActor.tell (
            new StoreActor.Msg("test", "test"),
            ActorRef.noSender()
    );

}
