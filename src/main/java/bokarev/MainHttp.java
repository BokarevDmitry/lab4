package bokarev;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;



public class MainHttp extends AllDirectives {
    public static void main(String[] args) throws Exception, InterruptedException, IOException {

        ActorSystem system = ActorSystem.create("routes");
        ActorRef routerActor = system.actorOf(RouterActor.props(system), "Router-Actor");
        ActorRef storageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");

        
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
                /*path("get", () ->
                        route(
                                get( () -> {
                                    Future<Object> result = Patterns.ask(testPackageActor,
                                            SemaphoreActor.makeRequest(), 5000);
                                    return completeOKWithFuture(result, Jackson.marshaller());
                                }))),*/
                path("get", () ->
                        route(
                                get(() -> {
                                    return complete("Good");
                                }))),
                path("post", () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackage.class), test -> {
                                            routerActor.tell(test, );
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
    }

    static class OneTest {
        final String testName;
        final Double expectedResult;
        final Object[] params;

        @JsonCreator
        OneTest(@JsonProperty("testName") String testName,
                @JsonProperty("expectedResult") Double expectedResult,
                @JsonProperty("params") Object[] params) {
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
        }
    }

    public static class TestForImpl {
        final Integer packageId;
        final String jsScript, functionName;

        final String testName;
        final Double expectedResult;
        final Object[] params;

        testForImpl(TestPackage test, int indexOfTest) {
            this.packageId = test.packageId;
            this.jsScript = test.jsScript;
            this.functionName = test.functionName;

            this.testName = test.testsLists.get(indexOfTest).testName;
            this.expectedResult = test.testsLists.get(indexOfTest).expectedResult;
            this.params = test.testsLists.get(indexOfTest).params;
        }
    }
}