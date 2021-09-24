package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.OutObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RedditReader {

    public int page = 0;

    MyLogger log;
    OutObject out;

    boolean printing;

    public RedditReader(MyLogger _log, OutObject _out, boolean _printing) {
        log = _log;
        out = _out;
        printing = _printing;
    }

    public void read_companies(ArrayList<Company> companies, int MAX_PAGES) {
        String[] subs = {"wallstreetbets", "investing", "stocks"};
        int p = 1;
        page = 0;

        for (int x = 0; x < 3; x++) {

            String BASE_URL = "https://old.reddit.com/r/" + subs[x] + "/";
            String currURL = BASE_URL;

            for (p = 1; p < MAX_PAGES + 1; ) {

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
                            }

                            lastChar = chars[0];
                            for (int i = 0; i < 3; i++) {
                                chars[i] = chars[i + 1];
                            }
                            chars[3] = ch;

                            if (new String(chars).equals("teTa")) active = true;

                            if (reading && active) {
                                for (int n = 0; n < companies.size(); n++) {
                                    Company curr_company = companies.get(n);

                                    String str = new String(chars);

                                    if (str.equals("ew m")) leaving = true;

                                    int len = curr_company.ticker.length();

                                    int targLen = len < 5 && len > 2 ? len : 4;

                                    if (curr_company.ticker.equals(str.substring(0, targLen)) && !charIsLetter(lastChar)) {

                                        boolean valid = targLen < 4 ? chars[targLen] == ' ' : true;

                                        if (valid) {
                                            curr_company.curr_num++;
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

                        if (printing) {
                            out.print("/////Through r/" + subs[x] + " page " + p + "/////");
                            for (int i = 0; i < companies.size(); i++) {
                                if (companies.get(i).curr_num > 2) {
                                    out.print(companies.get(i).curr_num + " instances of " + companies.get(i).ticker);
                                }
                            }
                            out.print("");
                            out.print("INITIALIZING NEW PAGE " + currURL);
                        }
                        p++;
                    } catch (IOException e) {
                    }
                } catch (MalformedURLException e) {
                    log.logString("Malformed URL Exception in " + subs[x] + " page " + p);
                }
            }
        }

        for (Company company : companies) {
            company.pages_read = page;
        }
    }

    private static boolean charIsLetter(char ch) {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        for (char letter : letters) {
            if (Character.toUpperCase(ch) == letter) {
                return true;
            }
        }

        return false;
    }
}
