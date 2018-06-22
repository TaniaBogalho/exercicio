package com.exemplo;

import com.exemplo.rest.JSONService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.Path;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.List;
import java.util.logging.*;

@Path("")
public class WatchService {


    private static Logger LOGGER = Logger.getLogger(JSONService.class.getName());

    private static CSVReader cvs = new CSVReader();

    private JSONService jsonService = new JSONService();


    public static void main(String [] args) {

        new WatchService().CVSFile();

        clean(LOGGER);
    }


    private void CVSFile() {

        File invalid_file = new File("/home/tania/invalid/test_file_invalidos.csv");
        //fileWriter = new FileWriter(invalid_file,true);

        StringBuilder builder = new StringBuilder();
        String ColumnNamesList = "filename, op, value1, value2";


        try
        {
            //region .log file
            FileHandler fileHandler = new FileHandler("/home/tania/logger.log", true);

            LOGGER.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            //endregion

            //Write in .log file
            LOGGER.info("INICIO PROCESSO\n");

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log( Level.SEVERE, e.toString(), e );
        }



        java.nio.file.Path path = Paths.get("/home/tania/input/");

        JSONObject obj_input;
        JSONObject obj_output = null;


        try {
            //Create a WatchService in folder that we intend to monitor
            java.nio.file.WatchService watcher = path.getFileSystem().newWatchService();

            //Associate watch service at the directory to listen to the event types
            WatchKey watchKey = path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

            while(true)
            {

                FileWriter fileWriter = new FileWriter(invalid_file, true);

                try {
                    // listen to events
                    watchKey = watcher.take();
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }


                // get list of events as they occur
                List<WatchEvent<?>> events = watchKey.pollEvents();


                //iterate over events
                for (WatchEvent event : events)
                {

                    //check if the event refers to a new file created
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
                    {
                        //Fill the JSONObject with the function readCsvFile return (read in file)
                        obj_input = cvs.readCsvFile("/home/tania/input/" + event.context().toString());

                        //Verify the operation read in file
                        if(!obj_input.get("op").equals("sum") & !obj_input.get("op").equals("avg") & !obj_input.get("op").equals("mul") & !obj_input.get("op").equals("div") )
                        {
                            //Write the incorrect line of file read in invalid_lines file
                            if (invalid_file.length() <= 1) {
                                builder.append(ColumnNamesList);
                                builder.append("\n");
                                builder.append(event.context().toString());
                                builder.append(",");
                                builder.append(obj_input.get("op"));
                                builder.append(",");
                                builder.append(obj_input.get("value1"));
                                builder.append(",");
                                builder.append(obj_input.get("value2"));
                                //builder.append(",");
                                builder.append('\n');
                                fileWriter.write(builder.toString());
                                fileWriter.close();


                                //Remove the first line in CSV file
                                removeFirstLine(invalid_file.toString());

                            } else {
                                builder = new StringBuilder();
                                builder.append(event.context().toString());
                                builder.append(",");
                                builder.append(obj_input.get("op"));
                                builder.append(",");
                                builder.append(obj_input.get("value1"));
                                builder.append(",");
                                builder.append(obj_input.get("value2"));
                                //builder.append(",");
                                builder.append('\n');
                                fileWriter.write(builder.toString());
                                fileWriter.close();
                            }
                        }

                        else
                        {
                            //Send the JSONObject read in JSONService of Ex. Part 1.
                            obj_output = jsonService.receiveJSON(obj_input);

                            System.out.println(obj_output.toString());
                        }


                        //Move the processed file to folder output
                        try {
                            File source = new File("/home/tania/input/" + event.context().toString());
                            File dest = new File("/home/tania/output/" + event.context().toString());

                            source.renameTo(dest);


                        } catch (Exception e) {
                            e.printStackTrace();
                            LOGGER.log( Level.SEVERE, e.toString(), e );
                        }
                    }
                }

                boolean validKey = watchKey.reset();

                if (! validKey) {
                    System.out.println("Invalid watch key, close the watch service");
                }

                //Write in .log file
                LOGGER.info(" FIM PROCESSO\n\n\n");


                clean(LOGGER);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.toString(), e);

        }


    }

    //Function to remove first line of csv file
    private static void removeFirstLine(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        //Initial write position
        long writePosition = raf.getFilePointer();
        raf.readLine();
        // Shift the next lines upwards.
        long readPosition = raf.getFilePointer();

        byte[] buff = new byte[1024];
        int n;
        while (-1 != (n = raf.read(buff))) {
            raf.seek(writePosition);
            raf.write(buff, 0, n);
            readPosition += n;
            writePosition += n;
            raf.seek(readPosition);
        }
        raf.setLength(writePosition);
        raf.close();
    }


    //Handlers clean function (.log.lck, .log0...x)
    private static void clean(Logger logger) {
        if (logger != null) {
            for (Handler handler : logger.getHandlers()) {
                handler.close();
            }
            clean(logger.getParent());
        }
    }



    //region other things
    /*private java.nio.file.WatchService watcher;

    private void initialize()
    {
        java.nio.file.Path path = Paths.get("/home/tania/input/"); // get the directory which needs to be watched.

        try {

            watcher = path.getFileSystem().newWatchService();

            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);// register the watch service on the path. ENTRY_CREATE-register file create event, ENTRY_DELETE=register the delete event, ENTRY_MODIFY- register the file modified event


        } catch (IOException e) {
            System.out.println("IOException"+ e.getMessage());
        }
    }

    //Once it added to the watch list it will start to monitor the changes on the directory

    private void doMonitor() {

        WatchKey watchKey;

        JSONObject obj_input;
        JSONObject obj_output = new JSONObject();

        while(true) {
            try {
                // listen to events
                watchKey = watcher.take();

                // get list of events as they occur
                List<WatchEvent<?>> events = watchKey.pollEvents();

                //iterate over events
                for (WatchEvent event : events) {
                    //check if the event refers to a new file created
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
                    {
                        //Preenchemos o JSONObject com o que é retornado da função readCsvFile (lido no ficheiro)
                        obj_input = cvs.readCsvFile("/home/tania/input/" + event.context().toString());

                        //Enviamos o objecto lido do ficheiro para o JSONService da Parte 1 e recebemos o resultado.
                        obj_output = jsonService.receiveJSON(obj_input);

                    }
                }

            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
         }
    }

    public static void main(String[] args) {
        WatchService watchservice = new WatchService();
        watchservice.initialize();
        watchservice.doMonitor();
    }
    */
    //endregion
}
