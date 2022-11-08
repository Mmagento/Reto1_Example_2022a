package es.pruebas.reto1_example_2022.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import es.pruebas.reto1_example_2022.R;
import es.pruebas.reto1_example_2022.beans.Video;

/**
 * Simple adapter for the table
 */
public class MyTableAdapter extends ArrayAdapter<Video> {

    private final ArrayList <Video> listado;
    private final Context context;

    public MyTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Video> listado) {
        super( context, resource, listado );
        this.listado = listado;
        this.context = context;
    }

    @Override
    public int getCount (){
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate ( R.layout.myrow_layout, null);

        ((TextView) view.findViewById( R.id.userIdTextView)).setText(listado.get(position).getId() + "");
        ((TextView) view.findViewById( R.id.userNameTextView)).setText(listado.get(position).getTitle());
        ((TextView) view.findViewById( R.id.locationTextView)).setText(listado.get(position).getUrl());

        return view;
    }
}
