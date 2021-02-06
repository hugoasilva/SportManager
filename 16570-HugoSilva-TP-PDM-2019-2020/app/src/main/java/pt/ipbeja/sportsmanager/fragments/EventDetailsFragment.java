package pt.ipbeja.sportsmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.ipbeja.sportsmanager.R;

/**
 * Event Details Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-06
 */
public class EventDetailsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frg_space, new EventsFragment())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details,
                container, false);
        TextView textView = view.findViewById(R.id.location_text);
        Bundle bundle = this.getArguments();
        int eventNo = 0;
        if (bundle != null) {
            eventNo = bundle.getInt("key");
            textView.setText(Integer.toString(eventNo));
        }
        return view;
    }
}