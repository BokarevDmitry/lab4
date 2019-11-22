package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import javax.script.*;

public class TestPasserActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public TestPasserActor() {
    }

    public static Props props() {
        return Props.create(TestPasserActor.class);
    }

    @Override
    public void preStart() {
        log.info("Starting TestPasserActor {}", this);
    }

    @Override
    public void postStop() {
        log.info("Stopping TestPasserActor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MainHttp.OneTest.class, r -> {
                    log.info("Received test message");
                    Boolean res = (Double.parseDouble(invoke(r)) == r.testsLists.get(0).expectedResult);
                    log.info("RESULT: " + res);
                    getSender().tell(new StorageActor.TestResult(r.testsLists.get(0).testName, r.testsLists.get(0).expectedResult, r.testsLists.get(0).params, res), ActorRef.noSender());
                })

                .build();
    }

    private String invoke(MainHttp.OneTest r) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(r.jsScript);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(r.functionName, r.testsLists.get(0).params).toString();
    }
}

