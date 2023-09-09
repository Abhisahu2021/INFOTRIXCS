import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {
    private String[] currencyCodes;
    private double[] exchangeRates;
    private int numCurrencies;
    private Map<String, Boolean> favoriteCurrencies;

    public CurrencyConverter() {
        currencyCodes = new String[100];
        exchangeRates = new double[100];
        favoriteCurrencies = new HashMap<>();
        numCurrencies = 0;
    }

    public void addFavoriteCurrency(String currencyCode) {
        if (!favoriteCurrencies.containsKey(currencyCode)) {
            favoriteCurrencies.put(currencyCode, true);
            currencyCodes[numCurrencies] = currencyCode;
            numCurrencies++;
        }
    }

    public void removeFavoriteCurrency(String currencyCode) {
        if (favoriteCurrencies.containsKey(currencyCode)) {
            favoriteCurrencies.remove(currencyCode);
            numCurrencies--;
        }
    }

    public void showFavoriteCurrencies() {
        System.out.println("Favorite Currencies:");
        for (String currencyCode : favoriteCurrencies.keySet()) {
            System.out.println(currencyCode);
        }
    }

    public void updateExchangeRates() throws IOException {
        String urlStr = "https://api.exchangerate.host/latest";

        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Read the JSON response
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();

        // Get the exchange rates object from JSON
        JsonObject rates = jsonObject.getAsJsonObject("rates");

        // Extract and update exchange rates
        for (String currencyCode : rates.keySet()) {
            double rate = rates.get(currencyCode).getAsDouble();
            updateExchangeRate(currencyCode, rate);
        }

        System.out.println("Exchange rates updated.");
    }

    private void updateExchangeRate(String currencyCode, double rate) {
        // Update exchange rate logic here
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        // Implement currency conversion logic using exchange rates
        return 0.0; // Replace with actual conversion logic
    }

    public static void main(String[] args) {
        CurrencyConverter converter = new CurrencyConverter();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Currency Converter Commands:");
            System.out.println("1. add <currencyCode> - Add a favorite currency");
            System.out.println("2. remove <currencyCode> - Remove a favorite currency");
            System.out.println("3. show - Show favorite currencies");
            System.out.println("4. update - Update exchange rates");
            System.out.println("5. convert <amount> <fromCurrency> to <toCurrency> - Convert currency");
            System.out.println("6. exit - Exit the program");
            System.out.print("Enter a command: ");
            String command = scanner.nextLine();
            String[] parts = command.split(" ");

            switch (parts[0]) {
                case "add":
                    if (parts.length == 2) {
                        converter.addFavoriteCurrency(parts[1]);
                        System.out.println(parts[1] + " added to favorites.");
                    } else {
                        System.out.println("Invalid command format.");
                    }
                    break;
                case "remove":
                    if (parts.length == 2) {
                        converter.removeFavoriteCurrency(parts[1]);
                        System.out.println(parts[1] + " removed from favorites.");
                    } else {
                        System.out.println("Invalid command format.");
                    }
                    break;
                case "show":
                    converter.showFavoriteCurrencies();
                    break;
                case "update":
                    try {
                        converter.updateExchangeRates();
                    } catch (IOException e) {
                        System.out.println("Failed to update exchange rates.");
                    }
                    break;
                case "convert":
                    if (parts.length == 5 && parts[3].equals("to")) {
                        double amount = Double.parseDouble(parts[1]);
                        String fromCurrency = parts[2];
                        String toCurrency = parts[4];
                        double result = converter.convertCurrency(amount, fromCurrency, toCurrency);
                        if (result != 0.0) {
                            System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, result, toCurrency);
                        } else {
                            System.out.println("Invalid currency conversion.");
                        }
                    } else {
                        System.out.println("Invalid command format.");
                    }
                    break;
                case "exit":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }
}
