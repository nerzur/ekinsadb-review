package cu.havanaclub.ekinsadbreview.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {

    private static final String logStorageDir = System.getProperty("user.home") + File.separator + "TagsWithErrorsLogs" + File.separator;


    public static void writeToCsv(List<Searcher> searcherList, String filename, String formattedDate) throws IOException {
        File dir = new File(logStorageDir);
        dir.mkdirs();
        PrintWriter writer = new PrintWriter(logStorageDir + filename + " " + formattedDate + ".csv");
        writer.println("#,Tag,Date,Lote,Weight,Zone");

        for (int i = 0; i < searcherList.size(); i++) {
            Searcher searcher = searcherList.get(i);
            writer.println(i + "," + searcher.toString());
        }
        writer.close();
    }
}
