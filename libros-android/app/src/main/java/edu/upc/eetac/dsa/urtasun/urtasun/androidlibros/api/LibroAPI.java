package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api;

/**
 * Created by xavi on 7/12/14.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LibroAPI {
    private final static String TAG = LibroAPI.class.getName();
    private static LibroAPI instance = null;
    private URL url;

    private LibroRootAPI rootAPI = null;

    private LibroAPI(Context context) throws IOException, AppException {
        super();

        AssetManager assetManager = context.getAssets();
        Properties config = new Properties();
        config.load(assetManager.open("config.properties"));
        String urlHome = config.getProperty("libro.home");
        url = new URL(urlHome);

        Log.d("LINKS", url.toString());
        getRootAPI();
    }

    public final static LibroAPI getInstance(Context context) throws AppException {
        if (instance == null)
            try {
                instance = new LibroAPI(context);
            } catch (IOException e) {
                throw new AppException(
                        "Can't load configuration file");
            }
        return instance;
    }

    private void getRootAPI() throws AppException {
        Log.d(TAG, "getRootAPI()");
        rootAPI = new LibroRootAPI();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Libro API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, rootAPI.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Libros API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Libros Root API");
        }

    }

    public LibroCollection getLibros() throws AppException {
        Log.d(TAG, "getLibros()");
        LibroCollection libros = new LibroCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("libros").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Libros API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, libros.getLinks());

            libros.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
            libros.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
            JSONArray jsonLibros = jsonObject.getJSONArray("libros");
            for (int i = 0; i < jsonLibros.length(); i++) {
                Libro libro = new Libro();
                JSONObject jsonLibro = jsonLibros.getJSONObject(i);
                libro.setAutor(jsonLibro.getString("autor"));
                libro.setDateCreation(jsonLibro.getLong("dateCreation"));
                libro.setDateImpresion(jsonLibro.getLong("dateImpresion"));
                libro.setEdition(jsonLibro.getString("edition"));
                libro.setEditorial(jsonLibro.getString("editorial"));
                libro.setIdautor(jsonLibro.getInt("idautor"));
                libro.setLanguage(jsonLibro.getString("language"));
                libro.setLibroid(jsonLibro.getInt("libroid"));
                libro.setTitle(jsonLibro.getString("title"));
                jsonLinks = jsonLibro.getJSONArray("links");
                parseLinks(jsonLinks,libro.getLinks());
                libros.getLibros().add(libro);

            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Libros API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Libros Root API");
        }

        return libros;
    }


    public ReviewCollection getReviews() throws AppException {
        Log.d(TAG, "getReviews()");
        ReviewCollection reviews = new ReviewCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("allReviews").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Libros API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, reviews.getLinks());
            JSONArray jsonReviews = jsonObject.getJSONArray("reviews");
            for (int i = 0; i < 3; i++) {
                Review review = new Review();
                JSONObject jsonReview = jsonReviews.getJSONObject(i);
                review.setIdLibro(jsonReview.getInt("idLibro"));
                review.setIdReview(jsonReview.getInt("idReview"));
                review.setLastmodified(jsonReview.getLong("lastModified"));
                review.setName(jsonReview.getString("nameReviewer"));
                review.setReviewText(jsonReview.getString("reviewtext"));
                review.setUsername(jsonReview.getString("usernameReviewer"));

                jsonLinks = jsonReview.getJSONArray("links");
                parseLinks(jsonLinks,review.getLinks());
                reviews.getReviews().add(review);

            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Libros API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Libros Root API");
        }

        return reviews;
    }



    private Map<String, Libro> librosCache = new HashMap<String, Libro>();

    public Libro getLibro(String urlLibro) throws AppException {
        Libro libro = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlLibro);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            libro = librosCache.get(urlLibro);
            String eTag = (libro == null) ? null : libro.getETag();
            if (eTag != null)
                urlConnection.setRequestProperty("If-None-Match", eTag);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.d(TAG, "CACHE");
                return librosCache.get(urlLibro);
            }
            Log.d(TAG, "NOT IN CACHE");
            libro = new Libro();
            eTag = urlConnection.getHeaderField("ETag");
            libro.setETag(eTag);
            librosCache.put(urlLibro, libro);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonLibro = new JSONObject(sb.toString());
            libro.setAutor(jsonLibro.getString("autor"));
            libro.setDateCreation(jsonLibro.getLong("dateCreation"));
            libro.setDateImpresion(jsonLibro.getLong("dateImpresion"));
            libro.setEdition(jsonLibro.getString("edition"));
            libro.setEditorial(jsonLibro.getString("editorial"));
            libro.setIdautor(jsonLibro.getInt("idautor"));
            libro.setLanguage(jsonLibro.getString("language"));
            libro.setLibroid(jsonLibro.getInt("libroid"));
            libro.setTitle(jsonLibro.getString("title"));
            JSONArray jsonLinks = jsonLibro.getJSONArray("links");
            parseLinks(jsonLinks,libro.getLinks());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Bad sting url");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception when getting the sting");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception parsing response");
        }

        return libro;
    }






    private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
            throws AppException, JSONException {
        for (int i = 0; i < jsonLinks.length(); i++) {
            Link link = null;
            try {
                link = SimpleLinkHeaderParser
                        .parseLink(jsonLinks.getString(i));
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
            String rel = link.getParameters().get("rel");
            String rels[] = rel.split("\\s");
            for (String s : rels)
                map.put(s, link);
        }
    }
}
