package com.etrade.exampleapp.mystuff;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class DataManager {

    private PrintWriter out;
    private Scanner sc;

    private final boolean write;

    public DataManager(String filepath, boolean _write) {
        write = _write;
        if (_write) {
            try {
                FileWriter fw = new FileWriter(filepath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        } else {
            try {
                sc = new Scanner(new File(filepath));
                sc.useDelimiter(",");
            } catch (FileNotFoundException e) {

            }
        }
    }

    public void write_line(String newLine) {
        out.println(newLine);
    }

    public String next_entry() {
        return sc.next();
    }

    public String next_line() {
        return sc.nextLine();
    }

    public void close() {
        if (write) {
            out.close();
        } else {

        }
    }

    public ArrayList<ArrayList<Company>> B_e_c_o_m_e_A_r_r_a_y_L_i_s_t() {
        ArrayList<ArrayList<Company>> temp_array = new ArrayList<>();

        String currDate = null;

        try {
            while (sc.hasNext()) {
                Company temp_company = new Company(sc.next());
                temp_company.curr_num = Integer.parseInt(sc.next());
                temp_company.morn_price = Double.parseDouble(sc.next());
                temp_company.aft_price = Double.parseDouble(sc.next());

                String temp_date = sc.next();
                if (currDate == null || !Objects.equals(currDate, temp_date)) {
                    currDate = temp_date;
                    temp_array.add(new ArrayList<>());
                }

                temp_company.date = temp_date;

                temp_company.pages_read = Integer.parseInt(sc.next());

                temp_array.get(temp_array.size() - 1).add(temp_company);

                sc.nextLine();
            }
        } catch (NoSuchElementException e) {
            return temp_array;
        }

        return temp_array;
    }
}