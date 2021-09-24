package com.etrade.exampleapp.mystuff.simulations;

import com.etrade.exampleapp.mystuff.DataManager;
import com.etrade.exampleapp.mystuff.algorithms.Algorithm;
import com.etrade.exampleapp.mystuff.algorithms.AllAboveThreshold;
import com.etrade.exampleapp.mystuff.algorithms.Indices;
import com.etrade.exampleapp.mystuff.algorithms.TopX;

import java.util.Arrays;

public class Optimize {

    public static void main(String[] args) {
        DataManager data = new DataManager("/Users/Ralph/Downloads/StockDatabase.csv", false);
        Algorithm.set_data(data.B_e_c_o_m_e_A_r_r_a_y_L_i_s_t());

        //above_thresh(false);

        AllAboveThreshold thresh_alg = new AllAboveThreshold("Optimized Daily", -1, false);
        thresh_alg.optimize("09/09/2021", "09/21/2021");

        TopX top_alg = new TopX("Optimized Daily", -1, false);
        top_alg.optimize("09/09/2021", "09/21/2021");

        System.out.println();
        System.out.println(thresh_alg.run_sim("09/09/2021", "09/21/2021"));
        System.out.println(top_alg.run_sim("09/09/2021", "09/21/2021"));


        //top_x(false);
    }

    public static void above_thresh(boolean overnight) {
        double startThresh = 0.01;
        double end_Thresh = 0.5;
        double step = 0.01;

        Algorithm[] Alorithms = new Algorithm[(int) ((end_Thresh - startThresh) / step)];

        for (int i = 0; i < Alorithms.length; i++) {
            double thresh = startThresh + (i * step);
            Alorithms[i] = new AllAboveThreshold(Double.toString(thresh), thresh, overnight);
        }

        for (Algorithm algorithm : Alorithms) {
            System.out.printf("%4.4s:  %.2f\n", algorithm.name, algorithm.run_sim("09/09/2021", "09/17/2021"));
            //System.out.println(algorithm.name + ":  " + algorithm.run_sim("09/09/2021", "09/15/2021"));
        }
    }

    public static void top_x(boolean overnight) {
        int up_to = 50;

        Algorithm[] Alorithms = new Algorithm[up_to];

        for (int i = 0; i < Alorithms.length; i++) {
            Alorithms[i] = new TopX(Integer.toString(i + 1), i + 1, overnight);
        }

        for (Algorithm algorithm : Alorithms) {
            System.out.printf("%2.2s:  %.2f\n", algorithm.name, algorithm.run_sim("09/09/2021", "09/17/2021"));
        }
    }
}