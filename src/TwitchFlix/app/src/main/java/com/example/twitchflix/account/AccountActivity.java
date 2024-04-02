package com.example.twitchflix.account;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitchflix.R;
import com.example.twitchflix.network.Client;
import com.example.twitchflix.network.UserData;
import com.example.twitchflix.network.WebInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// activity that represents user account details
public class AccountActivity extends AppCompatActivity {
    private boolean loged_in = false;
    Toolbar toolbar;

    WebInterface service = Client.getClient().create(WebInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if user is logged in
        if(AccountInfo.getInstance().getData() != null) {
            loged_in = true;
        }

        // we create different layout depending on whether user is logged in or not
        if(loged_in) {
            setContentView(R.layout.loged_in);
            Button logout_button = findViewById(R.id.logOut);
            Button remove_button = findViewById(R.id.removeUser);
            TextView name = (TextView)findViewById(R.id.name_input);
            TextView email = (TextView)findViewById(R.id.email_input);

            toolbar = findViewById(R.id.mainToolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            UserData data = AccountInfo.getInstance().getData();
            name.setText(data.getName());
            email.setText(data.getEmail());

            // simple log out
            logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountInfo.getInstance().setData(null);
                    finish();
                }
            });

            // first show deletion confirmation alert, then delete
            remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to delete this account ?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<UserData> call = service.deleteUser(AccountInfo.getInstance().getData());

                                    call.enqueue(new Callback<UserData>() {
                                        @Override
                                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                                            if (response.body() != null) {
                                                if(response.body().getMessage() == null) {
                                                    Toast.makeText(getApplicationContext(), "User Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                    AccountInfo.getInstance().setData(null);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UserData> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            setContentView(R.layout.loged_out);
            Button login_button = findViewById(R.id.logIn);
            Button register_button = findViewById(R.id.register);

            toolbar = findViewById(R.id.mainToolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editEmail = (EditText)findViewById(R.id.editEmail);
                    String email = editEmail.getText().toString().trim();

                    EditText editPassword = (EditText)findViewById(R.id.editPassword);
                    String password = editPassword.getText().toString().trim();

                    if(isValidEmail(email)) {
                        Call<UserData> call = service.loginUser(new UserData(null, null, email, password, null));

                        call.enqueue(new Callback<UserData>() {
                            @Override
                            public void onResponse(Call<UserData> call, Response<UserData> response) {
                                if (response.body() != null) {
                                    // is login fails, there will be message saying it, otherwise there wont which means login was successful and we return to previous screen
                                    if (response.body().getMessage() == null) {
                                        Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                                        AccountInfo.getInstance().setData(response.body());
                                        finish();
                                    } else
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserData> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // we use alert box to get data to register
            register_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Add User");
                    builder.setMessage("Enter data for new user");
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.register_dialog, null, false);
                    builder.setView(dialogView);

                    final EditText editName = (EditText)dialogView.findViewById(R.id.edit_name);
                    final EditText editEmail = (EditText)dialogView.findViewById(R.id.edit_email);
                    final EditText editPassword = (EditText)dialogView.findViewById(R.id.edit_password);
                    final EditText editRep_pass = (EditText)dialogView.findViewById(R.id.edit_repeat_password);

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = editName.getText().toString().trim();
                            String email = editEmail.getText().toString().trim();
                            String password = editPassword.getText().toString().trim();
                            String rep_pass = editRep_pass.getText().toString().trim();

                            if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && isValidEmail(email) && password.equals(rep_pass)) {
                                Call<UserData> call = service.addUser(new UserData(null,name,email,password,null));

                                call.enqueue(new Callback<UserData>() {
                                    @Override
                                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                                        if (response.body() != null) {
                                            if (response.body().getMessage() == null) {
                                                Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserData> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    // method that validates inputted email
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
