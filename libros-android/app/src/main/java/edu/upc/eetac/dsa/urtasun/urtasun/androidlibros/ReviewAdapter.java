package edu.upc.eetac.dsa.urtasun.urtasun.androidlibros;


/**
 * Created by xavi on 7/12/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.Review;

/**
 * Created by xavi on 11/12/14.
 */
public class ReviewAdapter extends BaseAdapter {


    private ArrayList<Review> data;
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Review) getItem(position)).getIdReview();
    }

    private static class ViewHolder {
        TextView tvUsername;
        TextView tvLastMofidied;
        TextView tvReviewText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_libro, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUsername = (TextView) convertView
                    .findViewById(R.id.tvUsername);
            viewHolder.tvLastMofidied = (TextView) convertView
                    .findViewById(R.id.tvLastModified);
            viewHolder.tvReviewText = (TextView) convertView
                    .findViewById(R.id.tvReviewText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String username = data.get(position).getUsername();
        String lastModified = SimpleDateFormat.getInstance().format(
                data.get(position).getLastmodified());
        String reviewText = data.get(position).getReviewText();
        viewHolder.tvUsername.setText(username);
        viewHolder.tvLastMofidied.setText(lastModified);
        viewHolder.tvReviewText.setText(reviewText);
        return convertView;
    }

    public ReviewAdapter(Context context, ArrayList<Review> data) {
        super();
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
}
