package com.aman.code.api;

import com.aman.code.model.Suggestion;

import java.nio.file.Path;
import java.util.List;

public class FriendServiceImpl implements FriendService{
    @Override
    public List<Suggestion> getSuggestions(String id, int maxConnectionDegree, int maxSuggestions, Path attributeInfoFilePath, Path existingConnectionsFilePath, Path masterDataFeedFilePath) {
        return null;
    }
}
