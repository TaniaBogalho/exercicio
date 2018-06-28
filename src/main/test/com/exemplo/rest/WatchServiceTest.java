package com.exemplo.rest;

import com.exemplo.WatchService;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMockRunner;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

//import java.io.File;

@RunWith(EasyMockRunner.class)
class WatchServiceTest {

    //Create de mock
    /*@Mock
    private Files file = EasyMock.createMock(Files.class);
    */

    //Set the tested class
    @TestSubject
    private WatchService watchService = new WatchService();

    
    
    private static void createFile()
    {
        //boolean sucess = false;

        String sourcePath = "/home/tania/test_file.csv";
        File source = new File(sourcePath);

        String destPath = "/home/tania/input/test_file.csv";
        File dest = new File(destPath);


        try {
            FileUtils.copyFile(source,dest);
            //sucess = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        //return sucess;

    }

    @Test
    void testReadCSVFile()
    {
        Thread watchServicethread = new Thread(() -> watchService.readCSVFile());
        watchServicethread.start();


        try {

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread createFilethread = new Thread(WatchServiceTest::createFile);
        createFilethread.start();


        try {

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
