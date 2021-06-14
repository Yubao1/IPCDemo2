package com.xe.demo.ipcdemo2.ipcdemo2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.xe.demo.ipcdemo2.ipcserver.MyService

/**
 * Created by 86188 on 2021/6/11.
 */
class MainActivity: AppCompatActivity() {
    var mTv: TextView? = null
    var mMyServiceConnection: MyServiceConnection? = null
    var mMessengerClientHandler: MessengerClientHandler? = null
    var mClientMesstenger: Messenger? = null
    var mMessenger: Messenger? = null
    var num: Int = 0
    inner class MyServiceConnection: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mMessenger = Messenger(service)
        }

    }
    inner class MessengerClientHandler: Handler() {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg!!.what == 2) {
                var fromServiceContent: String = msg.data.getString("replyContent");
                mTv!!.setText(fromServiceContent)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTv = findViewById(R.id.tv);
        mMyServiceConnection = MyServiceConnection()
        mMessengerClientHandler = MessengerClientHandler()
        mClientMesstenger = Messenger(mMessengerClientHandler)
        bindService()
    }
    fun bindService() {
        var intent: Intent = Intent(this, MyService::class.java)
        bindService(intent,mMyServiceConnection, Context.BIND_AUTO_CREATE);
    }

    fun onClick(v: View) {
        sendMessage()
    }

    fun sendMessage() {
        num++
        var message: Message = Message.obtain(null,1)
        var bundle: Bundle = Bundle()
        bundle.putCharSequence("msg","这是客户端发送给服务器端的第" + num + "条消息")
        message.data = bundle
        message.replyTo = mClientMesstenger;
        try {
            mMessenger!!.send(message);
        } catch (e: RemoteException) {
            e.printStackTrace();
        }
    }
}