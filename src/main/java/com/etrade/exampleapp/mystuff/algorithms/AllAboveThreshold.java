package com.etrade.exampleapp.mystuff.algorithms;

import com.etrade.exampleapp.mystuff.Company;
import com.etrade.exampleapp.mystuff.Utility;

import java.util.ArrayList;

public class AllAboveThreshold extends Algorithm {

    double threshold;

    public AllAboveThreshold(String name, double _threshold, boolean overnight) {
        super(name, overnight);
        threshold = _threshold;
    }

    @Override
    public String[] get_selections(String date) {
        ArrayList<Company> today = Utility.get_day_companies(all_data, date, 0);
        Company[] today_sorted = Utility.sort_companies(today);

        return Utility.get_above_thresh(today_sorted, threshold, today_sorted[0].getPages_read());
    }

    @Override
    public void optimize(String start_date, String end_date) {
        double startThresh = 0.01;
        double end_Thresh = 0.5;
        double step = 0.01;

        AllAboveThreshold[] Alorithms = new AllAboveThreshold[(int) ((end_Thresh - startThresh) / step)];

        for (int i = 0; i < Alorithms.length; i++) {
            double thresh = startThresh + (i * step);
            Alorithms[i] = new AllAboveThreshold(Double.toString(thresh), thresh, overnight);
        }

        double max = -1;
        AllAboveThreshold max_alg = null;
        for (AllAboveThreshold algorithm : Alorithms) {
            double gain = algorithm.run_sim(start_date, end_date);
            System.out.printf("%4.4s:  %.2f\n", algorithm.name, gain);

            if (gain > max) {
                max_alg = algorithm;
                max = gain;
            }
        }

        threshold = max_alg.threshold;
    }
}
