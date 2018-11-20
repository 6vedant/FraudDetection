package in.ac.nitsikkim.frauddetection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPasswordCheck extends AppCompatActivity {

    String correct_password;

    public String getCorrect_password() {
        return correct_password;
    }

    public void setCorrect_password(String correct_password) {
        this.correct_password = correct_password;
    }

    private EditText password_admin_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_password_check);

        password_admin_et = (EditText) findViewById(R.id.et_password_admin);
        String some_temp_string = getCurrentAdminPassword();

        

        findViewById(R.id.btn_submit_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if the password is correct

                String admin_password = getCurrentAdminPassword();
                if (admin_password != null) {
                    if (password_admin_et.getText().toString().equals(admin_password)) {
                        // correct password
                        startActivity(new Intent(getApplicationContext(), AdminPanel.class));
                        Toast.makeText(getApplicationContext(), "Correct admin password..", Toast.LENGTH_SHORT).show();
                    } else {
                        // password is incorrect
                        Toast.makeText(getApplicationContext(), "Incorrect password...", Toast.LENGTH_SHORT).show();


                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Password is null...", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    // some comment out there

    private String getCurrentAdminPassword() {

        FirebaseDatabase.getInstance().getReference().child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pass = (String) dataSnapshot.child("admin_password").getValue();
                setCorrect_password(pass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setCorrect_password(null);
            }
        });

        return getCorrect_password();
    }

}
