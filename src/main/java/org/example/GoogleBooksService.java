package org.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;


public class GoogleBooksService {
    private static final String API_KEY = "AIzaSyCi2gzpILaufWLs1G9Neo0w6DpmQSv4w7g";

    public Book fetchBookDetails(String isbn) throws IOException {
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return parseBookDetails(response.toString());
    }

    public Book parseBookDetails(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Kiểm tra sự tồn tại của trường "items" trước khi tiếp tục
            if (!jsonObject.has("items") || jsonObject.isNull("items")) {
                return null;
            }
            JSONArray items = jsonObject.getJSONArray("items");

            // Kiểm tra xem mảng "items" có dữ liệu không
            if (items != null && items.length() > 0) {
                JSONObject firstItem = items.getJSONObject(0);
                JSONObject volumeInfo = firstItem.getJSONObject("volumeInfo");

                String isbn = null;
                if (volumeInfo.has("industryIdentifiers")) {
                    JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                    for (int i = 0; i < industryIdentifiers.length(); i++) {
                        JSONObject identifier = industryIdentifiers.getJSONObject(i);
                        if (identifier.getString("type").equals("ISBN_13")) {
                            isbn = identifier.getString("identifier");
                            break;
                        }
                    }
                }

                String title = volumeInfo.optString("title", "Unknown Title");
                String author = volumeInfo.optJSONArray("authors") != null
                        ? volumeInfo.getJSONArray("authors").getString(0)
                        : "Unknown Author";
                String language = volumeInfo.optString("language", "Unknown Language");

                return new Book(isbn, title, author, language, 5);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing book details: " + e.getMessage());
        }
    }

}
