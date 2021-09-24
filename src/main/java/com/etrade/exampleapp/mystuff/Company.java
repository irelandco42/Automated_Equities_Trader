package com.etrade.exampleapp.mystuff;

public class Company {

    String ticker;

    String date = null;
    int last_num = -42;
    int curr_num = 0;

    double morn_price = -1;
    double aft_price = -1;

    int pages_read = -1;

    boolean index = false;

    public Company(String _ticker) {
        ticker = _ticker;
    }

    public Company(String _ticker, boolean _index) {
        ticker = _ticker;
        index = _index;
    }

    @Override
    public Company clone() {
        Company temp = new Company(ticker);
        temp.curr_num = curr_num;
        temp.last_num = last_num;
        temp.morn_price = morn_price;
        temp.aft_price = aft_price;
        temp.date = date;
        temp.pages_read = pages_read;
        return temp;
    }

    public String to_csv() {
        if (date != null) {
            return ticker + "," + curr_num + "," + morn_price + "," + aft_price + "," + date + "," + pages_read + ",";
        } else {
            return ticker + "," + curr_num + "," + morn_price + "," + aft_price + "," + CalendarUtil.getDate() + "," + pages_read + ",";
        }
    }

    @Override
    public String toString() {
        return ticker;
    }

    public String getDate() {
        return date;
    }

    public double getMorn_price() {
        return morn_price;
    }

    public double getAft_price() {
        return aft_price;
    }

    public int getPages_read() {
        return pages_read;
    }

    public int getCurr_num() {
        return curr_num;
    }

    public String getTicker() {
        return ticker;
    }
}
