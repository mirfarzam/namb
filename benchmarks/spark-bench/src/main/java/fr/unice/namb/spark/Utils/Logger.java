package fr.unice.namb.spark.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    static FileWriter fstream = null;  //true tells to append data.
    static BufferedWriter out;
    static File file;


    public static void start() throws IOException {
         file = new File("logfile" + System.currentTimeMillis() + ".txt");

        if (!file.exists()) {
            file.createNewFile();
        }

        fstream = new FileWriter(file, true); //true tells to append data.
        out = new BufferedWriter(fstream);
    }

    public static void debug(String log) throws IOException {
        out.write("\n"+log);
        out.flush();
    }
}
