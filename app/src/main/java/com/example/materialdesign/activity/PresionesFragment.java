package com.example.materialdesign.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.example.materialdesign.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class PresionesFragment extends Fragment {
    SQLiteDatabase sqld;
    TextView jtvL;
    boolean secondFragment=false;

    public PresionesFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_presiones, container, false);
        String id, presionD, presionS, fecha, hora, numero;
        jtvL= (TextView) rootView.findViewById(R.id.xtvL);
        try {
            DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this.getActivity(), "DBpacientes", null, 1);
            sqld = dsqlh.getWritableDatabase();
            Cursor c = sqld.rawQuery("SELECT * FROM Pacientes", null);
            jtvL.setText(" id"+ "      Numero" +"      \tPresionS" +"   PresionD" +"\t  Fecha      Hora" + "\n");
            if (c.moveToFirst()) {
                do {
                    id = c.getString(0);
                    presionD = c.getString(1);
                    presionS=c.getString(2);
                    fecha = c.getString(3);
                    hora = c.getString(4);
                    numero = c.getString(5);
                    jtvL.append(" " + id + "\t" + numero +"\t        " + presionS +"\t       " + presionD +"\t       " +fecha+" "+hora+ "\n");
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