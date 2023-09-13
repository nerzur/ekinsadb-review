package cu.havanaclub.ekinsadbreview.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Esta clase contiene las funcionalidades necesarias para crear un fichero con extensión <em>.csv</em> con la información
 * contenida en una lista de objetos {@link Searcher}. Este fichero es almacenado en la carpeta del usuario actual en una
 * carpeta llamada <em>TagsWithErrorsLogs</em> y contiene la fecha y hora del sistema.
 *
 * @author Nerzur
 */
public class CsvWriter {

    /**
     * Contiene la dirección donde se almacenarán los ficheros con formato <em>.csv</em>
     */
    private static final String logStorageDir = System.getProperty("user.home") + File.separator + "TagsWithErrorsLogs" + File.separator;

    /**
     * @param searcherList Lista de {@link Searcher} que contiene la información de los registros con error que será guardada
     *                     en el <em>.csv</em>
     * @param filename Nombre del fichero.
     * @param formattedDate Fecha formateada que será incluida en el nombre del fichero <em>.csv</em>.
     * @throws IOException Esta excepción es lanzada cuando existen errores durante el proceso de guardado del fichero.
     */
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
