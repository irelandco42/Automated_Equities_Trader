package com.etrade.exampleapp.mystuff;

import com.etrade.exampleapp.mystuff.outs.DiscordPrinter;
import com.etrade.exampleapp.v1.oauth.AuthorizationService;
import com.etrade.exampleapp.v1.terminal.ETClientApp;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Date;

public class MorningRunSynology {

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken("ODA1MTAzMDk1MDEzMDQ4MzMy.YBWArw.YzdoaKMpBqfQ8zWRquWrlFVnRxQ").login().join();
        MyLogger log = new MyLogger("/volume1/Scripts/logs" + new Date().toString().substring(4) + ".txt");
        DiscordPrinter printer = new DiscordPrinter("806639965395484681");

        printer.print("Initializing!");

        ETClientApp obj = new ETClientApp(args);
        AuthorizationService.loading = true;

        if (obj.hasOption("help")) {
            obj.printHelp();
            return;
        }

        obj.init(false, true);
        //obj.getAccountList(synology);
        obj.getPrice("ABNB");

        printer.print("Done!");

        //obj.getPrice("AAL");

		/*try {
			obj.keyMenu(obj);
		} catch (NumberFormatException e) {
			System.out.println(" Main menu : NumberFormatException ");
		} catch (IOException e) {
			System.out.println(" Main menu : System failure ");;
		}*/

        sleep(5000);

        log.close();

        System.exit(0);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
}
