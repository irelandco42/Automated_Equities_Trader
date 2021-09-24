package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.SystemPrinter;
import com.etrade.exampleapp.v1.oauth.AuthorizationService;
import com.etrade.exampleapp.v1.terminal.ETClientApp;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class MorningRunTest {

    static ArrayList<String> blacklist = new ArrayList<>();
    //static PrintWriter out;

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken("ODA1MTAzMDk1MDEzMDQ4MzMy.YBWArw.YzdoaKMpBqfQ8zWRquWrlFVnRxQ").login().join();
        MyLogger log = new MyLogger("/Users/Ralph/Downloads/java_example_app-2/logs/LogOutput" + new Date().toString().substring(4) + ".txt");
        SystemPrinter printer = new SystemPrinter();

        /*try {
            FileWriter fw = new FileWriter("/Users/Ralph/Downloads/bork.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }*/
        DataManager data = new DataManager("/Users/Ralph/Downloads/bork.csv", true);

        printer.print("Initializing!");

        blacklist = Utility.load_blacklist();
        ArrayList<Company> companies = Utility.get_companies(blacklist);

        ETClientApp obj = new ETClientApp(args);
        AuthorizationService.loading = false;

        obj.init(true, false);

        obj.getAccountList(false);
        System.out.println(obj.getPrice("TSLA"));

        //out.close();
        data.close();

        printer.print("Done!");

        Utility.sleep(5000);

        log.close();

        System.exit(0);
    }
}
