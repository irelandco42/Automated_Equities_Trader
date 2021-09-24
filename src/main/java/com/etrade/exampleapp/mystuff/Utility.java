package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.OutObject;

import java.io.*;
import java.util.*;

public class Utility {
    static MyLogger log;
    static OutObject out;

    static String root;

    public Utility(MyLogger log, OutObject out) {
        Utility.log = log;
        Utility.out = out;
    }

    public static void init(MyLogger _log, OutObject _out, String _root) {
        log = _log;
        out = _out;
        root = _root;
    }

    public static void export_tickers(ArrayList<Company> companies, String fileName) {
        PrintWriter out;

        try {
            out = new PrintWriter(fileName);
            for (Company company : companies) {
                out.println(company.ticker + "," + company.curr_num + ",");
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Export Error");
        }
    }

    public static ArrayList<Company> get_companies(ArrayList<String> blacklist) {
        ArrayList<Company> temp_companies = new ArrayList<>();

        Scanner sc;
        try {
            sc = new Scanner(new File(root + "data_files/stonks.csv"));

            sc.useDelimiter(",");
            while (sc.hasNext()) {
                boolean badStock = false;
                String temp = sc.next();

                sc.next();
                if (sc.nextLine().length() < 20) {
                    badStock = true;
                }

                for (String black : blacklist) {
                    if (temp.matches(black)) {
                        badStock = true;
                    }
                }

                if (!badStock) {
                    temp_companies.add(new Company(temp));
                }


            }

            sc.close();
        } catch (FileNotFoundException e) {
            log.logString("stonks.csv not found!");
            log.close();

            out.print("stonks.csv not found!");
        }

        return temp_companies;
    }

    public static ArrayList<String> load_blacklist() {

        ArrayList<String> blackList = new ArrayList<>();

        Scanner sc;
        try {
            sc = new Scanner(new File(root + "data_files/blacklist.txt"));
            sc.useDelimiter(",");
            while (sc.hasNext()) {
                blackList.add(sc.next());
            }

            sc.close();
        } catch (FileNotFoundException e) {
            log.logString("Blacklist file not found!");
            out.print("Blacklist file not found!");
        }

        return blackList;
    }

    public static Company[] sort_companies(ArrayList<Company> input) {
        Company[] temp = new Company[input.size()];

        for (int i = 0; i < input.size(); i++) {
            temp[i] = input.get(i).clone();
        }

        return sort_companies(temp);
    }

    public static Company[] sort_companies(Company[] input) {
        Company[] temp = new Company[input.length];
        for (int i = 0; i < input.length; i++) {
            int min = Integer.MAX_VALUE;
            int maxIndex = -1;

            for (int j = 0; j < input.length; j++) {
                if (input[j].curr_num < min) {
                    min = input[j].curr_num;
                    maxIndex = j;
                }
            }
            temp[input.length - i - 1] = input[maxIndex].clone();
            input[maxIndex].curr_num = Integer.MAX_VALUE;
        }

        return temp;
    }

    public static void setDiffs(ArrayList<Company> all_companies, String old_data_filepath) {
        Scanner sc;
        try {
            sc = new Scanner(new File(old_data_filepath));

            sc.useDelimiter(",");
            while (sc.hasNext()) {
                String ticker = sc.next();
                for (Company company : all_companies) {
                    if (company.ticker.equals(ticker)) {
                        company.last_num = Integer.parseInt(sc.next());
                    }
                }

                sc.nextLine();
            }
        } catch (FileNotFoundException e) {
            for (Company company : all_companies) {
                company.last_num = 0;
            }
            log.logString("Yesterday Data file not found!");
            log.close();
        }
    }

    public static void get_yesterday(ArrayList<Company> today_companies, ArrayList<ArrayList<Company>> all_data) {

        for (Company tod_comp : today_companies) {
            Company yest_comp = Utility.get_from_ticker(tod_comp.ticker, all_data.get(all_data.size() - 1));
            if (yest_comp != null) {
                tod_comp.last_num = yest_comp.curr_num;
            } else {
                tod_comp.last_num = 0;
            }
        }
    }

    public static void process_data(ArrayList<Company> companies) {
        ArrayList<Company> valid_companies = new ArrayList<>();
        for (Company company : companies) {
            if (company.curr_num > 2) {
                valid_companies.add(company.clone());
            }
        }

        Company[] validCompanyArr = new Company[valid_companies.size()];
        for (int i = 0; i < valid_companies.size(); i++) {
            validCompanyArr[i] = valid_companies.get(i);
        }

        Company[] outputComanies = Utility.sort_companies(validCompanyArr);

        out.print("Final Results:");
        for (Company outputComany : outputComanies) {
            out.print(outputComany.ticker + "  Num: " + outputComany.curr_num + "  Diff: " + (outputComany.curr_num - outputComany.last_num));
        }
    }

    public static String[] get_top_of_array(Company[] companies, int num) {
        String[] temp = new String[num];

        for (int i = 0; i < num; i++) {
            temp[i] = companies[i].ticker;
        }

        return temp;
    }

    public static String[] get_above_thresh(Company[] companies, double thresh, int pages) {
        if (companies.length > 0) {
            ArrayList<Company> above = new ArrayList<>();


            for (int i = 0; i < companies.length; i++) {
                Company company = companies[i];
                if ((double) company.curr_num / (double) pages > thresh) {
                    above.add(company.clone());
                } else {
                    i = companies.length + 1;
                }
            }

            String[] out = new String[above.size()];
            for (int i = 0; i < above.size(); i++) {
                out[i] = above.get(i).ticker;
            }
            return out;
        } else {
            return new String[]{};
        }
    }

    public static void time_warp(String wake_time) {
        int wake_sec = Integer.parseInt(wake_time.substring(6, 8));
        int wake_min = Integer.parseInt(wake_time.substring(3, 5));
        int wake_hr = Integer.parseInt(wake_time.substring(0, 2));

        String curr_time = CalendarUtil.getTime();
        int curr_sec = Integer.parseInt(curr_time.substring(6, 8));
        int curr_min = Integer.parseInt(curr_time.substring(3, 5));
        int curr_hr = Integer.parseInt(curr_time.substring(0, 2));

        int tot_sec = wake_sec - curr_sec;
        tot_sec += (wake_min - curr_min) * 60;
        if (wake_hr < curr_hr) {
            tot_sec += ((wake_hr + 24) - curr_hr) * 3600;
        } else if (wake_hr == curr_hr) {
            if (wake_min < curr_min) {
                tot_sec += ((wake_hr + 24) - curr_hr) * 3600;
            }
        } else {
            tot_sec += (wake_hr - curr_hr) * 3600;
        }
        sleep(tot_sec * 1000);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            out.print("The very fabric of spacetime has been ruptured!");
        }
    }

    public static Company get_from_ticker(String ticker, ArrayList<Company> company_list) {
        for (Company company : company_list) {
            if (Objects.equals(company.ticker, ticker)) return company;
        }
        //System.out.println("Company Not Found!");
        return null;
    }

    public static ArrayList<Company> get_day_companies(ArrayList<ArrayList<Company>> all_data, String date, int offset) {
        for (int i = 0; i < all_data.size(); i++) {
            if (Objects.equals(all_data.get(i).get(0).getDate(), date)) {
                return all_data.get(i + offset);
            }
        }
        return null;
    }

    public static void put_day_backup(ArrayList<Company> companies) {
        PrintWriter out;

        try {
            out = new PrintWriter(root + "data_files/day_backup");
            for (Company company : companies) {
                out.println(company.to_csv());
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Export Error");
        }
    }

    public static void load_day_backup(ArrayList<Company> companies) {
        Scanner sc;

        try {
            sc = new Scanner(new File(root + "data_files/day_backup"));
            sc.useDelimiter(",");

            while (sc.hasNext()) {
                Company temp_company = new Company(sc.next());
                temp_company.curr_num = Integer.parseInt(sc.next());
                temp_company.morn_price = Double.parseDouble(sc.next());
                temp_company.aft_price = Double.parseDouble(sc.next());

                temp_company.date = sc.next();

                temp_company.pages_read = Integer.parseInt(sc.next());

                companies.add(temp_company);

                sc.nextLine();
            }

            out.print("Backup successfully loaded!");
        } catch (FileNotFoundException e) {
            out.print("File not found!");
        } catch (NoSuchElementException e) {

        }
    }
}
