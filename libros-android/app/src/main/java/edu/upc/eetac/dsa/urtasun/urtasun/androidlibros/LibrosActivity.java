package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.AppException;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.Libro;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.LibroAPI;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.LibroCollection;


public class LibrosActivity extends ListActivity {

    private ArrayList<Libro> libroslist;
    private LibroAdapter adapter;


    private final static String TAG = LibrosActivity.class.toString();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);

        libroslist = new ArrayList<Libro>();
        adapter = new LibroAdapter(this, libroslist);
        setListAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("libros-profile",
                Context.MODE_PRIVATE);
        final String username = prefs.getString("username", null);
        final String password = prefs.getString("password", null);
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password
                        .toCharArray());
            }
        });
        (new FetchLibrosTask()).execute();
    }



    private class FetchLibrosTask extends
            AsyncTask<Void, Void, LibroCollection> {
        private ProgressDialog pd;

        @Override
        protected LibroCollection doInBackground(Void... params) {
            LibroCollection libros = null;
            try {
                libros = LibroAPI.getInstance(LibrosActivity.this)
                        .getLibros();
            } catch (AppException e) {
                e.printStackTrace();
            }
            return libros;
        }

        @Override
        protected void onPostExecute(LibroCollection result) {
            addLibros(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LibrosActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }

    private void addLibros(LibroCollection libros) {
        libroslist.addAll(libros.getLibros());
        adapter.notifyDataSetChanged();
    }




    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Libro libro = libroslist.get(position);
        Log.d(TAG, libro.getLinks().get("self").getTarget());

        Intent intent = new Intent(this, LibroDetailActivity.class);
        intent.putExtra("url", libro.getLinks().get("self").getTarget());
        startActivity(intent);
    }

}
