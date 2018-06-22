package com.exemplo.rest;

import com.exemplo.CSVReader;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

//import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONServiceTest {

    JSONService jsonService = new JSONService();

    private static CSVReader cvs = new CSVReader();

    @Test
    void testReceiveJSON () throws IOException, JSONException
    {

        JSONObject obj_a_testar = new JSONObject()
                    .put("op", "sum")
                    .put("value1", 10)
                    .put("value2", 5);


        JSONObject obj = jsonService.receiveJSON(obj_a_testar);

        String atual = obj.toString();
        String expected = "{\"op\":\"sum\",\"value1\":10,\"value2\":5,\"Total\":15,\"Data\":\"22\\/06\\/2018\"}";

        assertEquals(expected, atual);
    }




    @Test
    void testCVSFile() throws Exception
    {
        java.nio.file.Path path = Paths.get("/home/tania/input/");

        java.nio.file.WatchService watcher = path.getFileSystem().newWatchService();
        WatchKey watchKey = path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);



        // get list of events as they occur
        List<WatchEvent<?>> events = watchKey.pollEvents();


        //iterate over events
        for (WatchEvent event : events) {
            assertThat(event.kind() == StandardWatchEventKinds.ENTRY_CREATE, is(true));
        }




        JSONObject obj_input = cvs.readCsvFile("");

        JSONObject obj_output = jsonService.receiveJSON(obj_input);

        String atual = obj_output.toString();
        String expected = "";

        String op = obj_output.getString("op");

        if(op.equals("sum"))
        {
            expected = "{\"op\":\"sum\",\"value1\":5,\"value2\":2,\"Total\":7,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("avg"))
        {
            expected = "{\"op\":\"avg\",\"value1\":10,\"value2\":5,\"Total\":7.5,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("mul"))
        {
            expected = "{\"op\":\"mul\",\"value1\":2,\"value2\":2,\"Total\":4,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("div"))
        {
            expected = "{\"op\":\"div\",\"value1\":10,\"value2\":5,\"Total\":2,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }
    }


   /*

    //Função que testa a função de ler os dados através do URL
    @Test
    void testCVSFileWEB() throws JSONException, IOException {
        JSONObject obj = js.CVSFileWEB("test_file.csv");

        String atual = obj.toString();
        String expected = "";

        String op = obj.getString("op");

        if(op.equals("sum"))
        {
            expected = "{\"op\":\"sum\",\"value1\":5,\"value2\":2,\"Total\":7,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("avg"))
        {
            expected = "{\"op\":\"avg\",\"value1\":10,\"value2\":5,\"Total\":7.5,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("mul"))
        {
            expected = "{\"op\":\"mul\",\"value1\":2,\"value2\":2,\"Total\":4,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("div"))
        {
            expected = "{\"op\":\"div\",\"value1\":10,\"value2\":0,\"Total\":0,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

    }

    //Função que testa a função de ler os dados através do ficheiro quando este é colocado na pasta INPUT
    @Test
    void testCVSFile() throws Exception
    {
        JSONObject obj = js.CVSFile();

        String atual = obj.toString();
        String expected = "";

        String op = obj.getString("op");

        if(op.equals("sum"))
        {
            expected = "{\"op\":\"sum\",\"value1\":5,\"value2\":2,\"Total\":7,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("avg"))
        {
            expected = "{\"op\":\"avg\",\"value1\":10,\"value2\":5,\"Total\":7.5,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("mul"))
        {
            expected = "{\"op\":\"mul\",\"value1\":2,\"value2\":2,\"Total\":4,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }

        else if(op.equals("div"))
        {
            expected = "{\"op\":\"div\",\"value1\":10,\"value2\":5,\"Total\":2,\"Data\":\"20\\/06\\/2018\"}";

            assertEquals(expected, atual);
        }
    }*/
}