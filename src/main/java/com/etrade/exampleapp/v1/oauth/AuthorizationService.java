package com.etrade.exampleapp.v1.oauth;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.Scanner;

import com.etrade.exampleapp.v1.terminal.ETClientApp;
import org.apache.log4j.Logger;

import com.etrade.exampleapp.v1.exception.ApiException;
import com.etrade.exampleapp.v1.oauth.model.Message;
import com.etrade.exampleapp.v1.oauth.model.OAuthToken;
import com.etrade.exampleapp.v1.oauth.model.SecurityContext;
import com.etrade.exampleapp.v1.terminal.KeyIn;
/*
 * Send the user to authorize url with oauth token. On success, client prompts the user for verifier token available at authorization page.
 */
public class AuthorizationService implements Receiver{

	private Logger log = Logger.getLogger(AuthorizationService.class);

	private Receiver nextReceiver;

	public static boolean loading;

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {

		}
	}
	
	@Override
	public boolean handleMessage(Message message, SecurityContext context)throws  ApiException {
		log.debug(" AuthorizationService .. ");
		//if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				if( context.getToken() != null ) {
					
					OAuthToken token = context.getToken();

					String url = String.format("%s?key=%s&token=%s",context.getResouces().getAuthorizeUrl() ,context.getResouces().getConsumerKey(),token.getOauth_token());
					if (!ETClientApp.synology) {
						System.out.println(url);
					} else {
						ETClientApp.printer.print(url);
					}

					//Desktop.getDesktop().browse(new URI(url));

					String code = "";
					if (loading) {
						sleep(50000);
						ETClientApp.printer.print("10 Seconds Left!");
						sleep(10000);

						Scanner in;
						File readFile;
						if (!ETClientApp.synology) {
							readFile = new File("/Users/Ralph/Downloads/Code.txt");
						} else {
							readFile = new File("/volume1/Scripts/Code.txt");
						}
						try {
							in = new Scanner(readFile);
							code = in.next();
						} catch (FileNotFoundException e) {

						}
					} else {
						System.out.print( "Enter Verifier Code : " );
						code = KeyIn.getKeyInString();
					}

					log.debug("set code on to params " + code);

					message.setVerifierCode(code);

					if(nextReceiver != null) {
						nextReceiver.handleMessage(message, context);
					} else {
						log.error(" AuthorizationService : nextReceiver is null");
					}
				}else {
					return false;
				}

			} catch (Exception e) {
				log.error(e);
				throw new ApiException(500, "502", e.getMessage());
			} 
		//}else{
		//	log.error(" Launching default browser is not supported on current platform ");
		//}
		
		return false;
	}

	@Override
	public void handleNext(Receiver nextHandler) throws TokenException {
		this.nextReceiver = nextHandler;
	}
}
