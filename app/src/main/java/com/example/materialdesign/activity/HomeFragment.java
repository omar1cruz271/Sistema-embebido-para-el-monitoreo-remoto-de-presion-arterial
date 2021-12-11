package com.example.materialdesign.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.materialdesign.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */


    public class HomeFragment extends Fragment {
        SQLiteDatabase sqld;
        String NumeroOrigen;
        String ID;
        String PresionD;
        String PresionS;
        String Fecha;
        String Hora;
        View rootView;
        TextView sistolica, diastolica, tvfecha,tvhora;
        public HomeFragment() {
            // Required empty public constructor
        }
        public HomeFragment(String ps, String pd,String date, String Hour){
            this.PresionS=ps;
            this.PresionD=pd;
            this.Fecha=date;
            this.Hora=Hour;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            sistolica=(TextView)rootView.findViewById(R.id.tvSisto);
            sistolica.setText(this.PresionS);
            diastolica=(TextView)rootView.findViewById(R.id.tvDiasto);
            diastolica.setText(this.PresionD);
            tvfecha=(TextView) rootView.findViewById(R.id.tvFecha);
            tvfecha.setText(this.Fecha);
            tvhora=(TextView) rootView.findViewById(R.id.tvHora);
            tvhora.setText(this.Hora);
            // Inflate the layout for this fragment
            return rootView;
        }
    recibeSMS receiver= new recibeSMS(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

            TextView tv=  (TextView) rootView.findViewById(R.id.xtvL);
            tv.setText("El número "+origen+ " ha enviado datos, presiona REGISTRO DE PRESIONES para ver la información");
            datos(cuerpoMSM,origen);
            if(consultaBD(origen)) {
                DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(context, "DBpacientes", null, 1);
                sqld = dsqlh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("presionS", PresionS);
                cv.put("presionD", PresionD);
                cv.put("fecha", Fecha);
                cv.put("hora", Hora);
                cv.put("numero", NumeroOrigen);
                sqld.insert("Pacientes", "id", cv);
                Toast.makeText(context, "Nuevo registro de presión recibido", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "No se ha registrado este número", Toast.LENGTH_SHORT).show();
            }

        }
    };
        public void datos(String body, String numero){
            String[] elementos=body.split(";");
            this.PresionD=elementos[0];
            this.PresionS=elementos[1];
            this.Fecha=ObtenerFecha();
            this.Hora=elementos[2];
            this.Hora+=":"+elementos[3];
            this.NumeroOrigen=numero;
        }
        public String ObtenerFecha(){

            Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
            Log.d( "ObtenerFecha: ",objDate.toString());
            String strDateFormat = "dd-MMM-yyyy"; // El formato de fecha está especificado
            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto
            Log.d("fecha",objSDF.format(objDate));
            return objSDF.format(objDate);
        }
        public boolean consultaBD(String numero) {
            String numeroConsulta;
            DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this.getActivity(), "DBpacientes", null, 1);
            sqld = dsqlh.getWritableDatabase();
            Cursor c = sqld.rawQuery("SELECT * FROM Pacientes_Registrados", null);
            if (c.moveToFirst()) {
                do {
                    numeroConsulta = c.getString(6);
                    if(numeroConsulta.equals(numero)) return true;
                } while(c.moveToNext());
            }
            return false;
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


