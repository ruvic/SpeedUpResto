package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.models.Commande;

import java.util.ArrayList;
import java.util.List;

public class RefuseFragment extends Fragment {
    private Context context;
    private List<Commande> cmdList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CommandesAdapter mAdapter;


    public RefuseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_refuse, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerV_refuse);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_refuse);
        context=rootView.getContext();

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

    public void prepareDatas() {

        if (NonLivreFragment.refuseCmdList!=null) {
            if (NonLivreFragment.refuseCmdList.size()!=0) {

                cmdList=NonLivreFragment.refuseCmdList;
                mAdapter = new CommandesAdapter(cmdList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

                //Gestion du clic sur un élément de la liste
                recyclerView.addOnItemTouchListener(new NonLivreFragment.RecyclerTouchListener(getContext(), recyclerView, new NonLivreFragment.RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {

                        final Commande cmd = cmdList.get(position);
                        View detailsview = getLayoutInflater().inflate(R.layout.fragment_commande_livre_details, null);

                        TextView title=(TextView)detailsview.findViewById(R.id.livre_title_menu);
                        ImageView imgView=(ImageView)detailsview.findViewById(R.id.livre_image_menu);
                        TextView montant=(TextView)detailsview.findViewById(R.id.livre_montant_menu);
                        TextView qte=(TextView)detailsview.findViewById(R.id.livre_qte_menu);
                        TextView heure=(TextView)detailsview.findViewById(R.id.livre_heure_menu);
                        TextView dateLivraison=(TextView)detailsview.findViewById(R.id.livre_date_livraison);

                        title.setText(cmd.getMenu().getNom());
                        montant.setText(cmd.getMontant()+" FCFA");
                        qte.setText(cmd.getQte()+"");
                        heure.setText(cmd.getJour()+"\n"+cmd.getHeure());
                        dateLivraison.setText(cmd.getDateMaj());
                        if (ConnectionStatus.getInstance(context).isOnline()) {
                            new DownLoadImageTask(imgView).execute(cmd.getMenu().getImage());
                        } else {
                            new DownLoadImageTask(imgView, cmd.getMenu().getId()).execute(cmd.getMenu().getImage());
                        }

                        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                        dialog.setContentView(detailsview);
                        dialog.show();

                        ImageView btn_close=(ImageView) detailsview.findViewById(R.id.livre_close_menu);
                        btn_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //dialog.hide();
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        });
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            } else {
                Toast.makeText(NonLivreFragment.context, "Liste vide", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(NonLivreFragment.context, "Erreur du chargement de la grande liste", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            prepareDatas();
        }
    }
}
