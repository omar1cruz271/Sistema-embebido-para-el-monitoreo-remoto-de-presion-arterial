package com.example.materialdesign.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.materialdesign.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import  lecho.lib.hellocharts.*;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.renderer.AxesRenderer;
import lecho.lib.hellocharts.view.LineChartView;


public class GraficasFragment extends Fragment {
    SQLiteDatabase sqld;
    TextView jtvL;

    public GraficasFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graficas, container, false);

        String decimalPattern = "#.#";
        DecimalFormat decimalFormat = new DecimalFormat(decimalPattern);

        LineChartView lineChartView= (LineChartView)  rootView.findViewById(R.id.chart);
        dibujarGrafica(lineChartView,2, Color.RED);
        LineChartView lineChartView3= (LineChartView)  rootView.findViewById(R.id.chartDoble);
        dibujarGraficaDoble(lineChartView3);
        LineChartView lineChartView2= (LineChartView)  rootView.findViewById(R.id.chartDiastolic);
        dibujarGrafica(lineChartView2,1,Color.BLUE);




        return rootView;

    }
    public View dibujarGrafica(LineChartView lineChartView,Integer ValuesOnY, Integer color){

        List<PointValue> values = new ArrayList<PointValue>();
        List<Integer> valoresX= new ArrayList<Integer>();
        List<Integer> valoresY= new ArrayList<Integer>();
        values.add(new PointValue(0,0));
        try {
            DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this.getActivity(), "DBpacientes", null, 1);
            sqld = dsqlh.getWritableDatabase();
            Log.d("meto", "Me meto mucho ");
            Cursor c = sqld.rawQuery("SELECT * FROM Pacientes", null);

            if (c.moveToFirst()) {
                do {
                    valoresX.add(c.getInt(0));
                    valoresY.add(c.getInt(ValuesOnY));
                    Integer valuex=c.getInt(0);
                    // tempPointValue = new PointValue(valuex,valuex*15);
                    //tempPointValue.setLabel(decimalFormat.format(valuex*15));
                    values.add(new PointValue(valuex,c.getInt(ValuesOnY)));
                } while(c.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("values", values.toString());

        Line line = new Line(values)
                .setColor(color)
                .setCubic(false)
                .setHasPoints(true).setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        List<AxisValue> axisValuesForX = new ArrayList<>();
        List<AxisValue> axisValuesForY = new ArrayList<>();
        AxisValue tempAxisValue;
        for (float i = 0; i <= this.maxValue(valoresX); i += 1.0f){
            tempAxisValue = new AxisValue(i);
            tempAxisValue.setLabel(i+"");
            axisValuesForX.add(tempAxisValue);
            Log.d("Axisvalues", tempAxisValue.toString());
        }

        for (int i = 0; i <= this.maxValue(valoresY); i += 1){
            tempAxisValue = new AxisValue(i);
            tempAxisValue.setLabel(""+i);
            axisValuesForY.add(tempAxisValue);
        }

        Axis xAxis = new Axis(axisValuesForX);
        Axis yAxis = new Axis(axisValuesForY);
        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);


        lineChartView.setLineChartData(data);

        return lineChartView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public int maxValue(List<Integer> lista){
        int max=lista.get(0);
        for (Integer l: lista) {
            if (l>max) max=l;
        }
        return max;
    }
    public View dibujarGraficaDoble(LineChartView lineChartView){

        List<PointValue> values = new ArrayList<PointValue>();
        List<PointValue> valuesDiastol = new ArrayList<PointValue>();
        List<Integer> valoresX= new ArrayList<Integer>();
        List<Integer> valoresY= new ArrayList<Integer>();
        values.add(new PointValue(0,0));
        try {
            DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this.getActivity(), "DBpacientes", null, 1);
            sqld = dsqlh.getWritableDatabase();
            Log.d("meto", "Me meto mucho ");
            Cursor c = sqld.rawQuery("SELECT * FROM Pacientes", null);

            if (c.moveToFirst()) {
                do {
                    valoresX.add(c.getInt(0));
                    valoresY.add(c.getInt(2));
                    Integer valuex=c.getInt(0);
                    // tempPointValue = new PointValue(valuex,valuex*15);
                    //tempPointValue.setLabel(decimalFormat.format(valuex*15));
                    values.add(new PointValue(valuex,c.getInt(2)));
                    valuesDiastol.add(new PointValue(valuex,c.getInt(1)));
                } while(c.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("values", values.toString());
        Line lineaDiast= new Line(valuesDiastol)
                .setColor(Color.BLUE)
                .setCubic(false)
                .setHasPoints(true).setHasLabels(true);;
        Line line = new Line(values)
                .setColor(Color.RED)
                .setCubic(false)
                .setHasPoints(true).setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        lines.add(lineaDiast);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        List<AxisValue> axisValuesForX = new ArrayList<>();
        List<AxisValue> axisValuesForY = new ArrayList<>();
        AxisValue tempAxisValue;
        for (float i = 0; i <= this.maxValue(valoresX); i += 1.0f){
            tempAxisValue = new AxisValue(i);
            tempAxisValue.setLabel(i+"");
            axisValuesForX.add(tempAxisValue);
            Log.d("Axisvalues", tempAxisValue.toString());
        }

        for (int i = 0; i <= this.maxValue(valoresY); i += 1){
            tempAxisValue = new AxisValue(i);
            tempAxisValue.setLabel(""+i);
            axisValuesForY.add(tempAxisValue);
        }

        Axis xAxis = new Axis(axisValuesForX);
        Axis yAxis = new Axis(axisValuesForY);
        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);


        lineChartView.setLineChartData(data);

        return lineChartView;
    }

}