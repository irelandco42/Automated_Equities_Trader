package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.algorithms.Algorithm;
import com.etrade.exampleapp.mystuff.algorithms.TopX;
import com.etrade.exampleapp.v1.oauth.AuthorizationService;
import com.etrade.exampleapp.v1.terminal.ETClientApp;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.Arrays;

public class TestClass {

    public static void main(String[] args) {
        DataManager data = new DataManager("/Users/Ralph/Downloads/StockDatabase.csv", false);
        Algorithm.set_data(data.B_e_c_o_m_e_A_r_r_a_y_L_i_s_t());

        StockUtil.init("data_files/portfolio.txt", false);

        Portfolio diff = StockUtil.diff_portfolio(new TopX("Doesn't matter", 3, false).get_selections("09/20/2021"));

        System.out.print(StockUtil.get_actual_cash());
    }
}
