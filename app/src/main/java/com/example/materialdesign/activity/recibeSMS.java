package com.example.materialdesign.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class recibeSMS extends BroadcastReceiver {
    String origen="";
    String cuerpoMSM="";
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle= intent.getExtras();
            SmsMessage[] msgs;

            if( bundle!= null){
                try {
                    Object[] pdus= (Object[]) bundle.get("pdus");
                    msgs= new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i]= SmsMessage.createFromPdu((byte[])pdus[i]);
                        origen=msgs[i].getOriginatingAddress();
                        cuerpoMSM= msgs[i].getMessageBody();

                       // Toast.makeText(context, "Mensaje de: "+ origen + " contenido: "+ cuerpoMSM, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}
