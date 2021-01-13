package pt.ipbeja.sportsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialButton login;
    AppCompatTextView register;
    EditText pwd;
    TextView email;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.ed_email);
        pwd = findViewById(R.id.ed_password);
        login = findViewById(R.id.btn_signin);
        register = findViewById(R.id.txt_signup);

        db = FirebaseFirestore.getInstance();
        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin:
                if (email.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if (pwd.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                }
                db.collection("client")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String a = doc.getString("Email");
                                        String b = doc.getString("Password");
                                        String a1 = email.getText().toString().trim();
                                        String b1 = pwd.getText().toString().trim();
                                        if (a.equalsIgnoreCase(a1) & b.equalsIgnoreCase(b1)) {
                                            Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(home);
                                            Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                            break;
                                        } else
                                            Toast.makeText(LoginActivity.this, "Cannot login,incorrect Email and Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                break;
            case R.id.txt_signup:
                Intent register_view = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register_view);
                break;
        }
    }
}