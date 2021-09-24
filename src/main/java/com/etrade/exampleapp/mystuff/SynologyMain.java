package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.DiscordPrinter;
import com.etrade.exampleapp.mystuff.outs.SystemPrinter;
import com.etrade.exampleapp.v1.oauth.AuthorizationService;
import com.etrade.exampleapp.v1.terminal.ETClientApp;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SynologyMain {

    static DiscordApi api;
    static MyLogger log;
    static DiscordPrinter discord;

    static String state;

    static ArrayList<Company> companies;
    static ArrayList<String> blacklist;

    static RedditReader redditReader;

    private static void init_whole() {
        api = new DiscordApiBuilder().setToken("ODA1MTAzMDk1MDEzMDQ4MzMy.YBWArw.YzdoaKMpBqfQ8zWRquWrlFVnRxQ").login().join();
        log = new MyLogger("/volume1/Scripts/logs/LogOutput" + new Date().toString().substring(4) + ".txt");
        discord = new DiscordPrinter("806639965395484681");

        api.addMessageCreateListener(event -> {
            String mess = event.getMessageContent();
            if (Objects.equals(mess, "$kill")) {
                System.exit(0);
            }
        });

        api.addMessageCreateListener(event -> {
            String mess = event.getMessageContent();
            if (Objects.equals(mess, "$load_backup")) {
                companies = new ArrayList<>();
                Utility.load_day_backup(companies);
            }
        });

        StockUtil.init("volume1/Scripts/data_files/portfolio.txt", true);

        redditReader = new RedditReader(log, discord, false);

        Utility.init(log, discord, "/volume1/Scripts/");
    }

    private static void init_day() {
        blacklist = Utility.load_blacklist();
        companies = Utility.get_companies(blacklist);

        // Adding the three major indices
        companies.add(new Company("DJI", true));
        companies.add(new Company("SPX", true));
        companies.add(new Company("COMP.IDX", true));
    }

    public static void main(String[] args) {
        init_whole();
        state = "Morning";

        while (true) {
            switch (state) {
                case "Morning":
                    discord.print("Step 1");
                    Utility.time_warp("08:10:00");
                    discord.print("Step 2");

                    DataManager read_data = new DataManager("/volume1/Scripts/StockDatabase.csv", false);
                    ArrayList<ArrayList<Company>> all_data = read_data.B_e_c_o_m_e_A_r_r_a_y_L_i_s_t();

                    if (!CalendarUtil.isWeekday()) {
                        discord.print("I go back to sleep. See ya tomorrow.");
                        Utility.sleep(3600000);
                        break;
                    }

                    init_day();

                    redditReader.read_companies(companies, 20);
                    Utility.get_yesterday(companies, all_data);
                    Utility.process_data(companies);

                    discord.print("Pages scraped: " + redditReader.page);

                    Utility.put_day_backup(companies);

                    discord.print("Step 3");
                    state = "Buy Morning";
                    break;
                case "Buy Morning":
                    discord.print("Step 4");
                    Utility.time_warp("08:30:00");
                    discord.print("Step 5");

                    for (Company company : companies) {
                        if (company.curr_num > 0 || company.index) {
                            company.morn_price = StockUtil.getPrice(company.ticker);
                        }
                    }

                    Utility.put_day_backup(companies);

                    discord.print("Step 6");
                    state = "Afternoon";
                    break;
                case "Afternoon":
                    discord.print("Step 7");
                    Utility.time_warp("15:00:00");
                    discord.print("Step 8");

                    DataManager write_data = new DataManager("/volume1/Scripts/StockDatabase.csv", true);

                    for (Company company : companies) {
                        if (company.curr_num > 0 || company.index) {
                            company.aft_price = StockUtil.getPrice(company.ticker);
                            write_data.write_line(company.to_csv());
                        }
                    }

                    write_data.close();

                    discord.print("Step 9");

                    Utility.sleep(5000);

                    state = "Morning";
                    break;
            }
        }
    }
}
