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
import es.pruebas.reto1_example_2022.beans.Cancion;

/**
 * Simple adapter for the table
 */
public class MyTableAdapter extends ArrayAdapter<Cancion> {

    private final ArrayList <Cancion> listadoCancion;
    private final Context context;

    public MyTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Cancion> listadoCancion) {
        super( context, resource, listadoCancion );
        this.listadoCancion = listadoCancion;
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

        ((TextView) view.findViewById( R.id.userIdTextView)).setText(listadoCancion.get(position).getId() + "");
        ((TextView) view.findViewById( R.id.autorTextView)).setText(listadoCancion.get(position).getAutor());
        ((TextView) view.findViewById( R.id.titulotextView)).setText(listadoCancion.get(position).getTitulo());
        //((TextView) view.findViewById( R.id.urlTextView)).setText(listadoCancion.get(position).getUrl());

        return view;
    }
}
