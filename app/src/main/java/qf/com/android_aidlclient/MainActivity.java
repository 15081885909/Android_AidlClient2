package qf.com.android_aidlclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import qf.com.aidl.IMyAidl;

public class MainActivity extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private IMyAidl aidl;
    private ServiceConnection connection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "服务器绑定成功", Toast.LENGTH_SHORT).show();
            aidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidl = null;

        }
    };
    private TextView result_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = ((EditText) findViewById(R.id.et1));
        et2 = ((EditText) findViewById(R.id.et2));
        result_tv = ((TextView) findViewById(R.id.result));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bindServiceBtn:
                Intent intent = new Intent();
                intent.setAction("qf.com.aidl");
                bindService(intent,connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.resultBtn:
                String s1 = et1.getText().toString();
                String s2 = et2.getText().toString();
                if (aidl!=null) {
                    try {
                        int res = aidl.add(Integer.parseInt(s1), Integer.parseInt(s2));
                        result_tv.setText(String.valueOf(res));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(this, "服务被杀死，请重新绑定服务端", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.removeBindBtn:
                if (aidl!=null) {
                    unbindService(connection);
                    Toast.makeText(MainActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.binderBtn:
                startActivity(new Intent(this,SecondActivity.class));
                break;
        }

    }
}
