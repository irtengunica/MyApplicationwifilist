package com.example.okul.myapplicationwifilist;
import android.app.Activity;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ActionBarActivity {
    ListView lv;
    TextView tekdeger;
    WifiManager wifi;
    String wifis[];
    WifiScanReceiver wifiReciever;
    int degerbul=-1;
    Spinner spinner;
    TextView textView2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView)findViewById(R.id.listView);
        tekdeger=(TextView) findViewById(R.id.textview);
        spinner=(Spinner)findViewById(R.id.spinner);
        textView2=(TextView)findViewById(R.id.textView2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String[] dersDizisi = getResources().getStringArray(R.array.dersler);
                textView2.setText(wifis[position] + " aðýný seçtiniz!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifi.startScan();
        WifiConfiguration wifiConfig =new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", "Unutkan_K1");
        wifiConfig.preSharedKey = String.format("\"%s\"", "12345678");
        //WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int netId = wifi.addNetwork(wifiConfig);
        wifi.disconnect();
        wifi.enableNetwork(netId, true);
        wifi.reconnect();
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
        return super.onOptionsItemSelected(item);
    }

    private class WifiScanReceiver extends BroadcastReceiver{
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];
            for(int i = 0; i < wifiScanList.size(); i++){
                wifis[i] = ((wifiScanList.get(i)).toString());
                if(wifis[i].contains("Unutkan_K1")){
                    degerbul=i;
                }
                if(wifis[i].contains("SSID:")){
                    //wifis[i]=wifis[i].substring(wifis[i].indexOf("SSID:"), wifis[i].indexOf("BSSID:")-1);
                    wifis[i]=wifis[i].substring(5, wifis[i].indexOf("BSSID:")-2);
                }

                //myString.substring(myString.indexOf("dunya"), myString.length())
                //

            }

            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
            spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
            if(degerbul!=-1){
                //tekdeger.setText(wifis[degerbul]);
                tekdeger.setText("Unutkan_K1");
                /*WifiConfiguration wifiConfig =new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", "Unutkan_K1");
                wifiConfig.preSharedKey = String.format("\"%s\"", "12345678");
                //WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                int netId = wifi.addNetwork(wifiConfig);
                wifi.disconnect();
                wifi.enableNetwork(netId, true);
                wifi.reconnect();
                //programsonlanýnca baðlanýyor
                */

            }else{
                tekdeger.setText("Kablosuz aðlarýn listesinde aranýlan að bulunamadý");
            }



        }
    }
}