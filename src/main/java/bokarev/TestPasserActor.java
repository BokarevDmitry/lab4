package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
;

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
        log.info("Starting TestPasserActor {}", this.getSelf());
    }

    @Override
    public void postStop() {
        log.info("Stopping TestPasserActor {}", this.getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MainHttp.TestForImpl.class, test -> {
                    Boolean res = Double.parseDouble(invoke(test)) == test.oneTest.expectedResult;
                    test.setResult(res);
                    log.info("TEST IS DONE, RESULT: " + res);
                    getSender().tell(test, ActorRef.noSender());
                    getContext().stop(getSelf());
                })

                .build();
    }

    private String invoke(MainHttp.TestForImpl r) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(r.jsScript);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(r.functionName, r.oneTest.params).toString();
    }
}

