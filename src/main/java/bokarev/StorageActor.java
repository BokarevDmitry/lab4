package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class StorageActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private String text;

    public StorageActor(String text) {
        this.text = text;
    }

    public static Props props(String text) {
        return Props.create(StorageActor.class, text);
    }

    public static final class TestResultClass {
        int count;

        public TestResultClass(int count) {
            this.count = count;
        }
    }

    public static final class getTestsClass {
        int packageID;

        public getTestsClass(int packageID) {
            this.packageID = packageID;
        }
    }

    @Override
    public void preStart() {
        log.info("Starting ReadingActor {}", this);
    }

    @Override
    public void postStop() {
        log.info("Stopping ReadingActor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TestResultClass.class, r -> {
                    log.info("Received test result message");
                })

                .match(getTestsClass.class, r -> {
                    log.info("Received Get Test Request message for package ", r.packageID);
                })

                .build();
    }
}
