package bokarev;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;


public class RouterActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public RouterActor() {

    }

    public static Props props() {
        return Props.create(RouterActor.class);
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
                .match(TestResult.class, r -> {
                    log.info("Received test result message");
                })

                .build();
    }
}