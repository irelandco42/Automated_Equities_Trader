package com.etrade.exampleapp.mystuff.algorithms;

import com.etrade.exampleapp.mystuff.Company;
import com.etrade.exampleapp.mystuff.Utility;

import java.util.ArrayList;

public class TopXUnlessBig extends Algorithm {

    int num;
    double big_thresh;

    public TopXUnlessBig(String name, int _num, double _big_thresh, boolean overnight) {
        super(name, overnight);
        num = _num;
        big_thresh = _big_thresh;
    }

    @Override
    public String[] get_selections(String date) {
        ArrayList<Company> today = Utility.get_day_companies(all_data, date, 0);
        Company[] today_sorted = Utility.sort_companies(today);

        if ((double) today_sorted[0].getCurr_num() / (double) today_sorted[0].getPages_read() > big_thresh) {
            return Utility.get_top_of_array(today_sorted, 1);
        } else {
            return Utility.get_top_of_array(today_sorted, num);
        }
    }

    @Override
    public void optimize(String start_date, String end_date) {

    }
}
