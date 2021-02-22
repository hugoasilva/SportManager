/*
 * Copyright 2021 Hugo Silva @ IPBeja
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ipbeja.sportsmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import pt.ipbeja.sportsmanager.R;

public class RegisterActivity extends AppCompatActivity {
    private MaterialButton registerButton, loginButton;
    private EditText email, password;
    private FirebaseAuth firebaseAuth;

    /**
     * Creates activity
     *
     * @param savedInstanceState bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.ed_email);
        password = findViewById(R.id.ed_password);
        registerButton = findViewById(R.id.btn_sign_up);
        loginButton = findViewById(R.id.btn_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                        "Please fill in the required fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(),
                        "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(),
                        "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            }

            // Create user at firebase authentication service
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "E-mail or password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        loginButton.setOnClickListener(v -> startActivity(
                new Intent(getApplicationContext(), LoginActivity.class)
        ));

        // If user is signed in, start home activity
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }
}