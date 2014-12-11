package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros;

/**
 * Created by xavi on 10/12/14.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.AppException;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.Libro;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.LibroAPI;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.Review;

public class LibroDetailActivity extends Activity {
    private final static String TAG = LibroDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libro_detail_layout);
        String urlSting = (String) getIntent().getExtras().get("url");
        (new FetchStingTask()).execute(urlSting);
    }


    private void loadSting(Libro libro) {
        TextView tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
        TextView tvDetailAutor = (TextView) findViewById(R.id.tvDetailAutor);
        TextView tvDetailEditorial = (TextView) findViewById(R.id.tvDetailEditorial);
        TextView tvDetailDateCreation = (TextView) findViewById(R.id.tvDetailDateCreation);
        TextView tvDetailDateImpresion = (TextView) findViewById(R.id.tvDetailDateImpresion);
        TextView tvDetailEdition = (TextView) findViewById(R.id.tvDetailEdition);
        TextView tvDetailLanguage = (TextView) findViewById(R.id.tvDetailLanguage);
        TextView tvDetaliD = (TextView) findViewById(R.id.tvDetailiD);





        tvDetailTitle.setText("Titulo: "+libro.getTitle());
        tvDetailEditorial.setText("Editorial: "+libro.getEditorial());
        tvDetailAutor.setText("Autor: "+libro.getAutor());
        tvDetailDateCreation.setText("Creado: "+SimpleDateFormat.getInstance().format(
                libro.getDateCreation()));
        tvDetailDateImpresion.setText("Impreso: "+SimpleDateFormat.getInstance().format(
                libro.getDateImpresion()));
        tvDetailEdition.setText("Edition: "+libro.getEdition());
        tvDetailLanguage.setText("Idioma: "+libro.getLanguage());
        tvDetaliD.setText("id Libro: "+libro.getLibroid());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_libros, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_reviews:
                Intent intent = new Intent(this, ReviewsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class FetchStingTask extends AsyncTask<String, Void, Libro> {
        private ProgressDialog pd;

        @Override
        protected Libro doInBackground(String... params) {
            Libro libro = null;
            try {
                libro = LibroAPI.getInstance(LibroDetailActivity.this)
                        .getLibro(params[0]);
            } catch (AppException e) {
                Log.d(TAG, e.getMessage(), e);
            }
            return libro;
        }

        @Override
        protected void onPostExecute(Libro result) {
            loadSting(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LibroDetailActivity.this);
            pd.setTitle("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }



}