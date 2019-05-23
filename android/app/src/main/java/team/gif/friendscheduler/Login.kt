package team.gif.friendscheduler

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException


class Login : AppCompatActivity() {

    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText
    private lateinit var createAcctText: TextView
    private lateinit var confirmPassLabel: TextView
    private lateinit var confirmPassText: EditText
    private lateinit var emailLabel: TextView
    private lateinit var emailText: EditText
    private lateinit var stayInCheck: CheckBox
    private lateinit var loginButton: Button

    internal var client = OkHttpClient()

    internal var confirm = false


    fun createAcct() {
        createAcctText.text = "Show Login"
        loginButton.text = "Create"
        confirmPassLabel.visibility = View.VISIBLE
        confirmPassText.visibility = View.VISIBLE
        emailLabel.visibility = View.VISIBLE
        emailText.visibility = View.VISIBLE
        confirm = true
    }

    fun showLogin() {
        createAcctText.text = "Create Account"
        loginButton.text = "Login"
        confirmPassLabel.visibility = View.GONE
        confirmPassText.visibility = View.GONE
        emailLabel.visibility = View.GONE
        emailText.visibility = View.GONE
        confirm = false
    }

    fun stayLogged(v: View) {
        Globals.stayLoggedOn = stayInCheck.isChecked
        var editor = getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
        editor.putBoolean("stayLoggedOn", Globals.stayLoggedOn)
        editor.apply()
    }

    fun login(v: View) {
        var password = ""
        if (usernameText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
            password = passwordText.text.toString()
            Globals.enteredPass = password
            if(confirm) {
                if(confirmPassText.text.isNotEmpty() && passwordText.text.toString() == confirmPassText.text.toString()
                    && emailText.text.isNotEmpty()) {
                    Globals.user = User(usernameText.text.toString(), emailText.text.toString())
                    addUser(password)
                } else {
                    Snackbar.make(findViewById(R.id.loginCoordinator),
                        "Passwords are required to match", Snackbar.LENGTH_LONG).show()
                }
            }
            Globals.user = User(usernameText.text.toString())

            val request = Request.Builder()
                .url(Globals.BASE_URL + "/login")
                .addHeader("username", Globals.user.username)
                .addHeader("password", password)
                .get()
                .build()

            Log.w("loginrequest", "making request")

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.w("test", "shit")
                    if(e.message!!.contains("Failed to connect")) {
                        Snackbar.make(findViewById(R.id.loginCoordinator),
                            "Could not find server", Snackbar.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    Log.w("loginrequest", response.code().toString())

                    if (!response.isSuccessful) {
                        if (response.code() == 404) {
                            runOnUiThread {
                                var snacc = Snackbar.make(
                                    findViewById(R.id.loginCoordinator),
                                    "User does not exist", Snackbar.LENGTH_LONG
                                )
                                showLogin()
                                snacc.setAction("New User") { createAcct() }
                                snacc.show()
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


    fun addUser(password: String) {
        Log.w(
            "test", "{\"username\": \"" + Globals.user.username +
                    "\",\"password\": \"" + password + "\",\"email\": \"" +
                    Globals.user.email + "\"}"
        )
        val request = Request.Builder()
            .url(Globals.BASE_URL + "/user")
            .post(
                RequestBody.create(
                    Globals.JSON, "{\"username\": \"" + Globals.user.username +
                            "\",\"password\": \"" + password + "\",\"email\": \"" +
                            Globals.user.email + "\"}"
                )
            )
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.w("test", "shit")

//                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Snackbar.make(findViewById(R.id.loginCoordinator), "User created", Snackbar.LENGTH_LONG).show()
                } else {
                    Log.w("test", "Unexpected code $response")
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        toolbar.title = "Login"
        setSupportActionBar(toolbar)

        usernameText = findViewById(R.id.usernameText)
        passwordText = findViewById(R.id.passwordText)
        createAcctText = findViewById(R.id.createAcctText)
        confirmPassLabel = findViewById(R.id.confirmPassLabel)
        confirmPassText = findViewById(R.id.confirmPassText)
        emailText = findViewById(R.id.emailText)
        emailLabel = findViewById(R.id.emailLabel)
        stayInCheck = findViewById(R.id.stayInCheck)
        loginButton = findViewById(R.id.loginButton)

        confirmPassLabel.visibility = View.GONE
        confirmPassText.visibility = View.GONE
        emailLabel.visibility = View.GONE
        emailText.visibility = View.GONE

        var prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        Globals.stayLoggedOn = prefs.getBoolean("stayLoggedOn", false)
        stayInCheck.isChecked = Globals.stayLoggedOn

        createAcctText.setOnClickListener{
            if(confirm) {
                showLogin()
            } else {
                createAcct()
            }
        }

        val request = Request.Builder()
            .url(Globals.BASE_URL + "/hello")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.w("test", "No connection to server")
                Snackbar.make(findViewById(R.id.loginCoordinator),
                    "No connection to server, login may fail", Snackbar.LENGTH_SHORT).show()
//                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() == 200) {
                    val output = response.body()!!.string()
                    Log.w("ApiTest", output)
                } else {
                    Log.w("ApiTest", "Api not functional")
                }
            }
        })
    }

/*
this section is only useful if there is a menu on the login screen (there isnt)
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
*/
}
