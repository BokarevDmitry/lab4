package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;


public class RouterActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public ActorRef storageActor;

    public RouterActor(ActorSystem system) {
       storageActor = system.actorOf (StorageActor.props(), "Storage-Actor");
    }

    public static Props props(ActorSystem system) {
        return Props.create(RouterActor.class, system);
    }

    @Override
    public void preStart() {
        log.info("Starting RouterActor {}", this);
    }

    @Override
    public void postStop() {
        log.info("Stopping RouterActor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Classes.TestPackage.class, test -> {
                    log.info("NEW TEST PACKAGE");
                    int count = test.testsLists.size();
                    for (int i=0; i<count; i++) {
                        ActorRef testPasserActor = getContext().actorOf(TestPasserActor.props(), "TestPasser-Actor"+i);
                        testPasserActor.tell(new Classes.TestForImpl(test, i), storageActor);
                    }
                })
                .match(Classes.TestGetter.class, msg -> {
                    storageActor.tell(msg, getSender());
                })
                .build();
    }
}