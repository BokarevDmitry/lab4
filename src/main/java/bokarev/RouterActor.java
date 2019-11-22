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
        system.actorOf (StorageActor.props(), "Storage-Actor");
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
        ActorRef storageActor = getContext().getSystem().actorFor("akka://routes/user/Storage-Actor");

        return receiveBuilder()
                .match(MainHttp.TestPackage.class, test -> {
                    log.info("NEW TEST PACKAGE");
                    int count = test.testsLists.size();
                    for (int i=0; i<count; i++) {
                        ActorRef testPasserActor = getContext().actorOf(TestPasserActor.props(), "TestPasser-Actor"+i);
                        testPasserActor.tell(new MainHttp.TestForImpl(test, i), storageActor);
                    }
                })

                .build();
    }
}