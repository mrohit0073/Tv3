package com.tvremote.tv;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import java.net.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout root;
    TextView service, network, pointer;
    int dp(float v){return Math.round(v*getResources().getDisplayMetrics().density);}

    TextView label(String s,int size){
        TextView t=new TextView(this); t.setText(s); t.setTextColor(Color.WHITE); t.setTextSize(size); t.setPadding(dp(12),dp(8),dp(12),dp(8)); return t;
    }

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(34),dp(25),dp(34),dp(25)); root.setBackgroundColor(Color.rgb(7,10,15));
        TextView title=label("TV REMOTE  V3",30); title.setTypeface(null,1); root.addView(title);
        service=label("",18); root.addView(service);
        network=label("Network: starting…",18); root.addView(network);
        pointer=label("",18); root.addView(pointer);

        Button a=new Button(this); a.setText("OPEN ACCESSIBILITY SETTINGS"); a.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))); root.addView(a);
        Button test=new Button(this); test.setText("TEST / CENTER CURSOR"); test.setOnClickListener(v->{
            if(RemoteAccessibilityService.isRunning()) {
                // Send through local loopback to the service's own TCP server.
Button test=new Button(this); test.setText("TEST / CENTER CURSOR"); test.setOnClickListener(v->{
    if(RemoteAccessibilityService.isRunning()) {
        new Thread(new Runnable() {
            @Override public void run() {
                Socket socket = null;
                BufferedWriter writer = null;
                try {
                    socket = new Socket("127.0.0.1", 45456);
                    writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
                    writer.write("CURSOR_CENTER\\n");
                    writer.flush();
                } catch (Exception ignored) {
                } finally {
                    try { if (writer != null) writer.close(); } catch (Exception ignored) {}
                    try { if (socket != null) socket.close(); } catch (Exception ignored) {}
                }
            }
        }).start();
    }
}); root.addView(test);
            }
        }); root.addView(test);
        Button info=new Button(this); info.setText("REFRESH STATUS"); info.setOnClickListener(v->refresh()); root.addView(info);
        TextView help=label("\nSETUP\n1. Enable TV Remote Receiver V3 in Accessibility.\n2. Return here and press TEST / CENTER CURSOR.\n3. Keep this receiver installed and running.\n4. Open TV Remote V3 on your phone.\n\nThe phone and TV must be on the same Wi‑Fi network.",16); root.addView(help);
        setContentView(root); refresh();
    }

    @Override protected void onResume(){super.onResume();refresh();}
    void refresh(){
        boolean run=RemoteAccessibilityService.isRunning();
        service.setText("Accessibility: "+(run?"● ENABLED":"● NOT ENABLED"));
        service.setTextColor(run?Color.rgb(0,230,118):Color.rgb(255,82,82));
        network.setText("Receiver: 192.168.x.x:45456  •  "+(RemoteAccessibilityService.isConnected()?"PHONE CONNECTED":"Waiting for phone"));
        pointer.setText("Cursor: "+(run?"READY":"UNAVAILABLE"));
    }
}
