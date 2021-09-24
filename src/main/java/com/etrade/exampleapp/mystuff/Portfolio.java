package com.etrade.exampleapp.mystuff;

import java.util.ArrayList;
import java.util.Objects;

public class Portfolio {

    ArrayList<String> tickers;
    ArrayList<Integer> amounts;

    public Portfolio(ArrayList<String> _tickers, ArrayList<Integer> _amounts) {
        tickers = _tickers;
        amounts = _amounts;
    }

    public boolean contains(String ticker) {
        for (int i = 0; i < tickers.size(); i++) {
            if (Objects.equals(ticker, tickers.get(i))) {
                return true;
            }
        }

        return false;
    }

    public int get_amount(String ticker) {
        for (int i = 0; i < tickers.size(); i++) {
            if (Objects.equals(ticker, tickers.get(i))) {
                return amounts.get(i);
            }
        }

        return -1;
    }

    public void prune() {
        ArrayList<Integer> remove_indices = new ArrayList<>();
        for (int i = 0; i < tickers.size(); i++) {
            if (amounts.get(i) == 0) {
                remove_indices.add(i);
            }
        }

        for (int i = remove_indices.size() - 1; i > -1; i++) {
            tickers.remove(i);
            amounts.remove(i);
        }
    }

    @Override
    public String toString() {
        String str = "";

        for (int i = 0; i < tickers.size(); i++) {
            str = str + tickers.get(i) + ":  " + amounts.get(i) + "\n";
        }

        return str;
    }
}
