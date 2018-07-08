package com.example.shivamkumar.automation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import android.util.Log;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

import java.util.concurrent.TimeUnit;

public class getotp extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    private static final String TAG = "PhoneAuth";
    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getotp);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);


        //getting mobile number from the previous activity
        //and sending the verification code to the number

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        try {
            sendVerificationCode(mobile);


            //if the automatic sms detection did not work, user can also enter the code manually
            //so adding a click listener to the button
            findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = editTextCode.getText().toString().trim();
                    if (code.isEmpty() || code.length() < 6) {
                        editTextCode.setError("Enter valid code");
                        editTextCode.requestFocus();
                        return;
                    }

                    //verifying the code entered manually
                    verifyVerificationCode(code);
                }
            });
        }catch(Exception e){
            Toast.makeText(getBaseContext(),"Error!",
                    Toast.LENGTH_LONG).show();
        }

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            //Toast.makeText(getotp.this, e.getMessage(), Toast.LENGTH_LONG).show();

            ///delete if not working

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d(TAG, "Invalid credential: "
                        + e.getLocalizedMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // SMS quota exceeded
                Log.d(TAG, "SMS Quota exceeded.");
            }

            ///


        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getotp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(getotp.this, bluetooth.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //FirebaseUser user = task.getResult().getUser();


                        } else {
                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                Toast.makeText(getBaseContext(),"Invalid Code Entered!",
                                        Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getotp.this, MainActivity.class));
                            }
                        }
                    }
                });
    }

}
