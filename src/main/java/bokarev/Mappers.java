package bokarev;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class Mappers {

    public static JavaPairRDD<String,String> mapAirports (JavaRDD<String> airportsRDD) {
        return airportsRDD
                .mapToPair(
                (String s) -> {
                    String airportsInfo[] = ParserTools.parseAirports(s);
                    return new Tuple2<>(
                            ParserTools.getAirportCode(airportsInfo),
                            ParserTools.getAirportName(airportsInfo));
                }
        );
    }

    public static JavaPairRDD<Tuple2, Tuple2> mapFlights (JavaRDD<String> flightsRDD, Broadcast<Map<String, String>> airportsBroadcasted) {
        return flightsRDD
                .mapToPair(
                        (String s) -> {
                            String[] flightsInfo = ParserTools.parseFlights(s);
                            String airportOrigin = ParserTools.getAirportOrigin(flightsInfo);
                            String airportDest   = ParserTools.getAirportDest(flightsInfo);
                            String timeDelay     = ParserTools.getDelayTime(flightsInfo);
                            Float cancelStatus   = ParserTools.getCancelStatus(flightsInfo);

                            return new Tuple2<>(new Tuple2<>(
                                    airportsBroadcasted.value().get(airportOrigin),
                                    airportsBroadcasted.value().get(airportDest)),
                                    new FlightData(timeDelay, cancelStatus));
                        }
                )
                .reduceByKey(
                        (FlightData a, FlightData b) -> new FlightData(
                                Math.max(a.getTimeDelay(), b.getTimeDelay()),
                                a.getCountRecords()+b.getCountRecords(),
                                a.getCountDelayOrCancel() + b.getCountDelayOrCancel()))
                .mapToPair(
                        (Tuple2<Tuple2<String,String>, FlightData> a) -> new Tuple2<>(
                                a._1,
                                new Tuple2<>(a._2.getTimeDelay(), a._2.getPercent())));
    }
}