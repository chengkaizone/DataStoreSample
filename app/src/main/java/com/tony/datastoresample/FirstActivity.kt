package com.tony.datastoresample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * Home
 */
class FirstActivity: AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        findViewById<View>(R.id.bt_mmkv).setOnClickListener(this)
        findViewById<View>(R.id.bt_protobuf).setOnClickListener(this)
        findViewById<View>(R.id.bt_preference_datastore).setOnClickListener(this)
        findViewById<View>(R.id.bt_proto_datastore).setOnClickListener(this)

    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.bt_mmkv ->
            {
                startActivity(Intent(this@FirstActivity, MMKVActivity::class.java))
            }
            R.id.bt_protobuf ->
            {
                startActivity(Intent(this@FirstActivity, ProtobufActivity::class.java))
            }
            R.id.bt_preference_datastore ->
            {
                startActivity(Intent(this@FirstActivity, PreferenceDataStoreActivity::class.java))
            }
            R.id.bt_proto_datastore ->
            {
                startActivity(Intent(this@FirstActivity, ProtoDataStoreActivity::class.java))
            }
        }
    }

}