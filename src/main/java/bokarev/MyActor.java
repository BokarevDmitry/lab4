package bokarev;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import java.util.HashMap;
import java.util.Map;

/*public class StoreActor extends AbstractActor {
    private Map<String, String> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreMessage.class, m -> {
                    store.put(m.getKey(), m.getValue());
                    System.out.println("received msg " + m.toString());
                })
                .match(GetMessage.class, req -> sender().tell(
                        new StoreMessage(req.getKey(), store.get(req.getKey())), self())
                ).build();
    }
}*/


public class StoreActor extends AbstractActor {
    public static enum Msg {
        GREET, DONE
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Msg.GREET, m -> {
                    System.out.println("Hello World!");
                    sender().tell(Msg.DONE, self());
                })
                .build();
    }
}


