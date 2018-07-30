package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
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
import com.example.mbogniruvic.speedupresto.models.Commande;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBOGNI RUVIC on 27/06/2018.
 */

public class LivreFragment extends Fragment {

    private List<Commande> cmdList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommandesAdapter mAdapter;
    private ProgressBar progressBar;


    public LivreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_livre, container, false);
        //get Adresses
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerV_livre);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_livre);


        return rootView;
    }

    public void prepareDatas() {

        if (NonLivreFragment.livreCmdList.size()!=0) {

            cmdList=NonLivreFragment.livreCmdList;
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
                    new DownLoadImageTask(imgView).execute(cmd.getMenu().getImage());

                    final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                    dialog.setContentView(detailsview);
                    dialog.show();

                    ImageView btn_close=(ImageView) detailsview.findViewById(R.id.livre_close_menu);
                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hide();
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
            Toast.makeText(getContext(), "Liste vide", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            prepareDatas();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




}
