package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Commande;
import com.example.mbogniruvic.speedupresto.models.CommandeResponse;
import com.example.mbogniruvic.speedupresto.models.UpdateCommandeResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MBOGNI RUVIC on 27/06/2018.
 */

public class NonLivreFragment extends Fragment {
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    public static Context context;
    public static DatabaseHelper db;
    public static List<Commande> cmdList;
    public static List<Commande> nonLivreCmdList;
    public static List<Commande> livreCmdList;
    public static List<Commande> refuseCmdList;
    public static List<Commande> valideCmdList;

    private RecyclerView recyclerView;
    private CommandesAdapter mAdapter;
    private RestaurantPreferencesDB sharedDB;
    private ProgressBar progressBar;
    public static MainActivity mainActivity;

    private RelativeLayout mainLayout;
    private RelativeLayout connErrorLayout;
    private RelativeLayout emptyLayout;
    private Button refresh_conn;
    private Button refresh_empty;



    public NonLivreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_non_livre, container, false);
        context=rootView.getContext();

        //getAdress
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerV_non_livre);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_non_livre);
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.non_livre_main);
        connErrorLayout = (RelativeLayout) rootView.findViewById(R.id.non_livre_conn_error);
        emptyLayout = (RelativeLayout) rootView.findViewById(R.id.non_livre_empty);
        refresh_conn = (Button) rootView.findViewById(R.id.non_livre_conn_refresh);
        refresh_empty = (Button) rootView.findViewById(R.id.non_livre_empty_refresh);

        refresh_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setupViewPager(mainActivity.getViewPager());
            }
        });

        refresh_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setupViewPager(mainActivity.getViewPager());
            }
        });

        sharedDB= MainActivity.shareDB;
        prepareDatas();

        return rootView;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void prepareDatas() {

        /*Call<CommandeResponse> call=*/

        Call<CommandeResponse> call=null;


        //Si on doit afficher les commandes dee la journée en cours ...
        if(!mainActivity.hasChooseType){

            String today=getCurrentDate();
            call=apiService.getAllCommandesForDate(
                    sharedDB.getString(RestaurantPreferencesDB.ID_KEY, ""),
                    today
            );

        }else if (mainActivity.isDayFilter()){

            call=apiService.getAllCommandesForDate(
                    sharedDB.getString(RestaurantPreferencesDB.ID_KEY,""),
                    mainActivity.getStartDay()
            );

        }else{

            call=apiService.getAllCommandesForPeriodes(
                    sharedDB.getString(RestaurantPreferencesDB.ID_KEY,""),
                    mainActivity.getStartDay()+","+mainActivity.getEndDay()
            );
        }

        if (call!=null) {

            call.enqueue(new Callback<CommandeResponse>() {

                @Override
                public void onResponse(Call<CommandeResponse> call, Response<CommandeResponse> response) {
                    CommandeResponse body=response.body();

                    if(!body.isError()){

                        if (body.getCommandes()!=null && body.getCommandes().size()!=0) {

                            showCommandes(body.getCommandes());

                        } else {

                            livreCmdList=new ArrayList<>();
                            refuseCmdList=new ArrayList<>();
                            setVisible(emptyLayout);
                        }
                    }else {
                        Toast.makeText(context, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommandeResponse> call, Throwable t) {

                    db=new DatabaseHelper(context);
                    cmdList=new ArrayList<>();

                    if(!mainActivity.hasChooseType){
                        cmdList=db.getCommandesForDate(getCurrentDate());
                    }else if(mainActivity.isDayFilter()){
                        cmdList=db.getCommandesForDate(mainActivity.getStartDay());
                    }else{
                        cmdList=db.getCommandesForPeriode(mainActivity.getStartDay(), mainActivity.getEndDay());
                    }

                    db.closeDB();

                    if(cmdList.size()==0 || (cmdList.size()!=0 && cmdList.get(0).getMenu()==null)){
                        setVisible(connErrorLayout);
                    } else {
                        showCommandes(cmdList);
                    }


                }
            });
        } else {
            Toast.makeText(context, "Call is null", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return year+":"+((month<10)?"0"+month:month)+":"+((day<10)?"0"+day:day);
    }



    private void showCommandes(List<Commande> list){

        db=new DatabaseHelper(context);
        cmdList=list;
        nonLivreCmdList=new ArrayList<>();
        livreCmdList=new ArrayList<>();
        refuseCmdList=new ArrayList<>();
        valideCmdList=new ArrayList<>();

        for (Commande cmd : list) {

            switch (cmd.getEtat()){
                case Commande.STATUS_EN_ATTENTE :
                    nonLivreCmdList.add(cmd);
                    break;
                case Commande.STATUS_LIVRE:
                    livreCmdList.add(cmd);
                    break;
                case Commande.STATUS_REFUSE:
                    refuseCmdList.add(cmd);
                    break;
                default:
                    valideCmdList.add(cmd);
                    break;
            }

            //Stockage en local
            db.updateCommande(cmd);

        }

        mAdapter = new CommandesAdapter(nonLivreCmdList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //Gestion du clic sur un élément de la liste
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Commande cmd = nonLivreCmdList.get(position);
                View detailsview = getLayoutInflater().inflate(R.layout.fragment_commande_details, null);

                //get Adresses
                TextView title=(TextView)detailsview.findViewById(R.id.title_menu);
                ImageView imgView=(ImageView) detailsview.findViewById(R.id.image_menu);
                TextView montant=(TextView)detailsview.findViewById(R.id.montant_menu);
                TextView qte=(TextView)detailsview.findViewById(R.id.qte_menu);
                TextView heure=(TextView)detailsview.findViewById(R.id.heure_menu);
                Button mark=(Button) detailsview.findViewById(R.id.mark_cmd_btn);
                Button cancel=(Button) detailsview.findViewById(R.id.cancel_cmd_btn);

                //init fields
                title.setText(cmd.getMenu().getNom());
                montant.setText(cmd.getMontant()+" FCFA");
                qte.setText(cmd.getQte()+"");
                heure.setText(cmd.getJour()+"\n"+cmd.getHeure());
                if (ConnectionStatus.getInstance(context).isOnline()) {
                    new DownLoadImageTask(imgView).execute(cmd.getMenu().getImage());
                } else {
                    new DownLoadImageTask(imgView, cmd.getMenu().getId()).execute(cmd.getMenu().getImage());
                }

                final BottomSheetDialog[] dialog = {new BottomSheetDialog(getContext())};
                dialog[0].setContentView(detailsview);
                dialog[0].show();

                ImageView btn_close=(ImageView) detailsview.findViewById(R.id.close_menu);

                dialog[0].setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialog[0] =null;
                    }
                });

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //dialog.hide();
                        if (dialog[0].isShowing()) {
                            dialog[0].dismiss();
                        }

                    }
                });




                mark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //updateCommande(cmd, Commande.STATUS_LIVRE, dialog);
                        showAlert(
                             getString(R.string.conf_livre_desc),
                             cmd,
                             Commande.STATUS_LIVRE,
                                dialog[0]
                        );
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //updateCommande(cmd, Commande.STATUS_REFUSE, dialog);
                        showAlert(
                                getString(R.string.conf_refuse_desc),
                                cmd,
                                Commande.STATUS_REFUSE,
                                dialog[0]
                        );
                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        mainLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        db.closeDB();

    }

    private  void updateCommande(final Commande cmd, String state , final BottomSheetDialog dialog){

        Call<UpdateCommandeResponse> call=apiService.updateCommande(
                cmd.getId(),
                state
        );

        call.enqueue(new Callback<UpdateCommandeResponse>() {
            @Override
            public void onResponse(Call<UpdateCommandeResponse> call, Response<UpdateCommandeResponse> response) {
                UpdateCommandeResponse body=response.body();
                db=new DatabaseHelper(context);
                if(!body.isError()){
                    db.updateCommande(body.getCommande());
                    db.closeDB();
                    dialog.dismiss();
                    dialog.cancel();
                    getMainActivity().setupViewPager(getMainActivity().getViewPager());
                }else{
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateCommandeResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.conn_error_not), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }
    }

    private void showAlert(String message, final Commande cmd, final String state, final BottomSheetDialog dial){

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation...")
               .setMessage(message)
               .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        updateCommande(cmd, state, dial);

                    }
                })
               .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
               });

        AlertDialog alert=builder.create();
        alert.show();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(cmdList!=null){
                if (cmdList.size()!=0) {
                    showCommandes(cmdList);
                } else {
                    setVisible(connErrorLayout);
                }
            }
        }
    }

    private void showMessage(String msg){
        if (context!=null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void setVisible(RelativeLayout layout){

        if (layout.equals(mainLayout)){
            mainLayout.setVisibility(View.VISIBLE);
            connErrorLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
        }else if(layout.equals(connErrorLayout)){
            mainLayout.setVisibility(View.GONE);
            connErrorLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }else if (layout.equals(emptyLayout)){
            mainLayout.setVisibility(View.GONE);
            connErrorLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
