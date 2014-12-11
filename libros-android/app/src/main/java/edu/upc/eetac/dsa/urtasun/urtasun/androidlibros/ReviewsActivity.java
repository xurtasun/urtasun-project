package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.AppException;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.LibroAPI;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.Review;
import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.ReviewCollection;

/**
 * Created by xavi on 11/12/14.
 */
public class ReviewsActivity extends ListActivity {

    private ArrayList<Review> reviewslist;
    private ReviewAdapter adapter;


    private final static String TAG = ReviewsActivity.class.toString();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewslist = new ArrayList<Review>();
        adapter = new ReviewAdapter(this, reviewslist);
        setListAdapter(adapter);

        (new FetchReviewsTask()).execute();
    }



    private class FetchReviewsTask extends
            AsyncTask<Void, Void, ReviewCollection> {
        private ProgressDialog pd;

        @Override
        protected ReviewCollection doInBackground(Void... params) {
            ReviewCollection reviews = null;
            try {
                reviews = LibroAPI.getInstance(ReviewsActivity.this)
                        .getReviews();
            } catch (AppException e) {
                e.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(ReviewCollection result) {
            addReviews(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ReviewsActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_libros, menu);
        return true;
    }

    private void addReviews(ReviewCollection reviews) {
        reviewslist.addAll(reviews.getReviews());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Review review = reviewslist.get(position);
        Log.d(TAG, review.getLinks().get("self").getTarget());

        Intent intent = new Intent(this, LibroDetailActivity.class);
        intent.putExtra("url", review.getLinks().get("self").getTarget());
        startActivity(intent);
    }

}
