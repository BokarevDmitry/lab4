package bokarev;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import javax.script.*;
import java.util.ArrayList;


public class TestPasserActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public TestPasserActor() {
    }

    public static Props props() {
        return Props.create(TestPasserActor.class);
    }

    public static class Test {
        Integer packageID;
        String jsScript, functionName, testName;
        Double expectedResult;
        Object[] params;

        public Test(Integer packageID, String jsScript, String functionName, String testName, Double expectedResult, Object[] params) {
            this.packageID = packageID;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
        }
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
                .match(Test.class, r -> {
                    log.info("Received test message");
                    String result = invoke(r);
                    log.info("RESULT: " + (Double.parseDouble(result) == r.expectedResult));

                })

                .match(StorageActor.getTestsClass.class, r -> {
                    log.info("Received Get Test Request message for package " + r.packageID);
                })

                .build();
    }

    private String invoke(Test r) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(r.jsScript);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(r.functionName, r.params).toString();
    }
}

