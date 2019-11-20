package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;


public class StorageActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ArrayList<Integer> testResults;

    public StorageActor() {
        this.testResults = new ArrayList<>();
    }

    public static Props props() {
        return Props.create(StorageActor.class);
    }

    public static final class TestResultClass {
        int testResult;

        public TestResultClass(int testResult) {
            this.testResult = testResult;
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
        log.info("Starting StorageActor {}", this);
    }

    @Override
    public void postStop() {
        log.info("Stopping StorageActor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TestResultClass.class, r -> {
                    log.info("Received test result message");
                    this.testResults.add(r.testResult);
                })

                .match(getTestsClass.class, r -> {
                    log.info("Received Get Test Request message for package " + r.packageID);
                    log.info("Test results: " + this.testResults);
                })

                .build();
    }
}
