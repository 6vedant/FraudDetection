package in.ac.nitsikkim.frauddetection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText et_admin_name, et_admin_password;
    boolean isAdminRegistered;

    public boolean isAdminRegistered() {
        return isAdminRegistered;
    }

    public void setAdminRegistered(boolean adminRegistered) {
        isAdminRegistered = adminRegistered;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        // fetch if the admin is already registered
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean admin_registered = (boolean) dataSnapshot.child("admin_registered").getValue();
                setAdminRegistered(admin_registered);
                if(admin_registered == true){
                    startActivity(new Intent(getApplicationContext(), AdminPasswordCheck.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.register_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // register the admin here with the name as email id and password
                if (!isAdminRegistered) {
                    final String admin_password = et_admin_password.getText().toString();
                    final String admin_name = et_admin_name.getText().toString();
                    if (!(admin_name.isEmpty() || admin_password.isEmpty())) {
                        // login the admin with the credentials
                        saveAdminDetails(admin_password, admin_name);
                        // change the parameter admin_registered to true
                        FirebaseDatabase.getInstance().getReference().child("admin_registered")
                                .setValue(true);

                        startActivity(new Intent(getApplicationContext(), AdminPanel.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "One or more fields are empty...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Admin is already registered", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), AdminPasswordCheck.class));
                }

            }
        });

        if(isAdminRegistered()){
            startActivity(new Intent(getApplicationContext(), AdminPasswordCheck.class));
        }


    }

    public void init() {
        et_admin_name = (EditText) findViewById(R.id.et_admin_name);
        et_admin_password = (EditText) findViewById(R.id.et_admin_password);
    }

    public void saveAdminDetails(String admin_password, String admin_name) {
        FirebaseDatabase.getInstance().getReference().child("admin").
                child("admin_name").setValue(admin_name);

        FirebaseDatabase.getInstance().getReference().child("admin")
                .child("admin_password").setValue(admin_password);


    }

}
