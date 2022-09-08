package com.aman.code.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;

public class DataLoadService {

    public static String CURRENT_ORGANIZATION = "Current organization";
    public static String SCHOOL = "School";
    public static String INTERESTS = "Interests";
    public static String CITY = "City";
    public static String COLLEGE = "College";
    public static String PAST_ORGANIZATION = "Past organization";

    public static HashMap<String, Integer> loadScoreFactor(Path attributeInfoFilePath) throws IOException {
        HashMap<String, Integer> scoreFactor = new HashMap<>();

        CSVReader csvReader=null;

        try(BufferedReader reader = Files.newBufferedReader(attributeInfoFilePath)) {
            csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                String factor = nextRecord[0];
                Integer score = Integer.valueOf(nextRecord[1]);

                switch (factor) {
                    case "Current organization": scoreFactor.put(CURRENT_ORGANIZATION, score);
                    break;
                    case "School": scoreFactor.put(SCHOOL, score);
                    break;
                    case "Interests": scoreFactor.put(INTERESTS, score);
                    break;
                    case "City": scoreFactor.put(CITY, score);
                    break;
                    case "College": scoreFactor.put(COLLEGE, score);
                    break;
                    case "Past organization": scoreFactor.put(PAST_ORGANIZATION, score);
                    break;
                    default:
                        System.out.println("Unknown scoreFactor: "+factor);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(null!=csvReader) {
                csvReader.close();
            }
        }

        return scoreFactor;
    }

    public static HashMap<String, LinkedList<String>> buildAllConnections(Path connectionsFilePath) throws IOException {
        HashMap<String, LinkedList<String>> connections = new HashMap<>();
        CSVReader csvReader=null;

        try(BufferedReader reader = Files.newBufferedReader(connectionsFilePath)) {
            csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                String key = nextRecord[0];
                String value = nextRecord[1];
                LinkedList<String> values;
                if(connections.containsKey(key)) {
                    values = connections.get(key);
                }
                else {
                    values = new LinkedList<>();
                }
                values.push(value);
                connections.put(key, values);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(null!=csvReader) {
                csvReader.close();
            }
        }

        return connections;
    }
}
