package sigildesigns.booksearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static sigildesigns.booksearch.MainActivity.LOG_TAG;

/**
 * This is where the JSON will be pulled and parsed.
 */

public class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If url is null, return an empty string early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // Then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem receiving the JSON Response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Reads from the input stream and returns a usable string
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Creates a URL to use when getting JSON
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
            return null;
        }
        return url;
    }

    /**
     * Return a list of {@link Book} objects that has been built up from parsing a JSON response.
     */
    public static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, return early
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        /**
         * Try to parse the JSON, if there's a problem with the way the JSON is formatted, a
         * JSONException exception object will be thrown. Catch the exception so the app doesn't
         * crash, and print the error message to the logs.
         */
        try {
            // Create a new JSONObject named root which pulls JSON from a url
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray named "items" from the JSON
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            // Loop through the array
            for (int i = 0; i < booksArray.length(); i++) {
                // Collect a JSONObject
                JSONObject book = booksArray.getJSONObject(i);
                // extract the "volumeInfo" object
                JSONObject bookVolume = book.getJSONObject("volumeInfo");
                // Create variables for the title and information url
                String title = bookVolume.getString("title");
                Log.v(LOG_TAG, title);
                String url = bookVolume.getString("infoLink");
                // Extract the array for authors
                JSONArray authorsArray = bookVolume.getJSONArray("authors");
                String authors = "Written By: ";

                // Loop through the authorsArray and add them to the authors string
                // If there are no authors, replace with "No author listed"
                if (authorsArray.length() > 0) {
                    for (int a = 0; a < authorsArray.length(); a++) {
                        authors = authors + authorsArray.getString(a);
                        // If not at the end of the list of authors then add a comma and space
                        // between names
                        if (a != authorsArray.length() - 1) {
                            authors = authors + ", ";
                        }
                    }
                    // Add the book to the books ArrayList
                    books.add(new Book(title, authors, url));

                } else {
                    authors = "No author listed";
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the try block
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return books;
    }

    // Query the GoogleBooks API and return a list of {@link Book} objects.
    public static List<Book> fetchBookData(String requestUrl) {
        // Create a URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }
}
