package dev.mobile.zenithhouseapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import dev.mobile.zenithhouseapp.databinding.ActivityLoginBinding;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class activity_Login extends AppCompatActivity
{

    private ActivityLoginBinding Bind;
    private ApiHandler apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bind = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = Bind.getRoot();
        setContentView(view);

        Bind.LShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bind.LShow.setVisibility(View.INVISIBLE);
                Bind.LHide.setVisibility(View.VISIBLE);
                Bind.PassLogEdit.setTransformationMethod(null);
            }
        });

        Bind.LHide.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bind.LShow.setVisibility(View.VISIBLE);
                Bind.LHide.setVisibility(View.INVISIBLE);
                Bind.PassLogEdit.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d98b-196-176-164-36.ngrok-free.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create ApiService instance
        apiService = retrofit.create(ApiHandler.class);

        Bind.btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleLogin();
            }
        });

        Bind.logRegBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent start = new Intent(activity_Login.this, activity_registre.class);
                startActivity(start);
            }
        });
    }

    private void handleLogin()
    {
        if (validateInput())
        {
            loginUserFromServer(
                    Bind.EmailEditLogin.getText().toString(),
                    Bind.PassLogEdit.getText().toString()
            );
        }
    }

    private boolean validateInput()
    {
        if (Bind.EmailEditLogin.getText().toString().isEmpty())
        {
            Bind.EmailEditLogin.setError("REQUIRED !");
            return false;
        }

        if (Bind.PassLogEdit.getText().toString().isEmpty())
        {
            Bind.PassLogEdit.setError("REQUIRED !");
            return false;
        }

        return true;
    }

    private void loginUserFromServer(String email, String password)
    {
        Call<List<User>> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<List<User>>()
        {
            @Override
            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0)
                {
                    List<User> userList = response.body();

                    boolean credentialsMatch = false;

                    for (User user : userList)
                    {
                        if (user.getEmail().equals(email) && user.getPassword().equals(password))
                        {
                            credentialsMatch = true;
                            saveLoginStatus(true);  // Save login status in shared preferences
                            break;
                        }
                    }

                    if (credentialsMatch)
                    {
                        Toast.makeText(activity_Login.this, "Welcome User", Toast.LENGTH_LONG).show();
                        Intent start = new Intent(activity_Login.this, MainActivity.class);
                        startActivity(start);
                        finish();  // Finish the login activity
                    }
                    else
                    {
                        Toast.makeText(activity_Login.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(activity_Login.this, "User Not Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                Toast.makeText(activity_Login.this, "Failed to login. " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Retrofit", "Failed to execute API request", t);
            }
        });
    }

    // Save login status in shared preferences
    private void saveLoginStatus(boolean isLoggedIn)
    {
        SharedPreferences preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}
