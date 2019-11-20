package bokarev;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class AirportApp {
    public static void main(String[] args) throws Exception {

        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(config);
        if (hdfs.exists(new Path("hdfs://localhost:9000/user/dima/output")))
            hdfs.delete(new Path("hdfs://localhost:9000/user/dima/output"), true);

        SparkConf conf = new SparkConf().setAppName("AirportApp");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flightsRDD = sc.textFile("/user/dima/664600583_T_ONTIME_sample.csv");
        JavaRDD<String> airportsRDD = sc.textFile("/user/dima/L_AIRPORT_ID.csv");
        flightsRDD = ParserTools.removeHeaders(flightsRDD);
        airportsRDD = ParserTools.removeHeaders(airportsRDD);

        JavaPairRDD<String,String> airportDict = Mappers.mapAirports(airportsRDD);
        final Broadcast<Map<String, String>> airportsBroadcasted = sc.broadcast(airportDict.collectAsMap());
        JavaPairRDD<Tuple2, Tuple2> flightsInfo = Mappers.mapFlights(flightsRDD, airportsBroadcasted);

        flightsInfo.saveAsTextFile("/user/dima/output");
    }
}