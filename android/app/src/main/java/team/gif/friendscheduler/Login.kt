package team.gif.friendscheduler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException


class Login : AppCompatActivity() {

	private lateinit var usernameLabel: TextView
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
		createAcctText.text = "Login instead"
		loginButton.text = "Create"
		confirmPassLabel.visibility = View.VISIBLE
		confirmPassText.visibility = View.VISIBLE
		usernameLabel.visibility = View.VISIBLE
		usernameText.visibility = View.VISIBLE
		confirm = true
	}

	fun showLogin() {
		createAcctText.text = "Create a new account"
		loginButton.text = "Login"
		confirmPassLabel.visibility = View.GONE
		confirmPassText.visibility = View.GONE
		usernameLabel.visibility = View.GONE
		usernameText.visibility = View.GONE
		confirm = false
	}

	fun stayLogged(v: View) {
		Globals.stayLoggedOn = stayInCheck.isChecked
		var editor = getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
		editor.putBoolean("stayLoggedOn", Globals.stayLoggedOn)
		editor.apply()
	}

	fun loginFromView(v: View) {
		var password = ""
		if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
			password = passwordText.text.toString()
			Globals.enteredPass = password
			Globals.user = User(usernameText.text.toString(), emailText.text.toString())
			if (confirm) {
				if (confirmPassText.text.isNotEmpty() && passwordText.text.toString() == confirmPassText.text.toString()
					&& usernameText.text.isNotEmpty()
				) {
					addUser(password)
				} else {
					Snackbar.make(
						findViewById(R.id.loginCoordinator),
						"Passwords are required to match", Snackbar.LENGTH_LONG
					).show()
				}
			} else {
				login(password)
			}
			if (Globals.stayLoggedOn) {
				var editor = getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
				editor.putString("email", Globals.user.email)
				editor.putString("password", Globals.enteredPass)
				editor.apply()
			}
		}
	}

	fun login(password: String) {

		val request = Request.Builder()
			.url(Globals.BASE_URL + "/login")
			.addHeader("email", Globals.user.email)
			.addHeader("password", password)
			.get()
			.build()

		Log.w("loginrequest", "making request")

		client.newCall(request).enqueue(object : Callback {
			override fun onFailure(call: Call, e: IOException) {
				Log.w("test", "shit")
				if (e.message!!.contains("Failed to connect")) {
					Snackbar.make(
						findViewById(R.id.loginCoordinator),
						"Could not find server", Snackbar.LENGTH_SHORT
					).show()
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
					val user = response.body()!!.string()
					val token = response.header("token")!!
					response.body()!!.close()
					runOnUiThread {
						Globals.user = User.userFromJson(user)
						Globals.token = token
						startActivity(Intent(applicationContext, MainActivity::class.java))
						finish()
					}
				}
			}
		})
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
					Globals.JSON, "{" +
							"\"username\": \"" + Globals.user.username +
							"\",\"password\": \"" + password +
							"\",\"email\": \"" + Globals.user.email +
							"\"}"
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
					val user = response.body()!!.string()
					val token = response.header("token")!!
					response.body()!!.close()
					runOnUiThread {
						Globals.user = User.userFromJson(user)
						Globals.token = token
						startActivity(Intent(applicationContext, MainActivity::class.java))
						finish()
					}
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

		usernameLabel = findViewById(R.id.usernameLabel)
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
		usernameLabel.visibility = View.GONE
		usernameText.visibility = View.GONE


		var prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
		Globals.stayLoggedOn = prefs.getBoolean("stayLoggedOn", false)
		stayInCheck.isChecked = Globals.stayLoggedOn


		if (Globals.stayLoggedOn) {
			Globals.enteredPass = prefs.getString("password", "FAIL")
			Globals.user = User("DefaultUser", prefs.getString("email", "FAIL"))
			login(Globals.enteredPass)
		}

		createAcctText.setOnClickListener {
			if (confirm) {
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
				Snackbar.make(
					findViewById(R.id.loginCoordinator),
					"No connection to server, login may fail", Snackbar.LENGTH_SHORT
				).show()
//                e.printStackTrace()
			}

			@Throws(IOException::class)
			override fun onResponse(call: Call, response: Response) {
				if (response.code() == 200) {
					val output = response.body()!!.string()
					response.body()!!.close()
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
