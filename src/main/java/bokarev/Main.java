package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.concurrent.CompletableFuture;

//import static bokarev.StoreActor.Msg.GREET;
import static com.sun.org.apache.xml.internal.serialize.Method.TEXT;

public class Main {
    public static void main(String[] args) {
        //System.out.println("test");

        ActorSystem system = ActorSystem.create("test");

        /*ActorRef readingActor = system.actorOf(
                ReadingActor.props(TEXT),
                "reading-Actor"
        );*/


       // readingActor.tell("hey", ActorRef.noSender());

       //ActorRef WordCounterActorRef = system.actorOf(Props.create(WordCounterActor.class), "WordCounter-Actor");

       //WordCounterActorRef.tell(new WordCounterActor.CountWords("ole ola"), readingActor);

       ActorRef StorageActorRef = system.actorOf (StorageActor.props(TEXT), "Storage-Actor");

       StorageActorRef.tell(new StorageActor.TestResultClass(1), ActorRef.noSender());
       StorageActorRef.tell(new StorageActor.getTestsClass(11), ActorRef.noSender());

       //readingActor.tell(new PrinterActor);

        //readingActor.tell(new ReadingActor("a").Readlines(),
        //        ActorRef.noSender());

       // CompletableFuture<Object> future = ast
    }
}