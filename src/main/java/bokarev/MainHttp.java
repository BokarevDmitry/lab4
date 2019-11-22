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
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
//import scala.compat.java8.OptionConverters;


//import static bokarev.StoreActor.Msg.GREET;


public class MainHttp extends AllDirectives {
    public static void main(String[] args) throws Exception, InterruptedException, IOException {

        ActorSystem system = ActorSystem.create("routes");
        ActorRef routerActor = system.actorOf(RouterActor.props(system), "Router-Actor");

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






        /*ActorRef storageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");


        //storageActorRef.tell(new RouterActor.TestResult(1), ActorRef.noSender());
        //storageActorRef.tell(new RouterActor.TestResult(7), ActorRef.noSender());
        //storageActorRef.tell(new RouterActor.TestResult(2), ActorRef.noSender());

        //storageActorRef.tell(new RouterActor.getTestsClass(11), ActorRef.noSender());


        Object[] params = {2,1};
        ActorRef testPasserActorRef = system.actorOf(TestPasserActor.props(), "TestPasser-Actor");
        testPasserActorRef.tell(new TestPasserActor.Test(
                11, "var divideFn = function(a,b) {return a/b}",
                "divideFn", "test1", 2.0, params), storageActorRef);


        Thread.sleep(1000);
        storageActorRef.tell(new StorageActor.getTestsClass(11), ActorRef.noSender());
        */
    }


    /*private Route createRoute(ActorRef routerActor) {

        return route(
                path("r", () ->
                        get(() ->
                        {
                            routerActor.tell(new RouterActor.TestResult(), ActorRef.noSender());
                            return complete("sent to router-actor");
                        })));
    }*/
    private Route createRoute(ActorSystem system) {
        return route(
                /*path("get", () ->
                        route(
                                get( () -> {
                                    Future<Object> result = Patterns.ask(testPackageActor,
                                            SemaphoreActor.makeRequest(), 5000);
                                    return completeOKWithFuture(result, Jackson.marshaller());
                                }))),*/
                path("post", () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackageMsg.class), msg -> {
                                            testPackageActor.tell(msg, ActorRef.noSender());
                                            return complete("Test started!");
                                        })))));
    }

    private static class TestsList {
        final String testName;
        final Double expectedResult;
        final Object[] params;

        @JsonCreator
        TestsList(@JsonProperty("testName") String testName,
                  @JsonProperty("expectedResult") Double expectedResult,
                  @JsonProperty("params") Object[] params) {
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
        }
    }

    private static class Tests {
        final Integer packageID;
        final String jsScript, functionName;

        @JsonCreator
        Tests(@JsonProperty("packageID"))


        public Tests(Integer packageID, String jsScript, String functionName, String testName, Double expectedResult, Object[] params) {
            this.packageID = packageID;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
        }
    }
}