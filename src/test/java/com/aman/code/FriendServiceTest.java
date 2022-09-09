package com.aman.code;

import com.aman.code.api.FriendService;
import com.aman.code.api.FriendServiceImpl;
import com.aman.code.model.Suggestion;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class FriendServiceTest {

    private static final Path attributeInfoFilePath = Paths.get("src/main/java/resources/AttributeInfo.csv");

    private static final Path connectionsFilePath = Paths.get("src/main/java/resources/ExistingConnections.csv");

    private static final Path personInfoFilePath = Paths.get("src/main/java/resources/MasterDataFeed.csv");

    private static Instant start;

    @BeforeClass
    public static void oneTimeSetup() {
        start = Instant.now();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.err.println("Execution took " + timeElapsed + " millis");
    }

    @Test
    public void test1() {
        FriendService service = new FriendServiceImpl();
        List<Suggestion> result = service.getSuggestions("P00004", 4, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
        Assert.assertEquals("P00001", result.get(0).getId());
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void test2() {
        FriendService service = new FriendServiceImpl();
        List<Suggestion> result = service.getSuggestions("P00001", 4, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
        Assert.assertEquals("P00005", result.get(0).getId());
        Assert.assertEquals("P00004", result.get(1).getId());
        Assert.assertEquals("P00003", result.get(2).getId());
    }

    @Test
    public void test3() {
        FriendService service = new FriendServiceImpl();
        List<Suggestion> result = service.getSuggestions("P00006", 4, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test4() {
        FriendService service = new FriendServiceImpl();
        List<Suggestion> result = service.getSuggestions("P00003", 4, 3, attributeInfoFilePath, connectionsFilePath, personInfoFilePath);
        Assert.assertEquals(20, result.get(0).getScore());
        Assert.assertEquals("Johny Holmes", result.get(0).getName());
    }
}
