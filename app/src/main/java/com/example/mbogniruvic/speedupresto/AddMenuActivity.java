package com.example.mbogniruvic.speedupresto;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.models.CreateMenuItemResponse;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenuActivity extends AppCompatActivity {

    private  Toolbar toolbar;
    private Context context;
    private List<Category> categoryList;
    private boolean imageHasChange=false;
    private boolean hasChooseCategorie=false;
    private RestaurantPreferencesDB sharedDB;
    private DatabaseHelper db;

    private ImageView menuImageField;
    private ImageButton loadImageView;
    private MaterialSpinner categorieField;
    private EditText nomMenuField;
    private EditText prixField;
    private SwitchCompat dispoField;
    private EditText descField;
    private Button cancelBtn;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

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

        //get Adresses
        menuImageField=(ImageView)findViewById(R.id.add_menu_image);
        loadImageView=(ImageButton)findViewById(R.id.add_load_image);
        categorieField=(MaterialSpinner)findViewById(R.id.add_menu_cat);
        nomMenuField=(EditText)findViewById(R.id.add_menu_name);
        prixField=(EditText)findViewById(R.id.add_menu_price);
        dispoField=(SwitchCompat) findViewById(R.id.add_menu_dispo);
        descField=(EditText)findViewById(R.id.add_menu_desc);
        addBtn=(Button)findViewById(R.id.add_menu_add_btn);
        cancelBtn=(Button)findViewById(R.id.add_menu_cancel_btn);

        //init field
        sharedDB=MainActivity.shareDB;
        db=new DatabaseHelper(context);

        getAllCategories();

        dispoField.setChecked(true);

        //events
        loadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1000);
            }
        });

        categorieField.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                hasChooseCategorie=true;
            }
        });

        dispoField.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(dispoField.isChecked()){
                    dispoField.setText(getString(R.string.menu_dispo));
                    dispoField.setTextColor(getResources().getColor(R.color.green));
                }else{
                    dispoField.setText(getString(R.string.menu_non_dispo));
                    dispoField.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MenuItem menuItem=getAllDataEntries();

                if(menuItem!=null){

                    if (ConnectionStatus.getInstance(context).isOnline()) {

                        final ProgressDialog pd = new ProgressDialog(context);
                        pd.setTitle("Ajout d'un menu...");
                        pd.setMessage("Veuillez patientez.");
                        pd.setCancelable(false);
                        pd.show();

                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        Call<CreateMenuItemResponse> call=apiService.createMenuItem(
                            menuItem.getNom(),
                            menuItem.getImage(),
                            menuItem.getPrice(),
                            menuItem.getDesc(),
                            menuItem.getIdCat(),
                            sharedDB.getString(RestaurantPreferencesDB.ID_KEY,""),
                            menuItem.isDispo()
                        );

                        call.enqueue(new Callback<CreateMenuItemResponse>() {

                            @Override
                            public void onResponse(Call<CreateMenuItemResponse> call, Response<CreateMenuItemResponse> response) {
                                if(response.body().getError().equals("false")){
                                    Toast.makeText(context, "Menu créé avec succès", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                    Intent intent=new Intent(AddMenuActivity.this, MenuActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    pd.dismiss();
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CreateMenuItemResponse> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(context, getString(R.string.conn_error_not), Toast.LENGTH_LONG).show();
                            }

                        });
                    } else {
                        Toast.makeText(context, getString(R.string.conn_error_not), Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            Uri uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                menuImageField.setImageBitmap(bitmap);
                imageHasChange=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Toast.makeText(context, EditMenuDetailsActivity.getRealPathFromURI(context,uri), Toast.LENGTH_LONG).show();

        }
    }

    private void getAllCategories() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse> call=apiService.getAllCategories();

        call.enqueue(new Callback<CategoryResponse>() {

            @Override
            public void onResponse(Call<CategoryResponse>call, Response<CategoryResponse> response) {
                categoryList = response.body().getResults();

                if(categoryList!=null && categoryList.size()!=0){

                    initCategorieField();

                }else if(categoryList==null){
                    Toast.makeText(context, "Liste null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Liste vide", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse>call, Throwable t) {

                categoryList = db.getAllCategories();
                initCategorieField();

            }

        });

    }

    private void initCategorieField() {
        List<String> listCategorie=new ArrayList<>();

        for (int i=0 ; i<categoryList.size(); i++){
            String titre=categoryList.get(i).getTitre();
            listCategorie.add(titre);
        }

        categorieField.setItems(listCategorie);
    }

    private MenuItem getAllDataEntries(){
        MenuItem menuItem=new MenuItem();

        if (nomMenuField.getText().toString().trim().isEmpty()
                || prixField.getText().toString().trim().isEmpty()
                || descField.getText().toString().trim().isEmpty()
                ) {
            menuItem=null;
            Toast.makeText(context, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
        } else {
            if (imageHasChange) {
                if (hasChooseCategorie) {
                    menuItem.setIdCat(categoryList.get(categorieField.getSelectedIndex()).getId());
                    menuItem.setImage("https://zupimages.net/up/18/29/379z.jpg");
                    menuItem.setNom(nomMenuField.getText().toString().trim());
                    menuItem.setPrice(Double.valueOf(prixField.getText().toString()).doubleValue());
                    menuItem.setDispo(dispoField.isChecked());
                    menuItem.setDesc(descField.getText().toString());
                } else {
                    menuItem=null;
                    Toast.makeText(context, "Veuillez choisir une catégorie pour le menu", Toast.LENGTH_SHORT).show();
                }
            } else {
                menuItem=null;
                Toast.makeText(context, "Veuillez charger une image pour le menu", Toast.LENGTH_SHORT).show();
            }
        }

        return  menuItem;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }
}
