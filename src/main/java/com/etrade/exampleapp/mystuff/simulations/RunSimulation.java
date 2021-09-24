package com.etrade.exampleapp.mystuff.simulations;

import com.etrade.exampleapp.mystuff.DataManager;
import com.etrade.exampleapp.mystuff.algorithms.Algorithm;
import com.etrade.exampleapp.mystuff.algorithms.Indices;
import com.etrade.exampleapp.mystuff.algorithms.TopX;

public class RunSimulation {

    public static void main(String[] args) {
        DataManager data = new DataManager("/Users/Ralph/Downloads/StockDatabase.csv", false);

        Algorithm.set_data(data.B_e_c_o_m_e_A_r_r_a_y_L_i_s_t());

        Algorithm[] Alorithms = {
                new Indices("", false)
        };

        for (Algorithm algorithm : Alorithms) {
            double start = System.currentTimeMillis();
            System.out.println(algorithm.run_sim("09/09/2021", "09/14/2021"));
            System.out.println(System.currentTimeMillis() - start);
        }
    }
}
