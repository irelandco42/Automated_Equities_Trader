package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.algorithms.*;
import com.etrade.exampleapp.mystuff.outs.SystemPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Stonks {

    static String BASE_URL = "https://old.reddit.com/r/wallstreetbets/";

    static final String[] subs = {"wallstreetbets", "investing", "stocks"};

    static ArrayList<String> BlackList = new ArrayList<>();

    static String currURL = BASE_URL;
    static String lastCode = "";

    static long startTime = 0;

    static Utility util;

    public static void main(String[] args) {
        MyLogger log = new MyLogger("/Users/Ralph/IdeaProjects/StonksBot/logs/LogOutput" + new Date().toString().substring(4) + ".txt");
        SystemPrinter printer = new SystemPrinter();

        util = new Utility(log, printer);
        log.logString("Logger initialized");

        BASE_URL = "https://old.reddit.com/r/" + "wallstreetbets" + "/";
        currURL = BASE_URL;

        BlackList = Utility.load_blacklist();
        ArrayList<Company> companies = Utility.get_companies(BlackList);

        RedditReader reddit_reader = new RedditReader(log, printer, true);
        reddit_reader.read_companies(companies, 2);

        Utility.setDiffs(companies, "/Users/Ralph/IdeaProjects/StonksBot/src/yesterdayData.csv");

        Utility.process_data(companies);

        Utility.export_tickers(companies, "/Users/Ralph/IdeaProjects/StonksBot/src/yesterdayData.csv");
        log.close();
    }
}