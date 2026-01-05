package com.manya;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

public class App {
    static HashMap<String, JSONObject> cache = new HashMap<>();
    static int totalCalls = 0;
    static int successfulCalls = 0;
    static int failedCalls = 0;
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter up to 3 stock tickers (comma separated): ");
        String input = sc.nextLine();

        String[] tickers = input.split(",");

        if (tickers.length > 3) {
            System.out.println("Max 3 tickers allowed!");
            return;
        }
        System.out.println("Enter API Key: ");
        String apiKey = sc.nextLine();

        for (int i = 0; i < tickers.length; i++) {
            String ticker = tickers[i].trim().toUpperCase();
            fetchAndPrintStockData(ticker, apiKey);
            System.out.println("----------------------");
        }
        System.out.println("\n==== API STATS ====");
        System.out.println("Total API Calls: " + totalCalls);
        System.out.println("Successful Calls: " + successfulCalls);
        System.out.println("Failed Calls: " + failedCalls);

    if (totalCalls > 0) {
    double successRate = (successfulCalls * 100.0) / totalCalls;
    System.out.println("Success Rate: " + successRate + "%");
    }


        sc.close();
    }

    // this method does the stock logic
    public static void fetchAndPrintStockData(String ticker, String apiKey) {

        try {
            JSONObject json;

            // ✅ CACHE CHECK
            if (cache.containsKey(ticker)) {
                System.out.println("\n[Using cached data]");
                json = cache.get(ticker);
            } else {
                System.out.println("\n[Fetching from API]");
                totalCalls++;
                String urlStr = "https://finnhub.io/api/v1/quote?symbol="
                        + ticker + "&token=" + apiKey;

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner in = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();

                while (in.hasNext()) {
                    response.append(in.nextLine());
                }
                in.close();

                json = new JSONObject(response.toString());
                // 🚨 INVALID TICKER CHECK
                if (json.getDouble("c") == 0) {
                failedCalls++;
                System.out.println("Invalid ticker: " + ticker);
                return;
                }
                successfulCalls++;

                // save to cache
                cache.put(ticker, json);
            }

            double currentPrice = json.getDouble("c");
            double openPrice = json.getDouble("o");
            double high = json.getDouble("h");
            double low = json.getDouble("l");

            System.out.println("==== STOCK DATA FOR " + ticker + " ====");
            System.out.println("Current Price: " + currentPrice);
            System.out.println("Open: " + openPrice);
            System.out.println("High: " + high);
            System.out.println("Low: " + low);

        } catch (Exception e) {
            System.out.println("API error or invalid ticker");
            failedCalls++;
        }
    }
}
