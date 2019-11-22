package bokarev;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
//import akka.compat.Future;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import scala.concurrent.Future;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;



public class MainHttp extends AllDirectives {
    public static void main(String[] args) throws Exception, InterruptedException, IOException {

        ActorSystem system = ActorSystem.create("routes");
        ActorRef routerActor = system.actorOf(RouterActor.props(system), "Router-Actor");



        //ActorRef testPasserActorRef = system.actorOf(TestPasserActor.props(), "TestPasser-Actor");


        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        MainHttp instance = new MainHttp();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(routerActor).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer
        );

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }

    private Route createRoute(ActorRef routerActor) {
        return route(
                path("get", () ->
                        route(
                                get(() -> {
                                    Future<Object> future = Patterns.ask(routerActor, new MainHttp.TestGetter(11), 5000);
                                    return completeOKWithFuture(future, Jackson.marshaller());
                                }))),
                path("post", () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackage.class), test -> {
                                            routerActor.tell(test, ActorRef.noSender());
                                            return complete("Test started!");
                                        })))));
    }

    public static class TestPackage {
        final Integer packageId;
        final String jsScript, functionName;
        final ArrayList<OneTest> testsLists;

        @JsonCreator
        TestPackage(
              @JsonProperty("packageId") Integer packageId,
              @JsonProperty("jsScript") String jsScript,
              @JsonProperty("functionName") String functionName,
              @JsonProperty("tests") ArrayList<OneTest> testsLists) {
            this.packageId = packageId;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.testsLists = testsLists;
        }

        TestPackage(TestForImpl test) {
            this.packageId = test.packageId;
            this.jsScript = test.jsScript;
            this.functionName = test.functionName;
            this.testsLists = new ArrayList<>();
            this.testsLists.add(test.oneTest);
        }
    }

    static class OneTest {
        String testName;
        Double expectedResult;
        Object[] params;

        Boolean result;

        @JsonCreator
        OneTest(@JsonProperty("testName") String testName,
                @JsonProperty("expectedResult") Double expectedResult,
                @JsonProperty("params") Object[] params,
                @JsonProperty("result") Boolean result) {
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
            this.result = null;
        }
    }

    public static class TestForImpl {
        final Integer packageId;
        final String jsScript, functionName;
        OneTest oneTest;

        TestForImpl(TestPackage test, int indexOfTest) {
            this.packageId = test.packageId;
            this.jsScript = test.jsScript;
            this.functionName = test.functionName;

            this.oneTest = new OneTest(
                    test.testsLists.get(indexOfTest).testName,
                    test.testsLists.get(indexOfTest).expectedResult,
                    test.testsLists.get(indexOfTest).params);
        }

        void setResult (Boolean result){
            this.oneTest.result = result;
        }
    }

    public static final class TestGetter {
        int packageID;

        public TestGetter(int packageID) {
            this.packageID = packageID;
        }
    }
}