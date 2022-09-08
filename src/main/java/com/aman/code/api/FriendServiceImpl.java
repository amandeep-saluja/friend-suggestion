package com.aman.code.api;

import com.aman.code.model.Person;
import com.aman.code.model.Suggestion;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.aman.code.api.DataLoadService.buildAllConnections;

public class FriendServiceImpl implements FriendService{
    @Override
    public List<Suggestion> getSuggestions(String id, int maxConnectionDegree, int maxSuggestions,
                                           Path attributeInfoFilePath,
                                           Path existingConnectionsFilePath,
                                           Path masterDataFeedFilePath) {
        try {
            //map will contain all the connections
            HashMap<String, LinkedList<String>> connections = buildAllConnections(existingConnectionsFilePath);
            System.out.println(connections);

            // iterate over the n degree connections to collect all the friends
            Set<String> potentialSuggestions = new HashSet<>();
            buildPotentialSuggestions(id, connections, maxConnectionDegree, potentialSuggestions);

            System.out.println(potentialSuggestions);

            List<String> ignoreList = new ArrayList<>(connections.get(id));
            ignoreList.add(id);

            System.out.println(ignoreList);

            //queue to store the top k suggestions
            Queue<Suggestion> queue = new PriorityQueue<>();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer getScore(Person primary, Person secondary) {



        return 0;
    }

    private void buildPotentialSuggestions(String id, HashMap<String, LinkedList<String>> connections,
                                           int degree, Set<String> potentialSuggestions) {
        if(degree==0) {
            return;
        }

        potentialSuggestions.add(id);
        if(null!=connections.get(id)) {
            for (String person : connections.get(id)) {
                potentialSuggestions.add(person);
                buildPotentialSuggestions(person, connections, degree - 1, potentialSuggestions);
            }
        }
    }


}
