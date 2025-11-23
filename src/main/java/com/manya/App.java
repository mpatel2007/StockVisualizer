package com.manya;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter stock ticker: ");
        String ticker = sc.nextLine().toUpperCase();

        String apiKey;
        System.out.println("Enter API key: ");
        apiKey = sc.nextLine();
        String urlStr = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + apiKey;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner in = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNext()) {
                response.append(in.nextLine());
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());

            double currentPrice = json.getDouble("c");
            double openPrice = json.getDouble("o");
            double high = json.getDouble("h");
            double low = json.getDouble("l");

            System.out.println("\n==== STOCK DATA FOR " + ticker + " ====");
            System.out.println("Current Price: " + currentPrice);
            System.out.println("Open: " + openPrice);
            System.out.println("High: " + high);
            System.out.println("Low: " + low);

        } catch (Exception e) {
            System.out.println("Wrong ticker or API error: ");
        }
    }
}
