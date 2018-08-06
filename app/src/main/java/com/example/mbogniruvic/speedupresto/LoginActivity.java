package com.example.mbogniruvic.speedupresto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.ImagesManager;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Restaurant;
import com.example.mbogniruvic.speedupresto.models.RestaurantResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String TAG_RESTO = TAG+"_RESTO";
    private EditText phoneText;
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
        phoneText=(EditText)findViewById(R.id.login_phone);
        passwordText=(EditText)findViewById(R.id.login_password);
        loginBtn=(Button) findViewById(R.id.login_sign_in);

        //event handler
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phoneText.getText().toString().isEmpty()
                        || passwordText.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(), getString(R.string.login_hint), Toast.LENGTH_SHORT).show();
                }else {

                    pd.show();
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<RestaurantResponse> call=apiService.getRestaurantProfile(
                        phoneText.getText().toString().trim(),
                        passwordText.getText().toString().trim()
                    );

                    call.enqueue(new Callback<RestaurantResponse>() {

                        @Override
                        public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                            RestaurantResponse body=response.body();

                            if(!body.isError()){

                                if(body.getRestaurant()!=null){

                                    //stockage en local
                                    pd.setTitle("Récupération des infos.. ");

                                    checkNewRestaurant(
                                            body.getRestaurant().getPhone(),
                                            body.getRestaurant().getPassword());

                                    RestaurantPreferencesDB sharedDB=new RestaurantPreferencesDB(getApplicationContext());
                                    body.getRestaurant().setPassword(passwordText.getText().toString().trim());
                                    sharedDB.put(body.getRestaurant());

                                    new SaveProfileImageTask(body.getRestaurant()).execute();

                                    pd.dismiss();
                                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<RestaurantResponse> call, Throwable t) {


                            RestaurantPreferencesDB shareDB=new RestaurantPreferencesDB(getApplicationContext());
                            String phone=shareDB.getString(RestaurantPreferencesDB.PHONE_KEY, "");
                            String pwd=shareDB.getString(RestaurantPreferencesDB.PASSWORD_KEY, "");

                            if (!(phone==null || pwd==null || phone.isEmpty() || pwd.isEmpty())) {

                                if(phoneText.getText().toString().trim().equals(phone)
                                        && passwordText.getText().toString().trim().equals(pwd)){

                                    pd.dismiss();
                                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                pd.dismiss();
                                DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                                db.dropDatabase();
                                Toast.makeText(getApplicationContext(), getString(R.string.conn_error_not), Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

                }
            }
        });

    }

    private void checkNewRestaurant(String phone, String pwd) {

        RestaurantPreferencesDB sharedDB=new RestaurantPreferencesDB(getApplicationContext());
        String ph=sharedDB.getString(RestaurantPreferencesDB.PHONE_KEY, "");
        if(ph!=null && !ph.isEmpty() && !ph.equals(phone)){
            DatabaseHelper db=new DatabaseHelper(getApplicationContext());
            db.dropDatabase();
        }

    }


    class  SaveProfileImageTask extends AsyncTask<Void, Void, Void> {

        private Restaurant restaurant;

        public SaveProfileImageTask(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(ConnectionStatus.getInstance(getApplicationContext()).isOnline()){
                ImagesManager.saveIntoInternalStorage(getApplicationContext(), restaurant.getImage(), restaurant.getId());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
