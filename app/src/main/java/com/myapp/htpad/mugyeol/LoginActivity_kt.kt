package com.myapp.htpad.mugyeol

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast

import com.google.gson.JsonObject
import com.myapp.htpad.mugyeol.util.MySharedPreference
import com.myapp.htpad.mugyeol.util.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity_kt : AppCompatActivity() {
    private var userid_et: EditText? = null
    private var pw_et: EditText? = null
    private var isChecboxClicked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val login_btn = findViewById<Button>(R.id.login_loginbtn)
        userid_et = findViewById(R.id.login_id)
        pw_et = findViewById(R.id.login_password)
        val checkBox = findViewById<CheckBox>(R.id.login_checkbox)


        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            //자동 로그인 확인 위한 변수
            isChecboxClicked = isChecked
        }


        //저장된 토큰 있으면 자동 로그인.
        if (!MySharedPreference.getInstance(this).getString("token").isEmpty()) {
            startActivity(Intent(this@LoginActivity_kt, CarlistActivity::class.java))
            finish()
        }
        login_btn.setOnClickListener {
            val userid = userid_et!!.text.toString()
            val pw = pw_et!!.text.toString()
            val deviceType = "android"

            RetrofitClient.restApi.login(userid, pw, deviceType).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val json = (if (response.body() != null) response.body()!!.get("data") else null) as JsonObject
                    val token = json?.get("token")?.asString

                    if (token != null) {
                        //자동 로그인 체크 여부 && 토큰 저장.
                        //앱 종료시 자동 로그인 false일 경우 shared data 삭제
                        if (isChecboxClicked) {
                            MySharedPreference.getInstance(this@LoginActivity_kt).putBoolean("boxcheck", isChecboxClicked)
                            MySharedPreference.getInstance(this@LoginActivity_kt).putString("token", token)
                        }
                        startActivity(Intent(this@LoginActivity_kt, CarlistActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity_kt, "로그인에 실패 하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d(TAG, "login fail $t")
                    Toast.makeText(this@LoginActivity_kt, "로그인에 실패 하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    companion object {
        private val TAG = "로그인액티비티"
    }
}
