package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.RestaurantPreferencesDB;

public class ProfileInfosFragment extends Fragment {

    RestaurantPreferencesDB shareDB;

    private TextView mobileView;
    private TextView emailView;
    private TextView descView;
    private TextView nbNoteView;
    private TextView minOrderView;
    private TextView fraisLivraiView;
    private TextView dureeLivrasView;
    private TextView etatView;

    public ProfileInfosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_infos, container, false);

        shareDB= MainActivity.shareDB;

        //get Adresses
        mobileView=(TextView)rootView.findViewById(R.id.prof_phone_text);
        emailView=(TextView)rootView.findViewById(R.id.prof_email_text);
        descView=(TextView)rootView.findViewById(R.id.prof_bio_text);
        nbNoteView=(TextView)rootView.findViewById(R.id.prof_nbr_notes);
        minOrderView=(TextView)rootView.findViewById(R.id.prof_min_order);
        fraisLivraiView=(TextView)rootView.findViewById(R.id.prof_frais_livrais);
        dureeLivrasView=(TextView)rootView.findViewById(R.id.prof_duree_livras);
        etatView=(TextView)rootView.findViewById(R.id.prof_etat);

        //init field
        mobileView.setText(shareDB.getString(RestaurantPreferencesDB.PHONE_KEY, "<mobile>"));
        emailView.setText(shareDB.getString(RestaurantPreferencesDB.EMAIL_KEY, "<email>"));
        descView.setText(shareDB.getString(RestaurantPreferencesDB.BIO_KEY, "<description>"));

        int minOrder=shareDB.getInt(RestaurantPreferencesDB.MIN_ORDER_KEY, 0);
        long frais=shareDB.getLong(RestaurantPreferencesDB.FEE_DELIVERY_KEY, 0);
        long duree=shareDB.getLong(RestaurantPreferencesDB.TIME_DELIVERY_KEY, 0);
        boolean etat=shareDB.getBoolean(RestaurantPreferencesDB.STATE_KEY, false);

        minOrderView.setText(minOrder+" FCFA");
        fraisLivraiView.setText(frais+" FCFA");
        dureeLivrasView.setText(duree+" min");

        etatView.setText((etat)?"Activé":"Desactivé");
        nbNoteView.setText("Undefined");

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

}
