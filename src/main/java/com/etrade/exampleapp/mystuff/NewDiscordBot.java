package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.DiscordPrinter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

public class NewDiscordBot {

    static String BASE_URL = "https://old.reddit.com/r/wallstreetbets/";

    static final String[] subs = {"wallstreetbets", "investing", "stocks"};

    static ArrayList<String> BlackList = new ArrayList<>();

    static String currURL = BASE_URL;
    static String lastCode = "";

    static int page = 0, MAX_PAGES = 20;

    static int pages = 0;

    static long startTime = 0;

    static Utility util;

    public static void main(String[] args) {
        MyLogger log = new MyLogger("/volume1/Scripts/logs" + new Date().toString().substring(4) + ".txt");
        DiscordPrinter printer = new DiscordPrinter("848927206985564200");

        util = new Utility(log, printer);
        log.logString("Logger initialized");

        DiscordApi api = new DiscordApiBuilder().setToken("ODA1MTAzMDk1MDEzMDQ4MzMy.YBWArw.YzdoaKMpBqfQ8zWRquWrlFVnRxQ").login().join();
        TextChannel channel = api.getTextChannelById("848927206985564200").get();

        log.logString("Discord Initialized");

        BASE_URL = "https://old.reddit.com/r/" + "wallstreetbets" + "/";
        currURL = BASE_URL;

        BlackList = Utility.load_blacklist();
        log.logString("Blacklist Loaded");
        ArrayList<Company> companies = Utility.get_companies(BlackList);
        log.logString("Companies loaded");

        int p;

        for (int x = 0; x < subs.length; x++) {

            BASE_URL = "https://old.reddit.com/r/" + subs[x] + "/";
            currURL = BASE_URL;

            for (p = 1; p < MAX_PAGES + 1;) {

                pages++;

                URL url = null;
                try {
                    url = new URL(currURL);

                    try {
                        // Get the input stream through URL Connection
                        URLConnection con = url.openConnection();
                        InputStream in = url.openStream();

                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        boolean reading = false;
                        boolean leaving = false;
                        boolean lastPage = false;
                        boolean active = false;

                        double startTime = System.currentTimeMillis();

                        char[] chars = new char[4];
                        char lastChar = ' ';

                        while (!leaving && !lastPage) {
                            int charInt = br.read();
                            if (charInt == -1) lastPage = true;
                            char ch = (char) charInt;

                            if (ch == '>') {
                                reading = true;
                                chars = new char[4];
                            }

                            if (ch == '<') {
                                reading = false;
                                //System.out.println();
                                //System.out.println(new String(chars));
                            }

                            lastChar = chars[0];
                            for (int i = 0; i < 3; i++) {
                                chars[i] = chars[i + 1];
                            }
                            chars[3] = ch;

                            if (new String(chars).equals("teTa")) active = true;

                            if (reading && active) {
                                for (int n = 0; n < companies.size(); n++) {

                                    String str = new String(chars);

                                    if (str.equals("ew m")) leaving = true;

                                    int len = companies.get(n).ticker.length();

                                    int targLen = len < 5 && len > 2 ? len : 4;

                                    if (companies.get(n).ticker.equals(str.substring(0, targLen)) && !charIsLetter(lastChar)) {

                                        boolean valid = targLen < 4 ? chars[targLen] == ' ' : true;

                                        if (valid) {
                                            companies.get(n).curr_num++;
                                            //System.out.println(tickers.get(n));
                                            //System.out.println(new String(chars));
                                        }
                                    }
                                }
                            }
                        }

                        double start = System.currentTimeMillis();
                        while (!(new String(chars).equals("er=t")) && !lastPage) {
                            int charInt = br.read();
                            char ch = (char) charInt;

                            for (int i = 0; i < 3; i++) {
                                chars[i] = chars[i + 1];
                            }
                            chars[3] = ch;

                            if (start + 3000 < System.currentTimeMillis()) {
                                p = MAX_PAGES + 2;
                                break;
                            }
                        }

                        char[] code = new char[8];

                        for (int j = 0; j < 8; j++) {
                            code[j] = (char) br.read();
                        }

                        currURL = BASE_URL + "?count=25&after=t" + new String(code);

                        log.logString(subs[x] + " page " + p);
                        page++;
                        p++;
                    } catch (IOException e) {

                    }
                } catch (MalformedURLException e) {
                    log.logString("URL ERROR");
                }
            }
        }

        Utility.setDiffs(companies, "/volume1/Scripts/YesterdayData.csv");

        ArrayList<Company> valid_companies = new ArrayList<>();
        for (int i = 0; i < companies.size(); i++) {
            if (companies.get(i).curr_num > 2) {
                valid_companies.add(companies.get(i).clone());
            }
        }

        Company[] validCompanyArr = new Company[valid_companies.size()];
        for (int i = 0; i < valid_companies.size(); i++) {
            validCompanyArr[i] = valid_companies.get(i);
        }

        Company[] outputComanies = Utility.sort_companies(validCompanyArr);

        channel.sendMessage("FINAL RESULTS");
        for (int n = 0; n < outputComanies.length; n++) {
            channel.sendMessage(outputComanies[n].ticker + "  Num: " + outputComanies[n].curr_num + "  Diff: " + (outputComanies[n].curr_num - outputComanies[n].last_num));
        }
        channel.sendMessage("Pages scraped: " + page);

        Utility.export_tickers(companies, "/volume1/Scripts/YesterdayData.csv");
        log.close();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {

        }

        System.exit(0);
    }

    public static boolean charIsLetter(char ch) {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        for (char letter : letters) {
            if (Character.toUpperCase(ch) == letter) {
                return true;
            }
        }

        return false;
    }
}
