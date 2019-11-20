package bokarev;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

//import static bokarev.StoreActor.Msg.GREET;


public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        ActorSystem system = ActorSystem.create("Actor-System");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        MainHttp instance = new MainHttp(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(system).flow(system, materializer);
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








        ActorRef storageActorRef = system.actorOf (StorageActor.props(), "Storage-Actor");


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
    }
}