package com.example.materialdesign.activity;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.materialdesign.R;

public class FriendsFragment extends Fragment {
    SQLiteDatabase sqld;
    Spinner s;
    Intent itn;
    Button registrar;
    String nombre, apellidoP, apellidoM, curp, nacimiento, sexo, numero;
    EditText nombreET, apellidoPET, apellidoMET, curpET, nacimientoET, numeroET;
    DbmsSQLiteHelper2 dsqlh = new DbmsSQLiteHelper2(this.getActivity(), "DBpacientes", null, 1);

    public FriendsFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        registrar=(Button) rootView.findViewById(R.id.registrar);
        numeroET= (EditText) rootView.findViewById(R.id.numero);
        nombreET= (EditText) rootView.findViewById(R.id.editNombre);
        apellidoPET=(EditText) rootView.findViewById(R.id.ApellidoP);
        apellidoMET=(EditText) rootView.findViewById(R.id.ApellidoM);
        curpET= (EditText) rootView.findViewById(R.id.curp);
        nacimientoET= (EditText) rootView.findViewById((R.id.nacimiento));

        s = (Spinner) rootView.findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> av, View v, int i, long l){
                sexo=s.getSelectedItem().toString();
                Toast.makeText( FriendsFragment.this.getActivity(), s.getSelectedItem().toString(),
                        Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0){ }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nombre = nombreET.getText().toString();
                apellidoP=apellidoPET.getText().toString();
                apellidoM=apellidoMET.getText().toString();
                curp=curpET.getText().toString();
                nacimiento=nacimientoET.getText().toString();
                numero=numeroET.getText().toString();
                DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(FriendsFragment.this.getActivity(), "DBpacientes", null, 1);
                sqld = dsqlh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("nombre",nombre);
                cv.put("apellidoP",apellidoP);
                cv.put("apellidoM",apellidoM);
                cv.put("nacimiento",nacimiento);
                cv.put("numero", numero);
                cv.put("curp", curp);
                cv.put("sexo", sexo);
                sqld.insert("Pacientes_Registrados", null, cv);
                Toast.makeText(FriendsFragment.this.getActivity(), "Se ha registrado a "+nombre, Toast.LENGTH_LONG).show();
                itn = new Intent(FriendsFragment.this.getActivity(), MainActivity.class);
                startActivity(itn);
            }
        });
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