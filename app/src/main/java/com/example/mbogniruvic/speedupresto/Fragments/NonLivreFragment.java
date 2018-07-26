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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.models.Commande;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBOGNI RUVIC on 27/06/2018.
 */

public class NonLivreFragment extends Fragment {

    private List<Commande> cmdList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommandesAdapter mAdapter;


    public NonLivreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_non_livre, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerV_non_livre);

        mAdapter = new CommandesAdapter(cmdList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareDatas();

        //Gestion du clic sur un élément de la liste
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Commande cmd = cmdList.get(position);
                View detailsview = getLayoutInflater().inflate(R.layout.fragment_commande_details, null);

                TextView title=(TextView)detailsview.findViewById(R.id.title_menu);
                TextView montant=(TextView)detailsview.findViewById(R.id.montant_menu);
                TextView qte=(TextView)detailsview.findViewById(R.id.qte_menu);
                TextView heure=(TextView)detailsview.findViewById(R.id.heure_menu);
                Button mark=(Button) detailsview.findViewById(R.id.mark_cmd_btn);
                Button cancel=(Button) detailsview.findViewById(R.id.cancel_cmd_btn);

                title.setText(cmd.getMenu().getNom());
                montant.setText(cmd.getMontant()+" FCFA");
                qte.setText(cmd.getQte()+"");
                heure.setText(cmd.getHeure());

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
                        Toast.makeText(getContext(), cmd.getMenu().getId() + " a été livré", Toast.LENGTH_SHORT).show();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), cmd.getMenu().getId() + " a été refusé", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {
                Commande cmd = cmdList.get(position);
                Toast.makeText(getContext(), "Long click on : "+cmd.getMenu().getId(), Toast.LENGTH_SHORT).show();
            }
        }));


        return rootView;
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void prepareDatas() {

    }
}
