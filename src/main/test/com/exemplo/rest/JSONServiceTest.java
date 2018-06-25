package com.exemplo.rest;

import com.exemplo.CSVReader;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

//import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONServiceTest {

    JSONService jsonService = new JSONService();

    private static CSVReader cvs = new CSVReader();

    Date data = new Date();

    Calendar calendar;

    @Test
    void testReceiveJSON ()
    {

        calendar = Calendar.getInstance();
        calendar.setTime(data);

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;


        JSONObject obj_input = null;

        try {
            obj_input = new JSONObject()
                    .put("op", "sum")
                    .put("value1", 10)
                    .put("value2", 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject obj_output = jsonService.receiveJSON(obj_input);

        String atual = null;
        String expected = null;

        try {
            atual = "{\"op\":\"" + obj_input.getString("op") + "\",\"value1\":" + obj_input.getDouble("value1") +  ",\"value2\":" + obj_input.getDouble("value2") + ",\"Total\":" + obj_output.getString("Total") + ",\"Data\":\"" + day+ "\\/" + (month<10?("0"+month):(month)) + "\\/" + year + "\"}";
            expected = "{\"op\":\"" + obj_output.getString("op") + "\",\"value1\":" + obj_output.getDouble("value1") +  ",\"value2\":" + obj_output.getDouble("value2") + ",\"Total\":" + obj_output.getString("Total") + ",\"Data\":\"" + day+ "\\/" + (month<10?("0"+month):(month)) + "\\/" + year + "\"}";

        } catch (JSONException e) {
            e.printStackTrace();
        }


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