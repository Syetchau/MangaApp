package com.example.comicreader.Common;

import com.example.comicreader.Model.Chapter;
import com.example.comicreader.Model.Comic;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static List<Comic> comicList = new ArrayList<>();
    public static Comic comicSelected;
    public static List<Chapter> chapterList;
    public static Chapter chapterSelected;
    public static int chapterIndex = -1;

    public static String formatString(String name) {
        StringBuilder finalResult =
                new StringBuilder(name.length() > 15?name.substring(0,15) +"...":name);
        return finalResult.toString();
    }

    public static String[] categories = {
            "Action",
            "Adult",
            "Adventure",
            "Comedy",
            "Completed",
            "Cooking",
            "Doujinshi",
            "Drama",
            "Drop",
            "Ecchi",
            "Fantasy",
            "Gender bender",
            "Harem",
            "Historical",
            "Horror",
            "Jose",
            "Latest",
            "Manhua",
            "Manhwa",
            "Material arts",
            "Mature",
            "Mecha",
            "Medical",
            "Mystery",
            "Newest",
            "One shot",
            "Ongoing",
            "Psychological",
            "Romance",
            "School life",
            "Sci fi",
            "Seinen",
            "Shoujo",
            "Shoujo a",
            "Shounen",
            "Shounen ai",
            "Slice of life",
            "Smut",
            "Sports",
            "Superhero",
            "Supernatural",
            "Top Read",
            "Tragedy",
            "Webtoons",
            "Yaoi",
            "Yuri"
    };
}
