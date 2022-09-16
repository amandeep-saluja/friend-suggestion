package com.aman.code;

import com.aman.code.api.FriendService;
import com.aman.code.api.FriendServiceImpl;
import com.aman.code.model.Suggestion;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class App
{
    private static final Path attributeInfoFilePath = Paths.get("src/main/java/resources/AttributeInfo.csv");

    private static final Path connectionsFilePath = Paths.get("src/main/java/resources/ExistingConnections.csv");

    private static final Path personInfoFilePath = Paths.get("src/main/java/resources/MasterDataFeed.csv");

    public static void main( String[] args )
    {
        /*FriendService service = new FriendServiceImpl();
        List<Suggestion> suggestions = service.getSuggestions("P00001", 4, 3,
                attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
        System.out.println(suggestions);*/
        Instant start = Instant.now();
        FriendService service = new FriendServiceImpl();
        List<Suggestion> suggestions = service.getSuggestions("P00045", 22, 20,
                Paths.get("src/main/java/resources/AttributeInfo.csv"),
                Paths.get("src/main/java/resources/data/connections.csv"),
                Paths.get("src/main/java/resources/data/5000_records.csv"));
        System.out.println("Overall time taken: "+ Duration.between(start, Instant.now()).toMinutes() + " minutes");
        suggestions.forEach(System.out::println);
    }
}
