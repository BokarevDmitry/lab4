package bokarev;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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

    public static class Test {
        Integer packageID;
        String jsScript, functionName, testName;
        Float expectedResult;
        ArrayList<Integer> args;


        public Test(Integer packageID, String jsScript, String functionName, String testName, Float expectedResult, ArrayList<Integer> args) {
            this.packageID = packageID;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.args = args;
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
                .match(Test.class, r -> {
                    log.info("Received test message");
                    String result = invoke(r);

                })

                .match(StorageActor.getTestsClass.class, r -> {
                    log.info("Received Get Test Request message for package " + r.packageID);
                    log.info("Test results: " + this.testResults);
                })

                .build();
    }

    public static String invoke(Test a) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(a.jsScript);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(a.functionName, a.args).toString();
    }
}

