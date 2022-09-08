package com.aman.code;

import com.aman.code.api.FriendService;
import com.aman.code.api.FriendServiceImpl;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App
{
    private static final Path attributeInfoFilePath = Paths.get("src/main/java/resources/AttributeInfo.csv");

    private static final Path connectionsFilePath = Paths.get("src/main/java/resources/ExistingConnections.csv");

    private static final Path personInfoFilePath = Paths.get("src/main/java/resources/MasterDataFeed.csv");

    public static void main( String[] args )
    {
        FriendService service = new FriendServiceImpl();
        service.getSuggestions("P00001", 4, 3,
                attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
    }
}
