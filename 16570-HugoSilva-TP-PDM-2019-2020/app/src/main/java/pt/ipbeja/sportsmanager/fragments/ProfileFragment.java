package pt.ipbeja.sportsmanager.fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.ipbeja.sportsmanager.R;

/**
 * Profile Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-05
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}