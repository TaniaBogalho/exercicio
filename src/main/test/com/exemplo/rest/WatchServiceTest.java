package com.exemplo.rest;

import com.exemplo.WatchService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

class WatchServiceTest {

    private WatchService watchService = new WatchService();

    @Test
    void testCVSFile() throws Exception
    {

        watchService.ReadCSVFile();

        File source = new File("/home/tania/test_file.csv");
        File dest = new File("/home/tania/input/test_file.csv");

        FileUtils.copyFile(source, dest);


       /* while (true) {

            try {
                //iterate over events
                // listen to events
                watchKey = watcher.take();

                // get list of events as they occur
                List<WatchEvent<?>> events = watchKey.pollEvents();

                File source = new File("/home/tania/test_file.csv");
                File dest = new File("/home/tania/input/test_file.csv");

                FileUtils.copyFile(source, dest);

                for (WatchEvent event : events) {
                    assertThat(event.kind() == StandardWatchEventKinds.ENTRY_CREATE, is(true));

                    JSONObject obj_input = cvs.readCsvFile("");

                    JSONObject obj_output = jsonService.receiveJSON(obj_input);


                    String atual = null;
                    String expected = null;

                    try {
                        atual = "{\"op\":\"" + obj_input.getString("op") + "\",\"value1\":" + obj_input.getDouble
                                ("value1") + ",\"value2\":" + obj_input.getDouble("value2") + ",\"Total\":" +
                                obj_output.getString("Total") + ",\"Data\":\"" + day + "\\/" + (month < 10 ? ("0" +
                                month) : (month)) + "\\/" + year + "\"}";

                        expected = "{\"op\":\"" + obj_output.getString("op") + "\",\"value1\":" + obj_output
                                .getDouble("value1") + ",\"value2\":" + obj_output.getDouble("value2") + "," +
                                "\"Total\":" + obj_output.getString("Total") + ",\"Data\":\"" + day + "\\/" + (month
                                < 10 ? ("0" + month) : (month)) + "\\/" + year + "\"}";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    assertEquals(expected, atual);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }*/

    }
}
