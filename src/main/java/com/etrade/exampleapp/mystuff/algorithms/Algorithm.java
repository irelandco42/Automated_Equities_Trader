package com.etrade.exampleapp.mystuff.algorithms;

import com.etrade.exampleapp.mystuff.Company;
import com.etrade.exampleapp.mystuff.Utility;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Algorithm {

    static ArrayList<ArrayList<Company>> all_data;

    public String name;

    public boolean overnight;

    public Algorithm(String name, boolean _overnight) {
        this.name = name;
        overnight = _overnight;
    }

    public static void set_data(ArrayList<ArrayList<Company>> _all_data) {
        all_data = _all_data;
    }

    public abstract String[] get_selections(String date);

    public abstract void optimize(String start_date, String end_date);

    public double calculate_gain(String date) {
        ArrayList<Company> today = Utility.get_day_companies(all_data, date, 0);
        ArrayList<Company> tomorrow = null;
        if (overnight) {
            tomorrow = Utility.get_day_companies(all_data, date, 1);
        }

        String[] selection_list = get_selections(date);

        double sum = 0;
        for (String ticker : selection_list) {
            Company today_company = Utility.get_from_ticker(ticker, today);
            if (overnight) {
                Company tomorrow_company = Utility.get_from_ticker(ticker, tomorrow);
                try {
                    sum += tomorrow_company.getMorn_price() / today_company.getMorn_price();
                } catch (NullPointerException e) {
                    //System.out.println("Ticker " + today_company.getTicker() + " not found tomorrow!");
                    sum += 1;
                }
            } else {
                sum += today_company.getAft_price() / today_company.getMorn_price();
            }
        }

        if (selection_list.length > 0) {
            return sum / selection_list.length;
        } else {
            return 1;
        }
    }

    public double run_sim(String start_date, String end_date) {
        double total = 100;

        int startI = -1;
        for (int i = 0; i < all_data.size(); i++) {
            if (Objects.equals(all_data.get(i).get(0).getDate(), start_date)) {
                startI = i;
                i = all_data.size() + 1;
            }
        }

        int counter = startI;
        String date;
        do {
            date = all_data.get(counter).get(0).getDate();
            total *= calculate_gain(date);
            counter++;
        } while (!Objects.equals(date, end_date));

        return total;
    }
}