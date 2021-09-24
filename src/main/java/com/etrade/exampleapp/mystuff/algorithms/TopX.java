package com.etrade.exampleapp.mystuff.algorithms;

import com.etrade.exampleapp.mystuff.Company;
import com.etrade.exampleapp.mystuff.Utility;

import java.util.ArrayList;

public class TopX extends Algorithm {

    int num;

    public TopX(String name, int _num, boolean overnight) {
        super(name, overnight);
        num = _num;
    }

    @Override
    public String[] get_selections(String date) {
        ArrayList<Company> today = Utility.get_day_companies(all_data, date, 0);
        Company[] today_sorted = Utility.sort_companies(today);

        return Utility.get_top_of_array(today_sorted, num);
    }

    @Override
    public void optimize(String start_date, String end_date) {
        int up_to = 50;

        TopX[] alorithms = new TopX[up_to];

        for (int i = 0; i < alorithms.length; i++) {
            alorithms[i] = new TopX(Integer.toString(i + 1), i + 1, overnight);
        }

        double max = -1;
        TopX max_alg = null;
        for (TopX algorithm : alorithms) {
            double gain = algorithm.run_sim(start_date, end_date);
            System.out.printf("%6.6s:  %.2f\n", algorithm.name, gain);

            if (gain > max) {
                max_alg = algorithm;
                max = gain;
            }
        }

        num = max_alg.num;
    }
}
