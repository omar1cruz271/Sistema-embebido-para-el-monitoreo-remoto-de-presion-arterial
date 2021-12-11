package com.example.materialdesign.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.materialdesign.R;


public class MessagesFragment extends Fragment {

    SQLiteDatabase sqld;
    TextView jtvL;

    public MessagesFragment() {

        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        jtvL= (TextView) rootView.findViewById(R.id.xtvL);
        DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this.getActivity(), "DBpacientes", null, 1);
        sqld = dsqlh.getWritableDatabase();
        String nombre, apellidoP, apellidoM, curp, nacimiento, sexo, numero;
        try {

            Cursor c = sqld.rawQuery("SELECT * FROM Pacientes_Registrados", null);
            jtvL.setText(" CURP"+ "\t\t                                  Nombre"+ "\t      Numero" );
            if (c.moveToFirst()) {
                do {
                    curp = c.getString(0);
                    nombre = c.getString(1);
                    nacimiento=c.getString(4);
                    sexo = c.getString(5);
                    numero = c.getString(6);
                    jtvL.append("\n" + curp + "\t" + nombre +"\t        " +numero+ "\n");
                } while(c.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}