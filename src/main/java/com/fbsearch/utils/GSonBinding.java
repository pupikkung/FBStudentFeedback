package com.fbsearch.utils;

import com.fbsearch.domains.FBFirstLevel;
import com.fbsearch.domains.FBSecondLevel;
import com.fbsearch.domains.Feed;
import com.google.gson.Gson;

/**
 *
 * @author Songkran Totiya
 */
public class GSonBinding {

    public static FBFirstLevel convertToFirstLevelObject(String json) {
        Gson gson = new Gson();
        FBFirstLevel obj = gson.fromJson(json, FBFirstLevel.class);
        return obj;
    }

    public static FBSecondLevel convertToSecondLevelObject(String json) {
        Gson gson = new Gson();
        FBSecondLevel obj = gson.fromJson(json, FBSecondLevel.class);
        return obj;
    }

    public static Feed convertToNextPageLevelObject(String json) {
        Gson gson = new Gson();
        Feed obj = gson.fromJson(json, Feed.class);
        return obj;
    }
}
