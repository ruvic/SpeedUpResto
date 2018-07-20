package com.example.mbogniruvic.speedupresto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Restaurant;
import com.example.mbogniruvic.speedupresto.models.UpdateRestaurantResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = EditProfileActivity.class.getSimpleName();
    public static final String FROM_EDIT_PROFILE_TAG=TAG+".FROM_EDIT_PROFILE";

    private Toolbar toolbar;
    private Context context;
    private RestaurantPreferencesDB sharedDB;
    private boolean imageHasChanged=false;

    private ImageView logoRestauView;
    private ImageButton loadImageBtn;
    private EditText nomRestauView;
    private EditText descRestauView;
    private EditText phoneView;
    private EditText emailView;
    private EditText cityView;
    private EditText quartierView;
    private EditText minOrderView;
    private Button editBtn;
    private Button cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context=this;

        //get Adress
        logoRestauView=(ImageView)findViewById(R.id.edit_prof_image);
        loadImageBtn=(ImageButton)findViewById(R.id.edit_prof_load_image);
        nomRestauView=(EditText)findViewById(R.id.edit_prof_edit_nom_restau);
        descRestauView=(EditText)findViewById(R.id.edit_prof_desc);
        phoneView=(EditText)findViewById(R.id.edit_prof_phone);
        emailView=(EditText)findViewById(R.id.edit_prof_email);
        minOrderView=(EditText)findViewById(R.id.edit_prof_min_order);
        cityView=(EditText) findViewById(R.id.edit_prof_ville);
        quartierView=(EditText) findViewById(R.id.edit_prof_quartier);
        cancelBtn=(Button)findViewById(R.id.cancel_edit_prof_btn);
        editBtn=(Button)findViewById(R.id.edit_prof_edit_btn);

        //init field
        sharedDB=MainActivity.shareDB;
        new DownLoadImageTask(logoRestauView).execute(sharedDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        nomRestauView.setText(sharedDB.getString(RestaurantPreferencesDB.NOM_KEY,"<nom du restaurant>"));
        descRestauView.setText(sharedDB.getString(RestaurantPreferencesDB.BIO_KEY, "<desc>"));
        minOrderView.setText(sharedDB.getInt(RestaurantPreferencesDB.MIN_ORDER_KEY, 0)+"");
        phoneView.setText(sharedDB.getString(RestaurantPreferencesDB.PHONE_KEY,"<phone>"));
        emailView.setText(sharedDB.getString(RestaurantPreferencesDB.EMAIL_KEY,"<email>"));
        cityView.setText(sharedDB.getString(RestaurantPreferencesDB.CITY_KEY,"<city>"));
        quartierView.setText(sharedDB.getString(RestaurantPreferencesDB.QUARTIER_KEY,"<quartier>"));

        //events

        loadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1000);

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setTitle("Modification...");
                pd.setMessage("Veuillez patientez.");
                pd.setCancelable(false);
                pd.show();

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Restaurant restau=getEditRestaurant();
                Call<UpdateRestaurantResponse> call=apiService.updateRestaurant(
                    restau.getId(),
                    restau.getNom(),
                    restau.getEmail(),
                    restau.getCity(),
                    restau.getPhone(),
                    restau.getFee_delivery(),
                    restau.getMin_order(),
                    restau.getTime_delivery(),
                    restau.getImage(),
                    restau.getQuartier(),
                    restau.getBio(),
                    restau.getLatitude(),
                    restau.getLongitude(),
                    restau.getNote()
                );

                call.enqueue(new Callback<UpdateRestaurantResponse>() {

                    @Override
                    public void onResponse(Call<UpdateRestaurantResponse> call, Response<UpdateRestaurantResponse> response) {
                        if(!response.body().isError()){
                            Restaurant r=response.body().getRestaurant();
                            sharedDB.put(r);
                            pd.dismiss();
                            Intent intent =new Intent(EditProfileActivity.this, ProfileActivity.class);
                            intent.putExtra(FROM_EDIT_PROFILE_TAG, 1);
                            startActivity(intent);
                            Toast.makeText(context, "Modification effectuée avec succès", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateRestaurantResponse> call, Throwable t) {
                        Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private Restaurant getEditRestaurant() {
        Restaurant restaurant=null;

        if (nomRestauView.getText().toString().isEmpty()
                || descRestauView.getText().toString().isEmpty()
                || phoneView.getText().toString().isEmpty()
                || emailView.getText().toString().isEmpty()
                || cityView.getText().toString().isEmpty()
                || minOrderView.getText().toString().isEmpty()
                || quartierView.getText().toString().isEmpty()) {

            restaurant=null;
            Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }else {

            restaurant=new Restaurant(MainActivity.shareDB);
            restaurant.setNom(nomRestauView.getText().toString().trim());
            restaurant.setEmail(emailView.getText().toString().trim());
            restaurant.setCity(cityView.getText().toString().trim());
            restaurant.setQuartier(quartierView.getText().toString().trim());
            restaurant.setPhone(phoneView.getText().toString().trim());
            restaurant.setBio(descRestauView.getText().toString().trim());
            restaurant.setMin_order(Integer.valueOf(minOrderView.getText().toString().trim()).intValue());

            if(imageHasChanged){
                restaurant.setImage("https://image.ibb.co/h7UXfo/l_arbalete.jpg");
            }
        }

        return  restaurant;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                logoRestauView.setImageBitmap(bitmap);
                imageHasChanged=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, EditMenuDetailsActivity.getRealPathFromURI(context,uri), Toast.LENGTH_LONG).show();

        }

    }
}
