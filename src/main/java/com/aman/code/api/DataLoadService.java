package com.aman.code.api;

import com.aman.code.model.Person;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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

    public static Person getPrimaryPersonDetails(Path masterDataFeedFilePath, String id) throws IOException {
        Person p = null;
        CSVReader csvReader=null;

        try(BufferedReader reader = Files.newBufferedReader(masterDataFeedFilePath)) {
            csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                String personId = nextRecord[0];
                if(id.equals(personId)) {
                    String fullName = nextRecord[1];
                    Integer age = Integer.valueOf(nextRecord[2]);
                    String address = nextRecord[3];
                    List<String> cities = extractList(nextRecord[4]);
                    List<String> schools = extractList(nextRecord[5]);
                    List<String> colleges = extractList(nextRecord[6]);
                    String currentOrg = nextRecord[7];
                    List<String> pastOrgs = extractList(nextRecord[8]);
                    List<String> interests = extractList(nextRecord[9]);
                    p = new Person(id, fullName, address, age, cities, schools, colleges, currentOrg, pastOrgs, interests);
                    break;
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
        return p;
    }

    private static List<String> extractList(String key) {
        String regex = "\\|";
        List<String> list = Arrays.stream(key.split(regex)).collect(Collectors.toList());
        list.removeIf(String::isEmpty);
        list.removeIf(Objects::isNull);
        return list;
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

                setConnection(connections, key, value);
                setConnection(connections, value, key);
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

    private static void setConnection(HashMap<String, LinkedList<String>> connections, String person, String friend) {
        LinkedList<String> values;
        if(connections.containsKey(person)) {
            values = connections.get(person);
        }
        else {
            values = new LinkedList<>();
        }
        values.push(friend);
        connections.put(person, values);
    }
}
