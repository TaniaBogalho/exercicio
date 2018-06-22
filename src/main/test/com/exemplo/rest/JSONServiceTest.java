package com.exemplo.rest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONServiceTest {

    JSONService js = new JSONService();


    @Test
    void testReceiveJSON () throws IOException, JSONException
    {

        JSONObject obj_a_testar = new JSONObject()
                    .put("op", "sum")
                    .put("value1", 10)
                    .put("value2", 5);


        JSONObject obj = js.receiveJSON(obj_a_testar);

        String atual = obj.toString();
        String expected = "{\"op\":\"sum\",\"value1\":10,\"value2\":5,\"Total\":15,\"Data\":\"21\\/06\\/2018\"}";

        assertEquals(expected, atual);
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