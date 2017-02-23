package qf.com.android_aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import qf.com.aidl.IMyAidl;

public class SecondActivity extends AppCompatActivity {
    private EditText et1;
    private EditText et2;
    private IBinder aidl;
    private ServiceConnection connection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(SecondActivity.this, "服务器绑定成功", Toast.LENGTH_SHORT).show();
            aidl = service;
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
        setContentView(R.layout.activity_second);
        et1 = ((EditText) findViewById(R.id.et1));
        et2 = ((EditText) findViewById(R.id.et2));
        result_tv = ((TextView) findViewById(R.id.result));
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bindServiceBtn:
                Intent intent = new Intent();
                intent.setAction("qf.com.aidl2");
                bindService(intent,connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.resultBtn:
                String s1 = et1.getText().toString();
                String s2 = et2.getText().toString();
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("CalcPlusService");
                data.writeInt(Integer.parseInt(s1));
                data.writeInt(Integer.parseInt(s2));
                try {
                    aidl.transact(0x110,data,reply,0);
                    reply.readException();
                    int result = reply.readInt();
                    result_tv.setText(String.valueOf(result));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }finally {
                    reply.recycle();
                    data.recycle();
                }
                break;
            case R.id.removeBindBtn:
                if (aidl!=null) {
                    unbindService(connection);
                    Toast.makeText(SecondActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
