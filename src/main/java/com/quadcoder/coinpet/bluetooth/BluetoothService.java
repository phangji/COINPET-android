package com.quadcoder.coinpet.bluetooth;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.quadcoder.coinpet.MyApplication;

public class BluetoothService extends Service {

    /*
    State
    Started : startService() 호출시. 일단 started되면, background에서 독립적으로 실행된다. (호출한 컴포넌트가 Destory되어도)
    일반적으로 시작된 서비스는 single operation으로 동작하고, caller에게 아무런 결과값도 리턴하지 않는다.
    예를 들어, 네트워크로부터 파일을 다운로드하거나 업로드 하고 나서 서비스는 자기 자신을 Stop해야 한다.

    Bound : 어플리케이션 컴포넌트는 bindService()를 호출함으로써 서비스는 bound가 된다.
    바운드된 서비스는 IPC 클라이언트-서버 인터페이스를 허용한다.
    바운드된 서비스는 바운드시킨 어플리케이션 컴포넌트가 지속되는 만큼만 동작한다.
    여러 컴포넌트들은 서비스에 한번 바인드할 수 있지만, 그들 모두를 언바인드했을 때 서비스가 destory된다.

    Although this documentation generally discusses these two types of services separately,
    your service can work both ways—it can be started (to run indefinitely) and also allow binding.
    It's simply a matter of whether you implement a couple callback methods:
    onStartCommand() to allow components to start it and onBind() to allow binding.
     */

    Handler mHandler;
    BluetoothManager mManager;

    public BluetoothService() {
        mHandler = ((MyApplication)getApplication()).getHandler();
        mManager = new BluetoothManager(this, mHandler);
    }


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public IBinder onBind(Intent intent) {  // IPC를 위해
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {  // 액티비티나 다른 컴포넌트에서 startService()를 호출했을 때
        return super.onStartCommand(intent, flags, startId);
    }

}
