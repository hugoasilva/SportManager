package pt.ipbeja.sportsmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button reg_registration;
    EditText reg_name;
    EditText reg_email;
    EditText reg_password;
    EditText reg_conf_pwd;
    TextView signin;
    FirebaseFirestore firebaseFirestore;
    DocumentReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        reg_registration=findViewById(R.id.btn_signup);
        reg_name=findViewById(R.id.ed_name);
        reg_email=findViewById(R.id.ed_email);
        reg_password=findViewById(R.id.ed_password);
        signin=findViewById(R.id.btn_signin);
        firebaseFirestore=FirebaseFirestore.getInstance();
        ref = firebaseFirestore.collection("client").document();
        reg_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reg_name.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please type a username", Toast.LENGTH_SHORT).show();

                }else if(reg_email.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please type an email id", Toast.LENGTH_SHORT).show();

                }else if(reg_password.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please type a password", Toast.LENGTH_SHORT).show();

//                }else if(!reg_conf_pwd.getText().toString().equals(reg_password.getText().toString())){
//                    Toast.makeText(RegisterActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();

                }else {

                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {
                                Toast.makeText(RegisterActivity.this, "Sorry,this user exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> reg_entry = new HashMap<>();
                                reg_entry.put("Name", reg_name.getText().toString());
                                reg_entry.put("Email", reg_email.getText().toString());
                                reg_entry.put("Password", reg_password.getText().toString());

                                //   String myId = ref.getId();
                                firebaseFirestore.collection("client")
                                        .add(reg_entry)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RegisterActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Error", e.getMessage());
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainpage = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(mainpage);
            }
        });
    }
}