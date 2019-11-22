package bokarev;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.*;


public class StorageActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Map<Integer, MainHttp.TestPackage> testResults;

    public StorageActor() {
        this.testResults = new HashMap<Integer, MainHttp.TestPackage>();
    }

    public static Props props() {
        return Props.create(StorageActor.class);
    }

    static final class TestResult {
        String testName;
        Double expectedResult;
        Object[] params;
        Boolean testResult;

        TestResult(String testName, Double expectedResult, Object[] params, Boolean testResult) {
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
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
                .match(MainHttp.TestForImpl.class, test -> {
                    log.info("Received test result message");

                    if (this.testResults.get(test.packageId)) {

                    }


                    this.testResults.put(test.packageId, test)
                })

                .match(getTestsClass.class, r -> {
                    log.info("Received Get Test Request message for package " + r.packageID);
                    log.info("Test results: " + this.testResults.get(0).testResult);
                })

                .build();
    }
}