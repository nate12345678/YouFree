package team.gif.greyshiftedapi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * Copyright 2017 Nathan Post
 * Created 3/5/17
 * Updated 5/2/18
 *
 * @Version 1.6
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitationsunder the License.
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * This class is an extension of the AppCompatActivity and is designed to make it easier to complete some common tasks
 * within apps without much effort. it includes static methods for manipulating text, as well as helper methods for
 * common tasks.
 */

public abstract class ExtendedActivity extends AppCompatActivity {

    /**
     * Stores whether or not the application is debuggable
     * As per Android guidelines, a public static variable is used instead of a private variable with a getter.
     */
    public static boolean debuggable = false;

    /**
     * Default constructor
     */
    public ExtendedActivity() {
        super();

        //No reason to check again if the variable is true, we already know it was set.
// 		if (!debuggable)
// 		debuggable = isDebuggable(getApplicationContext());
    }


    /**
     * performs a check to see if the app has a given permission
     *
     * @param permission the permission string to be checked
     * @return the status of the permission
     */
    @Core
    public boolean hasPermission(String permission) {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Helper method that checks to see if the phone is connected to the internet
     *
     * @return if the phone is connected to the internet
     */
    @Core
    public boolean isOnline() {
        boolean isConnected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isConnected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return isConnected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.w("connectivity", e.toString());
        }
        return isConnected;
    }


    /**
     * Method to determine if the app is debuggable in the current install. Used to find determine whether or not crash
     * logs should be reported.
     *
     * @param context the context of the app (needed to obtain info about the app)
     * @return whether or not the app is debuggable
     */
    private boolean isDebuggable(Context context) {
        final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
        boolean isDebuggable = false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature signatures[] = packageInfo.signatures;
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            for (Signature signature : signatures) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signature.toByteArray());
                X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(stream);
                isDebuggable = certificate.getSubjectX500Principal().equals(DEBUG_DN);
                if (isDebuggable) break;
            }
        } catch (PackageManager.NameNotFoundException | java.security.cert.CertificateException e) {
            isDebuggable = false; //Just to make code more readable
        }

        return isDebuggable;
    }


    /**
     * Helper method to hide the action bar, with built in error handling.
     *
     * @return success or failure of the method.
     */
    public boolean hideActionBar() {
        try {
            getSupportActionBar().hide();
            return true;
        } catch (NullPointerException e) {
            Log.e("ActionBarStatus", "Failed to hide action bar");
            return false;
        }

    }


    /**
     * Helper method to show the action bar, with built in error handling.
     *
     * @return success or failure of the method.
     */
    public boolean showActionBar() {
        try {
            getSupportActionBar().show();
            return true;
        } catch (NullPointerException e) {
            Log.e("ActionBarStatus", "Failed to show action bar");
            return false;
        }

    }


    /**
     * A simple, safe method to get text from an editText
     *
     * @param editText the textview to read from
     * @return the contained text
     */
    public static String readInput(EditText editText) {
        try {
            return editText.getText().toString();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * A helper method that tests a TextView to see if it contains a number
     */
    public static boolean viewHasNumber(TextView textView) {
        String text;
        double numText;
        try {
            text = textView.getText().toString();
        } catch (Exception e) {
            return false;
        }
        try {
            numText = Double.parseDouble(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * A helper method to retrieve a double from a TextView.
     */
    public static double getViewDouble(TextView textView) {
        return (viewHasNumber(textView)) ? Double.parseDouble(textView.getText().toString()) : 0.0;
    }


    /**
     * A helper method to retrieve a double from a TextView.
     */
    public static int getViewInt(TextView textView) {
        return (viewHasNumber(textView)) ? Integer.parseInt(textView.getText().toString()) : 0;
    }


    /**
     * an extremely simple method that post a warning to the log letting the programmer know if a certain point has
     * been
     * reached.
     *
     * @param point an integer that tells which point has been reached
     */
    public static void logTestPoint(int point) {
        Log.w("test", "This is point number " + point);
    }


    public interface permissionResponder {
        void onRequestPermissionResult(int requestCode,
                                       String permissions[], int[] grantResults);
    }

}
