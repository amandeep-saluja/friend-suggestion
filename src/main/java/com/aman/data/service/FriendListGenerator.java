package com.aman.data.service;

import com.aman.code.model.Person;
import com.aman.code.model.Suggestion;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FriendListGenerator {

    private String baseId = "P000";

    private Integer count = 5000;

    private Path connectionFeedFilePath = Paths.get("src/main/java/resources/data/connections.csv");

    public void buildConnections() throws IOException {
        CSVWriter csvWriter = null;

        try (FileWriter writer = new FileWriter(connectionFeedFilePath.toString())) {
            csvWriter = new CSVWriter(writer);
            List<String[]> data = new ArrayList<String[]>();

            for(int i=1; i<count; i++) {
                Integer ran = Math.toIntExact(Math.round(Math.random() * count));
                String base = baseId + i;
                String connection = baseId + ran;
                data.add(new String[] {base, connection});
            }
            csvWriter.writeAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*finally {
            if (null != csvWriter) {
                csvWriter.close();
            }
        }*/
    }

    public static void main(String[] args) {
        try {
            FriendListGenerator friendListGenerator = new FriendListGenerator();
            friendListGenerator.buildConnections();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
