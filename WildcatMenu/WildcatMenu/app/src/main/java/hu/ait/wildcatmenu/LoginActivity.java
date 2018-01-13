package hu.ait.wildcatmenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.wildcatmenu.staff.OrderListActivity;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        firebaseAuth = firebaseAuth.getInstance();
    }

    @OnClick(R.id.btnRegister)
    public void registerClick() {
        if (!isFormValid())
            return;

        showProgressDialog();

        final boolean[] failed = {false};

        firebaseAuth.createUserWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernameFromEmail(user.getEmail())).build());
                        }

                        else {
                            Toast.makeText(LoginActivity.this,
                                    "Not successful: " + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            failed[0] = true;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (!failed[0]) {
                            Toast.makeText(LoginActivity.this,
                                    "Error: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }
                    }
                });

    }

    @OnClick(R.id.btnLogin)
    public void loginClick() {
        if (!isFormValid())
            return;

        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(),
                etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                            if (username.equals("staff"))
                                startActivity(new Intent(LoginActivity.this, OrderListActivity.class));
                            else {
                                startActivity(new Intent(LoginActivity.this, StudentActivity.class));
                            }
                        }

                        else {
                            Toast.makeText(LoginActivity.this,
                                    "Failed: " + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("This field should not be empty!");
            return false;
        }
        else if (!etEmail.getText().toString().contains("@")) {
            etEmail.setError("Invalid email");
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("This field should not be empty!");
            return false;
        }

        return true;
    }

    private String usernameFromEmail(String email) {
        return email.split("@")[0];
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait a moment...");
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.hide();
    }

}
