# Automated Equities Trader

This package contains the application I use to automatically buy and sell stocks on the market. 

Some notes:
* Any files I created are in contained in src/main/java/com/etrade/exampleapp/mystuff
* The rest of the package is the sample E*Trade application. However, I made modifications
to several files to suite my needs better.
  * I changed the OAuth process so it no longer redirects to a browser and allows me to
  give it a data file to read with the authorization code.
  * I modified the stock ordering client to actually be able to buy and sell stocks.
  previously, it could only give order previews, but now it can actually execute orders
  due to the changes I made to the HTML POST requests.
  * I changed the main client app to act more as a utility class, changing some methods,
  so they actually return values instead of just printing their results.
