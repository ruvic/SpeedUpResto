package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Commande;
import com.example.mbogniruvic.speedupresto.models.CommandeResponse;
import com.example.mbogniruvic.speedupresto.models.UpdateCommandeResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;

import java.util.ArrayList;
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
    public static List<Commande> cmdList;
    public static List<Commande> nonLivreCmdList;
    public static List<Commande> livreCmdList;
    public static List<Commande> refuseCmdList;
    public static List<Commande> valideCmdList;

    private RecyclerView recyclerView;
    private CommandesAdapter mAdapter;
    private RestaurantPreferencesDB sharedDB;
    private ProgressBar progressBar;
    private MainActivity mainActivity;


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

        String today="2018-07-19";

        Call<CommandeResponse> call=apiService.getAllCommandesForDate(
                sharedDB.getString(RestaurantPreferencesDB.ID_KEY, ""),
                today
        );

        call.enqueue(new Callback<CommandeResponse>() {

            @Override
            public void onResponse(Call<CommandeResponse> call, Response<CommandeResponse> response) {
                CommandeResponse body=response.body();
                if(!body.isError()){

                    if (body.getCommandes().size()!=0) {

                        cmdList=body.getCommandes();
                        nonLivreCmdList=new ArrayList<>();
                        livreCmdList=new ArrayList<>();
                        refuseCmdList=new ArrayList<>();
                        valideCmdList=new ArrayList<>();

                        for (Commande cmd : body.getCommandes()) {

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
                        }

                        showCommandes();
                        //MainActivity.livreFragment.prepareDatas();
                        //MainActivity.refuseFragment.prepareDatas();

                        /*String result="Non live : "+nonLivreCmdList.size()+"\n"
                                     +"Livre : "+livreCmdList.size()+"\n"
                                     +"Refuse : "+refuseCmdList.size()+"\n"
                                     +"Validé : "+valideCmdList.size()+"\n"
                                     +"Tous : "+cmdList.size();*/

                    } else {
                        Toast.makeText(context, "Aucune commande pour la journée", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommandeResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCommandes(){

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
                new DownLoadImageTask(imgView).execute(cmd.getMenu().getImage());

                final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                dialog.setContentView(detailsview);
                dialog.show();

                ImageView btn_close=(ImageView) detailsview.findViewById(R.id.close_menu);

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                });


                mark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateCommande(cmd, Commande.STATUS_LIVRE, dialog);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateCommande(cmd, Commande.STATUS_REFUSE, dialog);
                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

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
                if(!body.isError()){
                    dialog.hide();
                    getMainActivity().setupViewPager(getMainActivity().getViewPager());
                    Toast.makeText(getContext(), cmd.getMenu().getId() + " a été MAJ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateCommandeResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.conn_error_not), Toast.LENGTH_SHORT).show();
            }
        });

        /*int i=0;
        while (call.isExecuted()){
            System.out.println("Je suis là : "+i);
            i++;
        }*/

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

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
