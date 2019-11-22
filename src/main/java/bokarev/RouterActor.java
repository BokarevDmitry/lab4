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


    public RouterActor(ActorSystem system) {
        ActorRef storageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");
        //ActorRef testPasserAcrorRef = system.actorOf(TestPasserActor.props(), "TestPasser-Actor");
    }


    public static Props props(ActorSystem system) {
        return Props.create(RouterActor.class, system);
    }

    static final class TestResult {
        TestResult() {
        }
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
                .match(MainHttp.TestPackage.class, test -> {
                    log.info("Received test message");
                    int count = test.testsLists.size();
                    for (int i=0; i<count; i++) {
                        ActorRef testPasserActor = getContext().actorOf(TestPasserActor.props(), "TestPasser-Actor");
                        testPasserActor.tell(test.testsLists.get(i), );
                    }
                })

                .build();
    }
}