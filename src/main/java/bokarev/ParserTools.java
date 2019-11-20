package bokarev;

import org.apache.spark.api.java.JavaRDD;

public class ParserTools {
    private static String comma = ",";
    private static String commaInQuotes = "\",\"";
    private static String quote = "\"";
    private static String empty = "";

    public static String[] parseFlights(String s) {return s.split(comma);}
    public static String[] parseAirports (String s) { return s.split(commaInQuotes);}

    private static String removeQuotes (String s) {return s.replaceAll(quote, empty);}

    public static String getAirportCode(String[] airportsData) {return ParserTools.removeQuotes(airportsData[0]);}
    public static String getAirportName (String[] airportsData) { return ParserTools.removeQuotes(airportsData[1]);}

    public static String getAirportOrigin (String[] flightsData) {return flightsData[11];}
    public static String getAirportDest (String[] flightsData) {return flightsData[14];}
    public static String getDelayTime (String[] flightsData) {return flightsData[18];}
    public static Float  getCancelStatus (String[] flightsData) {return Float.parseFloat(flightsData[19]);}

    public static JavaRDD<String> removeHeaders (JavaRDD<String> a) {
        String header = a.first();
        return a.filter(row -> !row.equals(header));
    }
}