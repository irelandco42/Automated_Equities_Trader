package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.v1.oauth.AuthorizationService;
import com.etrade.exampleapp.v1.terminal.ETClientApp;
import org.springframework.security.access.method.P;

import javax.sound.sampled.Port;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class StockUtil {

    static ETClientApp etr;
    static String portfolio_filepath;

    public static void init(String _portfolio_filepath, boolean synology) {
        etr = new ETClientApp(new String[] {""});
        AuthorizationService.loading = synology;
        etr.init(true, synology);

        portfolio_filepath = _portfolio_filepath;

        etr.getAccountList(synology);
    }

    public static double getPrice(String ticker) {
        return etr.getPrice(ticker);
    }

    public static double get_current_cash_power() {
        double value_sum = 0;

        try {
            Scanner sc = new Scanner(new File(portfolio_filepath));
            sc.useDelimiter(",");

            // The first row contains the extra cash on hand
            sc.next();
            value_sum += Double.parseDouble(sc.next());
            sc.nextLine();

            while (sc.hasNext()) {
                String ticker = sc.next();
                int num = Integer.parseInt(sc.next());

                value_sum += etr.getPrice(ticker) * num;

                sc.nextLine();
            }
        } catch (FileNotFoundException e) {

        }

        return value_sum;
    }

    public static Portfolio get_current_portfolio() {

        ArrayList<String> tickers = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(portfolio_filepath));
            sc.useDelimiter(",");

            // Skip the first line
            sc.nextLine();

            while (sc.hasNext()) {
                tickers.add(sc.next());
                amounts.add(Integer.parseInt(sc.next()));

                sc.nextLine();
            }
        } catch (FileNotFoundException e) {

        }

        return new Portfolio(tickers, amounts);
    }

    public static Portfolio diff_portfolio(String[] selected_tickers) {
        double total_value = get_current_cash_power();
        double segmented_value = total_value / selected_tickers.length;
        double leftover_value = 0;

        int[] targ_nums = new int[selected_tickers.length];
        double[] prices = new double[selected_tickers.length];

        for (int i = 0; i < selected_tickers.length; i++) {
            prices[i] = etr.getPrice(selected_tickers[i]);
            targ_nums[i] = (int) (segmented_value / prices[i]);
            leftover_value += segmented_value - (targ_nums[i] * prices[i]);
        }

        System.out.println(leftover_value);
        System.out.println(Arrays.toString(selected_tickers));
        System.out.println(Arrays.toString(targ_nums));

        for (int i = 0; i < selected_tickers.length; i++) {
            if (leftover_value > prices[i]) {
                int extra = (int) (leftover_value / prices[i]);
                targ_nums[i] += extra;
                leftover_value -= extra * prices[i];
            }
        }

        System.out.println();
        System.out.println(leftover_value);
        System.out.println(Arrays.toString(selected_tickers));
        System.out.println(Arrays.toString(targ_nums));

        Portfolio current = get_current_portfolio();
        boolean[] current_accounted = new boolean[current.tickers.size()];

        boolean[] targ_accounted = new boolean[selected_tickers.length];

        ArrayList<String> all_tickers = new ArrayList<>();
        ArrayList<Integer> diffs = new ArrayList<>();
        for (int i = 0; i < selected_tickers.length; i++) {
            for (int j = 0; j < current.tickers.size(); j++) {
                if (Objects.equals(current.tickers.get(j), selected_tickers[i])) {
                    all_tickers.add(selected_tickers[i]);
                    diffs.add(targ_nums[i] - current.amounts.get(j));
                    current_accounted[j] = true;
                    targ_accounted[i] = true;
                    j = current.tickers.size() + 1;
                }
            }
        }

        for (int i = 0; i < selected_tickers.length; i++) {
            if (!targ_accounted[i]) {
                all_tickers.add(selected_tickers[i]);
                diffs.add(targ_nums[i]);
            }
        }

        for (int j = 0; j < current.tickers.size(); j++) {
            if (!current_accounted[j]) {
                all_tickers.add(current.tickers.get(j));
                diffs.add(-current.amounts.get(j));
            }
        }

        System.out.println();
        System.out.println(all_tickers);
        System.out.println(diffs);

        return new Portfolio(all_tickers, diffs);
    }

    public static void update_portfolio_file(Portfolio diff_portfolio) {
        Portfolio curr = get_current_portfolio();

        
    }

    public static void buy_and_sell(Portfolio diff_portfolio) {
        for (int i = 0; i < diff_portfolio.tickers.size(); i++) {
            String tick = diff_portfolio.tickers.get(i);
            int amount = diff_portfolio.amounts.get(i);

            if ((getPrice(tick) * amount) < get_actual_cash() || amount < 0) {
                //BUY BUY BUY
                //etr.placeOrder(tick, amount);
            }
        }
    }

    public static double get_actual_cash() {
        return etr.get_cash();
    }
}
