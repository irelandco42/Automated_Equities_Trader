package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.DiscordPrinter;
import com.etrade.exampleapp.mystuff.outs.SystemPrinter;
import com.etrade.exampleapp.v1.oauth.AuthorizationService;
import com.etrade.exampleapp.v1.terminal.ETClientApp;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ComputerMain {

    static DiscordApi api;
    static MyLogger log;
    static SystemPrinter printer;
    static DiscordPrinter discord;

    static String state;

    static ArrayList<Company> companies;
    static ArrayList<String> blacklist;

    static RedditReader redditReader;

    private static void init_whole() {
        api = new DiscordApiBuilder().setToken("TOKEN").login().join();
        log = new MyLogger("logs/LogOutput" + new Date().toString().substring(4) + ".txt");
        printer = new SystemPrinter();
        discord = new DiscordPrinter("848927206985564200");

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

        StockUtil.init("data_files/portfolio.txt", false);

        redditReader = new RedditReader(log, printer, true);

        Utility.init(log, printer, "");
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
                    //Utility.time_warp("09:15:00");
                    DataManager read_data = new DataManager("data_files/bork.csv", false);
                    ArrayList<ArrayList<Company>> all_data = read_data.B_e_c_o_m_e_A_r_r_a_y_L_i_s_t();

                    if (!CalendarUtil.isWeekday()) {
                        discord.print("I go back to sleep. See ya tomorrow.");
                        Utility.sleep(3600000);
                        break;
                    }

                    init_day();

                    redditReader.read_companies(companies, 2);
                    Utility.get_yesterday(companies, all_data);
                    Utility.process_data(companies);

                    printer.print(Integer.toString(redditReader.page));

                    Utility.put_day_backup(companies);

                    state = "Buy Morning";
                    break;
                case "Buy Morning":
                    //Utility.time_warp("16:15:00");

                    for (Company company : companies) {
                        if (company.curr_num > 0 || company.index) {
                            company.morn_price = StockUtil.getPrice(company.ticker);
                        }
                    }

                    Utility.put_day_backup(companies);

                    state = "Afternoon";
                    break;
                case "Afternoon":
                    //Utility.time_warp("15:12:00");
                    DataManager write_data = new DataManager("data_files/bork.csv", true);

                    for (Company company : companies) {
                        if (company.curr_num > 0 || company.index) {
                            company.aft_price = StockUtil.getPrice(company.ticker);
                            //write_data.write_line(company.to_csv());
                        }
                    }

                    write_data.close();

                    System.exit(0);

                    state = "Morning";
                    break;
            }
        }
    }
}
