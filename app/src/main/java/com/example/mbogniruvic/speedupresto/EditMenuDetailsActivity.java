package com.example.mbogniruvic.speedupresto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CategoriesAdapters;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.CategoryMenuResponse;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.models.UpdateMenuitemResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMenuDetailsActivity extends AppCompatActivity {

    private CategoryMenu currentCat;
    private MenuItem menu;
    private boolean imageHasChanged=false;
    private boolean isVoirPlus;
    private ImageView menu_image;
    private List<Category> categoryList;
    private MaterialSpinner categorieField;
    private EditText nomMenuField;
    private EditText prixField;
    private SwitchCompat dispoField=null;
    private ImageButton loadImage;
    private EditText descField;
    private Button cancelBtn;
    private Button editBtn;


    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_details);

        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }
        }

        menu=getIntent().getParcelableExtra(MenuDetailsActivity.MENU_ITEM_TAG);
        currentCat=getIntent().getParcelableExtra(MenuDetailsActivity.CAT_CURRENT_TAG);
        isVoirPlus=getIntent().getBooleanExtra(MenuDetailsActivity.MENU_ITEM_VOIR_PLUS_TAG, false);

        //get Address of fields
        menu_image=(ImageView) findViewById(R.id.edit_menu_image);
        loadImage=(ImageButton) findViewById(R.id.edit_menu_load_image);
        categorieField= (MaterialSpinner) findViewById(R.id.details_menu_edit_categorie);
        nomMenuField=(EditText) findViewById(R.id.edit_nom_menu);
        prixField=(EditText) findViewById(R.id.edit_prix_menu);
        dispoField=(SwitchCompat) findViewById(R.id.edit_dispo_menu);
        descField=(EditText) findViewById(R.id.edit_desc_menu);
        cancelBtn=(Button)findViewById(R.id.cancel_edit_menu_btn);
        editBtn=(Button)findViewById(R.id.edit_menu_btn);


        //initialize field
        new DownLoadImageTask(menu_image).execute(menu.getImage());
        nomMenuField.setText(menu.getNom());
        prixField.setText(menu.getPrice()+"");
        descField.setText(menu.getDesc());

        getAllCategories();

        dispoField.setChecked(menu.isDispo());
        if(menu.isDispo()){
            dispoField.setText(getString(R.string.menu_dispo));
            dispoField.setTextColor(getResources().getColor(R.color.green));
        }else{
            dispoField.setText(getString(R.string.menu_non_dispo));
            dispoField.setTextColor(getResources().getColor(R.color.red));
        }

        //Events
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

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*new MaterialFilePicker()
                        .withActivity(EditMenuDetailsActivity.this)
                        .withRequestCode(1000)
                        .withFilter(Pattern.compile(".*\\.png$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();*/
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

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                MenuItem editMenu=getEditMenuInformation();

                if (editMenu!=null) {

                    Call<UpdateMenuitemResponse> call=apiService.updateMenuItem(
                            editMenu.getId(),
                            editMenu.getNom(),
                            editMenu.getImage(),
                            editMenu.getPrice(),
                            editMenu.getDesc(),
                            editMenu.getIdCat(),
                            "5b060369244f78388a78f157",
                            editMenu.isDispo()
                    );

                    call.enqueue(new Callback<UpdateMenuitemResponse>() {
                        @Override
                        public void onResponse(Call<UpdateMenuitemResponse> call, Response<UpdateMenuitemResponse> response) {

                            if(response.body().getError().equals("false")){
                                menu=response.body().getMenu();
                                Toast.makeText(context, "Modification effectuée avec succès", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(EditMenuDetailsActivity.this, MenuActivity.class);
                                startActivity(intent);

                            }else {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<UpdateMenuitemResponse> call, Throwable t) {
                            Toast.makeText(context , t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private MenuItem getEditMenuInformation() {
        MenuItem menuItem=new MenuItem();

        if (nomMenuField.getText().toString().trim().isEmpty()
                || prixField.getText().toString().trim().isEmpty()
                || descField.getText().toString().trim().isEmpty()) {
            menuItem=null;
        } else {
            menuItem.setId(menu.getId());
            menuItem.setIdCat(categoryList.get(categorieField.getSelectedIndex()).getId());
            if (imageHasChanged) {
                menuItem.setImage("https://zupimages.net/up/18/28/7475.jpg");
            }else{
                menuItem.setImage(menu.getImage());
            }
            menuItem.setNom(nomMenuField.getText().toString().trim());
            menuItem.setPrice(Double.valueOf(prixField.getText().toString()).doubleValue());
            menuItem.setDispo(dispoField.isChecked());
            menuItem.setDesc(descField.getText().toString());
        }

        return  menuItem;
    }

    private void getAllCategories() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse> call=apiService.getAllCategories();

        call.enqueue(new Callback<CategoryResponse>() {

            @Override
            public void onResponse(Call<CategoryResponse>call, Response<CategoryResponse> response) {
                categoryList = response.body().getResults();

                if(categoryList!=null && categoryList.size()!=0){

                    List<String> listCategorie=new ArrayList<>();
                    int init=0;
                    for (int i=0 ; i<categoryList.size(); i++){
                        String titre=categoryList.get(i).getTitre();
                        listCategorie.add(titre);
                        if(titre.trim().equals(currentCat.getCategorie().trim())){
                            init=i;
                        }
                    }

                    categorieField.setItems(listCategorie);
                    categorieField.setSelectedIndex(init);

                }else if(categoryList==null){
                    Toast.makeText(context, "Liste null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Liste vide", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse>call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                menu_image.setImageBitmap(bitmap);
                imageHasChanged=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, getRealPathFromURI(context,uri), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Toast.makeText(context, "getRealPathFromURI Exception : " + e.toString(), Toast.LENGTH_SHORT).show();
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
