package team.gif.friendscheduler

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException
import java.lang.Long


class Login : AppCompatActivity() {

    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText

    internal var client = OkHttpClient()


    fun login(v: View) {
        var password = ""
        if (usernameText.text.isNotEmpty()) {
            if (passwordText.text.isNotEmpty()) {
                password = passwordText.text.toString()
            }
            Globals.user = User(usernameText.text.toString())

            val request = Request.Builder()
                .url(Globals.BASE_URL + "/login")
                .addHeader("username", Globals.user.username)
                .addHeader("password", password)
                .get()
                .build()



            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.w("test", "shit")

                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        Log.w("test", "Unexpected code $response")
                        if (response.code() != 401) {
                            val request = Request.Builder()
                                .url(Globals.BASE_URL + "/user")
                                .addHeader("username", Globals.user.username)
                                .addHeader("password", password)
                                .addHeader("email", Globals.user.email)
                                .addHeader("displayName", Globals.user.displayName)
                                .post(RequestBody.create(null, ""))
                                .build()

                            val newResponse = client.newCall(request).execute()
                            if (newResponse.isSuccessful) {
                                val requestSignin = Request.Builder()
                                    .url(Globals.BASE_URL + "/login")
                                    .addHeader("username", Globals.user.username)
                                    .addHeader("password", password)
                                    .get()
                                    .build()
                                val signinResponse = client.newCall(requestSignin).execute()
                                if (signinResponse.isSuccessful) {
                                    val user =
                                        signinResponse.body()!!.string() // TODO: convert from JSON to Java object
                                    val token = java.lang.Long.parseLong(signinResponse.header("token")!!)
                                    runOnUiThread {
                                        Globals.user = User.userFromJson(user)
                                        Globals.token = token
                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                    }
                                }
                            }
                        }

                    } else {
                        val user = response.body()!!.string() // TODO: convert from JSON to Java object
                        val token = java.lang.Long.parseLong(response.header("token")!!)
                        runOnUiThread {
                            Globals.user = User.userFromJson(user)
                            Globals.token = token
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        }
                    }
                }
            })

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        usernameText = findViewById(R.id.usernameText)
        passwordText = findViewById(R.id.passwordText)

        val request = Request.Builder()
            .url(Globals.BASE_URL + "/hello")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.w("test", "shit")

                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if(response.code() == 200) {
                    val output = response.body()!!.string()

                    runOnUiThread {
                        Snackbar.make(usernameText, output, Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    runOnUiThread {
                        Snackbar.make(usernameText, "failed to connect", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
