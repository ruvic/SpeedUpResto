package com.example.mbogniruvic.speedupresto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.models.Restaurant;
import com.example.mbogniruvic.speedupresto.models.RestaurantWithEmailResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String TAG_RESTO = TAG+"_RESTO";
    private EditText mailText;
    private EditText passwordText;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setTitle("Verification des infos...");
        pd.setMessage("Veuillez patientez.");
        pd.setCancelable(false);



        //get Adresses
        mailText=(EditText)findViewById(R.id.login_mail);
        passwordText=(EditText)findViewById(R.id.login_password);
        loginBtn=(Button) findViewById(R.id.login_sign_in);

        //event handler
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mailText.getText().toString().isEmpty()
                        || passwordText.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(), getString(R.string.login_hint), Toast.LENGTH_SHORT).show();
                }else {

                    pd.show();
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<RestaurantWithEmailResponse> call=apiService.getRestoWithMail(mailText.getText().toString());

                    call.enqueue(new Callback<RestaurantWithEmailResponse>() {

                        @Override
                        public void onResponse(Call<RestaurantWithEmailResponse> call, Response<RestaurantWithEmailResponse> response) {
                            RestaurantWithEmailResponse body=response.body();

                            if(!body.isError()){
                                if(body.getRestaurants()!=null){

                                    Restaurant resto=body.getRestaurants().get(0);

                                    pd.dismiss();
                                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra(TAG_RESTO, resto);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Informations incorrectes", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<RestaurantWithEmailResponse> call, Throwable t) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.conn_error_not), Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            }
        });

    }
}
