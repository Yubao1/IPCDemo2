package com.xe.demo.ipcdemo2.ipcserver

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

/**
 * Created by 86188 on 2021/6/11.
 */
class MyService: Service() {
    var mHandler: MessengerServiceHandler? = null;
    var TAG: String  = "MyService";
    var num: Int = 0
    var mServiceMesstenger: Messenger? = null;
    override fun onBind(intent: Intent?): IBinder {
        return mServiceMesstenger!!.binder
    }

    override fun onCreate() {
        super.onCreate()
        mHandler = MessengerServiceHandler()
        mServiceMesstenger = Messenger(mHandler)
    }

    inner class MessengerServiceHandler: Handler() {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg!!.what == 1) {
                num++
                var content: String = msg!!.data.getString("msg")
                Log.d(TAG,content)
                var clientMessenger: Messenger = msg.replyTo
                var message: Message  = Message.obtain(null,2)
                var replyContent: String = "发送给客户端" + num + "条消息"
                var bundle: Bundle = Bundle()
                bundle.putString("replyContent",replyContent)
                message.setData(bundle);
                try {
                    clientMessenger.send(message);
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
        }
    }
}