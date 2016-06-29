package com.codekul.comman;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IKeyListener listener = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnBind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent("com.codekul.intconverter");

                bindService(convertImplicitIntentToExplicitIntent(intent), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {

                        listener = IKeyListener.Stub
                                .asInterface(service);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btnConvert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.i("@codekul", String.valueOf(listener.hereIsKey(12)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Intent convertImplicitIntentToExplicitIntent(Intent implicitIntent) {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component =
                new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);

        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
