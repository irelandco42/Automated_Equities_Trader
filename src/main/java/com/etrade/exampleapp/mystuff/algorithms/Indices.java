package com.etrade.exampleapp.mystuff.algorithms;

import com.etrade.exampleapp.mystuff.Company;
import com.etrade.exampleapp.mystuff.Utility;

import java.util.ArrayList;

public class Indices extends Algorithm {

    public Indices(String name, boolean overnight) {
        super(name, overnight);
    }

    @Override
    public String[] get_selections(String date) {
        return new String[] {"DJI", "SPX", "COMP.IDX"};
    }

    @Override
    public void optimize(String start_date, String end_date) {
        Indices temp_day = new Indices("Temp", false);
        Indices temp_night = new Indices("Temp", true);

        overnight = !(temp_day.run_sim(start_date, end_date) > temp_night.run_sim(start_date, end_date));
    }
}
