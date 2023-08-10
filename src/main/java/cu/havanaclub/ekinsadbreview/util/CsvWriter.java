package cu.havanaclub.ekinsadbreview.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {

    public static void writeToCsv(List<Searcher> searcherList, String header, String filename, String formattedDate) throws IOException {
        PrintWriter writer = new PrintWriter(filename + " " + formattedDate + ".csv");
        writer.println(header);

        for (Searcher searcher : searcherList) {
            writer.println(searcher.toString());
        }
        writer.close();
    }
}
