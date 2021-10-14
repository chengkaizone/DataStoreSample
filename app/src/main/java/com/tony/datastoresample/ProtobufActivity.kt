package com.tony.datastoresample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*

/**
 * protobuf序列化和反序列化
 */
class ProtobufActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var et_input: EditText
    lateinit var bt_encode: Button
    lateinit var bt_decode: Button
    lateinit var tv_result: TextView
    lateinit var tv_bytes: TextView
    var currentBodys: ByteArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protobuf)
        et_input = findViewById(R.id.et_input)
        bt_encode = findViewById(R.id.bt_encode)
        bt_decode = findViewById(R.id.bt_decode)
        tv_result = findViewById(R.id.tv_result)
        tv_bytes = findViewById(R.id.tv_bytes)
        bt_encode.setOnClickListener(this)
        bt_decode.setOnClickListener(this)

        tv_result.movementMethod = ScrollingMovementMethod.getInstance()
        tv_bytes.movementMethod = ScrollingMovementMethod.getInstance()


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_encode -> {
                val text = et_input.text.toString().trim { it <= ' ' }
                val builder = FoodModelProto.FoodModel.newBuilder()
                builder.name = text
                builder.email = "email: $text"
                builder.id = System.currentTimeMillis().toInt()
                builder.sex = FoodModelProto.FoodModel.Sex.MALE
                val model = builder.build()
                currentBodys = model.toByteArray()
                tv_bytes.text = Arrays.toString(currentBodys)
            }
            R.id.bt_decode -> try {
                val model = FoodModelProto.FoodModel.parseFrom(currentBodys)
                val text =
                    model.id.toString() + " - " + model.name + " - " + model.email + " - " + model.sex.number
                tv_result.text = text
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}