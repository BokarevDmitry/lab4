package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;


public class TestPasserActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ArrayList<Integer> testResults;

    public TestPasserActor() {
        this.testResults = new ArrayList<>();
    }

    public static Props props() {
        return Props.create(TestPasserActor.class);
    }

    public static final class Test {
        Integer packageID;
        String jsScript;
        String functionName;
        

        public Test(int testResult) {
            this.testResult = testResult;
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

