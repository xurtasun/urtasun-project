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

import edu.upc.eetac.dsa.urtasun.urtasun.androidlibros.api.Libro;


public class LibroAdapter extends BaseAdapter {

    private ArrayList<Libro> data;
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
        return ((Libro) getItem(position)).getLibroid();
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvAutor;
        TextView tvEdition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_libro, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tvTitle);
            viewHolder.tvAutor = (TextView) convertView
                    .findViewById(R.id.tvAutor);
            viewHolder.tvEdition = (TextView) convertView
                    .findViewById(R.id.tvEdition);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String title = data.get(position).getTitle();
        String autor = data.get(position).getAutor();
        String edition = data.get(position).getEdition();
        viewHolder.tvTitle.setText(title);
        viewHolder.tvAutor.setText(autor);
        viewHolder.tvEdition.setText(edition);
        return convertView;
    }

    public LibroAdapter(Context context, ArrayList<Libro> data) {
        super();
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

}
