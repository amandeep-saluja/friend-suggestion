package com.aman.code.api;

import com.aman.code.model.Person;
import com.aman.code.model.Suggestion;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.aman.code.api.DataLoadService.*;

public class FriendServiceImpl implements FriendService {
    @Override
    public List<Suggestion> getSuggestions(String id, int maxConnectionDegree, int maxSuggestions,
                                           Path attributeInfoFilePath,
                                           Path existingConnectionsFilePath,
                                           Path masterDataFeedFilePath) {
        try {
            System.out.println("\nStart building connections");
            Instant start = Instant.now();
            //map will contain all the connections
            HashMap<String, LinkedList<String>> connections = buildAllConnections(existingConnectionsFilePath);
            System.out.println("Connections build successfully " + connections.size());
            /*System.out.println("Connections: " + connections);*/
            System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " milli sec");

            // iterate over the n degree connections to collect all the friends
            Set<String> potentialSuggestions = new HashSet<>();
            start = Instant.now();
            System.out.println("\nFinding potential suggestions");
            buildPotentialSuggestions(id, connections, maxConnectionDegree, potentialSuggestions);
            System.out.println("Potential suggestions build: " + potentialSuggestions.size());
            System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMinutes() + " minutes");

            start = Instant.now();
            System.out.println("\nBuilding Ignore list");
            List<String> ignoreList = new ArrayList<>(connections.get(id));
            ignoreList.add(id);
            System.out.println("Ignore list: " + ignoreList);
            System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " milli sec");

            start = Instant.now();
            System.out.println("\nRemoving ignore list from potential suggestions");
            ignoreList.forEach(potentialSuggestions::remove);
            System.out.println("Final potential list of " + potentialSuggestions.size() + " is " + potentialSuggestions);
            System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " milli sec");

            start = Instant.now();
            System.out.println("\nLoading score factors");
            HashMap<String, Integer> scoreFactors = loadScoreFactor(attributeInfoFilePath);
            System.out.println("Score factors build " + scoreFactors);
            System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMinutes() + " milli sec");

            start = Instant.now();
            System.out.println("\nStarting processing final potential suggestions by score factors");
            List<Suggestion> suggestions = prepareSuggestions(potentialSuggestions, scoreFactors, maxSuggestions, masterDataFeedFilePath, id);
            System.out.println("Suggestions build successfully: " + suggestions.size());
            System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMinutes() + " minutes");

            return suggestions;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Suggestion> prepareSuggestions(Set<String> potentialSuggestions,
                                                HashMap<String, Integer> scoreFactors,
                                                int maxSuggestions,
                                                Path masterDataFeedFilePath,
                                                String id) throws IOException {
        //queue to store the top k suggestions
        Queue<Suggestion> minHeap = new PriorityQueue<>(maxSuggestions, (s1, s2) -> {
            int diff = s2.getScore() - s1.getScore();
            if (diff == 0) {
                return s1.getName().compareTo(s2.getName());
            }
            return diff;
        });

        Person primaryPersonDetails = getPrimaryPersonDetails(masterDataFeedFilePath, id);

        CSVReader csvReader = null;

        try (BufferedReader reader = Files.newBufferedReader(masterDataFeedFilePath)) {
            csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                String key = nextRecord[0];
                if (potentialSuggestions.contains(key)) {
                    String fullName = nextRecord[1];
                    Integer age = Integer.valueOf(nextRecord[2]);
                    String address = nextRecord[3];
                    List<String> cities = extractList(nextRecord[4]);
                    List<String> schools = extractList(nextRecord[5]);
                    List<String> colleges = extractList(nextRecord[6]);
                    String currentOrg = nextRecord[7];
                    List<String> pastOrgs = extractList(nextRecord[8]);
                    List<String> interests = extractList(nextRecord[9]);
                    Person secondaryPerson = new Person(key, fullName, address, age, cities, schools, colleges, currentOrg, pastOrgs, interests);

                    int score = calculateScore(new Person(primaryPersonDetails), secondaryPerson, scoreFactors);
                    if (score > 0) {
                        if (minHeap.size() >= maxSuggestions) {
                            minHeap.poll();
                        }
                        minHeap.add(new Suggestion(secondaryPerson.getId(), score, secondaryPerson.getFullName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }
        List<Suggestion> suggestionList = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            suggestionList.add(minHeap.peek());
            minHeap.poll();
        }
//        System.out.println("MinHeap: "+suggestionList);
        return suggestionList;
    }

    private List<String> extractList(String key) {
        String regex = "\\|";
        List<String> list = Arrays.stream(key.split(regex)).collect(Collectors.toList());
        list.removeIf(String::isEmpty);
        list.removeIf(Objects::isNull);
        return list;
    }

    private Integer calculateScore(Person primary, Person secondary, HashMap<String, Integer> scoreFactors) {
        Integer finalScore = 0;

        if (primary.getCurrentOrganization().equals(secondary.getCurrentOrganization())) {
            finalScore += scoreFactors.get(CURRENT_ORGANIZATION);
        }

        if (null != primary.getSchools()) {
            primary.getSchools().retainAll(secondary.getSchools());
            if (primary.getSchools().size() > 0) {
                finalScore += scoreFactors.get(SCHOOL);
            }
        }

        if (null != primary.getInterests()) {
            primary.getInterests().retainAll(secondary.getInterests());
            if (primary.getInterests().size() > 0) {
                finalScore += scoreFactors.get(INTERESTS);
            }
        }

        if (null != primary.getCities()) {
            primary.getCities().retainAll(secondary.getCities());
            if (primary.getCities().size() > 0) {
                finalScore += scoreFactors.get(CITY);
            }
        }

        if (null != primary.getColleges()) {
            primary.getColleges().retainAll(secondary.getColleges());
            if (primary.getColleges().size() > 0) {
                finalScore += scoreFactors.get(COLLEGE);
            }
        }

        if (null != primary.getPastOrganizations()) {
            primary.getPastOrganizations().retainAll(secondary.getPastOrganizations());
            if (primary.getPastOrganizations().size() > 0) {
                finalScore += scoreFactors.get(PAST_ORGANIZATION);
            }
        }

        return finalScore;
    }

    private void buildPotentialSuggestions(String id, HashMap<String, LinkedList<String>> connections,
                                           int degree, Set<String> potentialSuggestions) {
        if (degree == 0) {
            return;
        }

        potentialSuggestions.add(id);
        if (null != connections.get(id)) {
            for (String person : connections.get(id)) {
                potentialSuggestions.add(person);
                buildPotentialSuggestions(person, connections, degree - 1, potentialSuggestions);
            }
        }
    }


}
