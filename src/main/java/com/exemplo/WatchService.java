package com.exemplo;

import com.exemplo.rest.JSONService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.Path;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.List;
import java.util.logging.*;

@Path("")
public class WatchService {


    FileWriter fileWriter;

    private static Logger LOGGER = Logger.getLogger(JSONService.class.getName());

    FileHandler fileHandler;

    static CSVReader cvs = new CSVReader();

    JSONService jsonService = new JSONService();




    public static void main(String [] args) throws IOException {

        new WatchService().CVSFile();

        clean(LOGGER);
    }


    public void CVSFile() throws IOException {

        File invalid_file = new File("/home/tania/invalid/test_file_invalidos.csv");
        //fileWriter = new FileWriter(invalid_file,true);

        StringBuilder builder = new StringBuilder();
        String ColumnNamesList = "filename, op, value1, value2";


        try
        {
            //region ficheiro .log
            fileHandler = new FileHandler("/home/tania/logger.log", true);

            LOGGER.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            //endregion

            //Escrevemos no ficheiro .log
            LOGGER.info("INICIO PROCESSO\n");

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log( Level.SEVERE, e.toString(), e );
        }



        java.nio.file.Path path = Paths.get("/home/tania/input/");

        JSONObject obj_input;
        JSONObject obj_output = null;


        try {
            //Criamos um WatchService na diretoria que pretendemos observar
            java.nio.file.WatchService watcher = path.getFileSystem().newWatchService();

            // associate watch service with the directory to listen to the event types
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

            INFINITE_WHILE_LOOP:
            while(true)
            {

                fileWriter = new FileWriter(invalid_file, true);

                WatchKey watchKey;

                try {
                    // listen to events
                    watchKey = watcher.take();
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                    break INFINITE_WHILE_LOOP;
                }
                // get list of events as they occur
                List<WatchEvent<?>> events = watchKey.pollEvents();

                //iterate over events
                for (WatchEvent event : events) {
                    //check if the event refers to a new file created
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {

                        //Preenchemos o JSONObject com o que é retornado da função readCsvFile (lido no ficheiro)
                        obj_input = cvs.readCsvFile("/home/tania/input/" + event.context().toString());


                        if(!obj_input.get("op").equals("sum") & !obj_input.get("op").equals("avg") & !obj_input.get("op").equals("mul") & !obj_input.get("op").equals("div") )
                        {
                            //Escrevemos no ficheiro para linhas inválidas
                            if (invalid_file.length() <= 1) {
                                builder.append(ColumnNamesList + "\n");
                                builder.append(event.context().toString() + ",");
                                builder.append(obj_input.get("op") + ",");
                                builder.append(obj_input.get("value1") + ",");
                                builder.append(obj_input.get("value2") + ",");
                                builder.append('\n');
                                fileWriter.write(builder.toString());
                                fileWriter.close();


                            } else {
                                builder.append(event.context().toString() + ",");
                                builder.append(obj_input.get("op") + ",");
                                builder.append(obj_input.get("value1") + ",");
                                builder.append(obj_input.get("value2") + ",");
                                builder.append('\n');
                                fileWriter.write(builder.toString());
                                fileWriter.close();

                            }
                        }

                        else
                        {
                            //Enviamos o objecto lido do ficheiro para o JSONService da Parte 1.
                            jsonService.receiveJSON(obj_input);
                        }


                        //Movemos o ficheiro tratado para a pasta output
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
                    break INFINITE_WHILE_LOOP;
                }

                //Escrevemos no ficheiro .log
                LOGGER.info(" FIM PROCESSO\n\n\n");


                //Chama a função clean para limpar os ficheiros .log.lck E .log0...x da diretoria
                clean(LOGGER);
            }

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.toString(), e);

        } catch (JSONException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }



    }


    //Função que limpa os Handlers (.log.lck, .log0...x)
    private static void clean(Logger logger) {
        if (logger != null) {
            for (Handler handler : logger.getHandlers()) {
                handler.close();
            }
            clean(logger.getParent());
        }
    }



    //region outras coisas
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
