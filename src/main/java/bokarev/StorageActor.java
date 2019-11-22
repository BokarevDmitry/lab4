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
        this.testResults = new HashMap<>();
    }

    public static Props props() {
        return Props.create(StorageActor.class);
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

                    if (this.testResults.containsKey(test.packageId)) {
                        this.testResults.get(test.packageId).testsLists.add(test.oneTest);
                    } else {
                        MainHttp.TestPackage testPackage = new MainHttp.TestPackage(test);
                        this.testResults.put(test.packageId, testPackage);
                    }
                })

                .match(MainHttp.TestGetter.class, r -> {
                    log.info("Received Get Test Request message for package " + r.packageID);
                    //log.info("Test results: " + this.testResults.get(0).testResult);
                    getSender().tell(this.testResults.get(11));
                })

                .build();
    }
}