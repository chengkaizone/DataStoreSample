package com.tony.datastoresample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mmkv.MMKV

class MMKVActivity: AppCompatActivity(), View.OnClickListener {

    lateinit var bt_write: Button
    lateinit var bt_read: Button
    lateinit var tv_write_time: TextView
    lateinit var tv_read_time: TextView

    companion object {
        const val TAG = "MMKVActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mmkv)

        bt_write = findViewById(R.id.bt_write)
        bt_read = findViewById(R.id.bt_read)
        tv_write_time = findViewById(R.id.tv_write_time)
        tv_read_time = findViewById(R.id.tv_read_time)

        bt_write.setOnClickListener(this)
        bt_read.setOnClickListener(this)

    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.bt_write ->
            {
                val start = System.currentTimeMillis()
                for (i in 0 until 10000) {
                    MMKV.defaultMMKV().encode("bool_$i", i % 2 == 0)
                }
                val duration = System.currentTimeMillis() - start

                tv_write_time.text = "write 10000 cost time: $duration ms"
            }
            R.id.bt_read ->
            {
                val start = System.currentTimeMillis()
                for (i in 0 until 10000) {
                    val value = MMKV.defaultMMKV().decodeBool("bool_$i")
                    //Log.e(TAG, "value: $value")
                }

                val duration = System.currentTimeMillis() - start

                tv_write_time.text = "read 10000 cost time: $duration ms"
            }
        }
    }


}