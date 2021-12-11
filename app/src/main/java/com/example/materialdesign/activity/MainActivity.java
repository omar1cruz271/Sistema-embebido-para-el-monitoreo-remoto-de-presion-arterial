package com.example.materialdesign.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.materialdesign.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        FragmentDrawer.FragmentDrawerListener {
    SQLiteDatabase sqld;
    String NumeroOrigen;
    String ID;
    String PresionD="...";
    String PresionS="...";
    String Fecha="...";
    String Hora="...";
    String channelID="channelID";
    String channelName="channelName";
    Integer notificationid=0;
    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)

                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)
                findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        // display the first navigation drawer view on app launch
        displayView(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    recibeSMS receiver= new recibeSMS(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel();
                Notification.Builder notificacion = new Notification.Builder(context,channelID);
                notificacion.setPriority(Notification.PRIORITY_HIGH);
                notificacion.setContentTitle("Se recibió nueva presión arterial");
                notificacion.setContentText("El número "+origen+" ha enviado un registro de presión");
                notificacion.setSmallIcon(R.drawable.ic_presion);
                NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(context);
                notificationManager.notify(notificationid,notificacion.build());
            }
            Toast.makeText(getApplicationContext(), "El número "+origen+ " ha enviado datos",
                    Toast.LENGTH_LONG).show();

            datos(cuerpoMSM,origen);
            Log.d("Meti","Me metí solo arregla esto");
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
                displayView(0);
                Toast.makeText(context, "Nuevo registro de presión recibido", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "No se ha registrado este número", Toast.LENGTH_SHORT).show();
            }

        }
    };
    public void datos(String body, String numero){
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        int minutes = rightNow.get(Calendar.MINUTE);
        String[] elementos=body.split(";");
        this.PresionS=elementos[0];
        this.PresionD=elementos[1];
        this.Fecha=ObtenerFecha();
        this.Hora=currentHourIn24Format+"";
        this.Hora+=":"+minutes;
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
        DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this, "DBpacientes", null, 1);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!",
                Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver , filter);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        int importance= NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel;
        channel = new NotificationChannel(channelID, channelName,importance);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the receiver to save unnecessary system overhead
        // Paused activities cannot receive broadcasts anyway
        try {
            if(receiver != null){
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment(this.PresionS,this.PresionD,this.Fecha,this.Hora);
                title = getString(R.string.title_home);
                break;
            case 2:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;
            case 1:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;
            case 3:
                fragment = new PresionesFragment();
                title = getString(R.string.title_presiones);
                break;
            case 4:
                fragment = new GraficasFragment();
                title = getString(R.string.title_graficas);
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}

