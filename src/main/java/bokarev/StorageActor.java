package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;

public class StorageActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Map<Integer, Classes.TestPackage> testResults;

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
                .match(Classes.TestForImpl.class, test -> {
                    log.info("REQUEST: store test results of package - " + test.packageId);
                    if (this.testResults.containsKey(test.packageId)) {
                        this.testResults.get(test.packageId).testsLists.add(test.oneTest);
                    } else {
                        Classes.TestPackage testPackage = new Classes.TestPackage(test);
                        this.testResults.put(test.packageId, testPackage);
                    }
                })
                .match(Classes.TestGetter.class, r -> {
                    log.info("REQUEST: tests for package - " + r.packageId);
                    if (this.testResults.containsKey(r.packageId)) {
                        getSender().tell(this.testResults.get(r.packageId), getSelf());
                    } else {
                        getSender().tell("RESPONSE: no tests with this packageId", ActorRef.noSender());
                    }
                })

                .build();
    }
}