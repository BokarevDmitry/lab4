package bokarev;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import static bokarev.StoreActor.Msg.GREET;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test");
        ActorRef storeActor = system.actorOf(
                Props.create(StoreActor.class)
        );



        storeActor.tell (
                new StoreActor.Msg(GREET),
                ActorRef.noSender()
        );
    }
}
