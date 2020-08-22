package example.com.authenticate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    private FirebaseAuth mAuth;
    Button btn_sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        btn_sign_out = (Button)findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                showSignInOption();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );



    }

    private void showSignInOption(){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
                btn_sign_out.setEnabled(true);
            }
            else{
                Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(MainActivity.this, "Call on start", Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, "Call", Toast.LENGTH_SHORT).show();
        // Check if user is signed in (non-null) and update UI accordingly.


        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {

                Toast.makeText(MainActivity.this, "Call hoga galat", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Le beta bopdike", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, currentUser.getUid(), Toast.LENGTH_SHORT).show();
                btn_sign_out.setEnabled(true);
            } else {
                Toast.makeText(MainActivity.this, "Call", Toast.LENGTH_SHORT).show();
                showSignInOption();
            }

        }catch (Exception e){e.printStackTrace();}

    }
}