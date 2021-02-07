package pt.ipbeja.sportsmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import pt.ipbeja.sportsmanager.R;
import pt.ipbeja.sportsmanager.activities.HomeActivity;
import pt.ipbeja.sportsmanager.data.Event;
import pt.ipbeja.sportsmanager.data.Position;

/**
 * Event Details Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-07
 */
public class EventDetailsFragment extends Fragment {
    private int eventNo;
    private Event event;
    private final List<Event> eventList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details,
                container, false);
//        TextView textView = view.findViewById(R.id.location_text);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.eventNo = bundle.getInt("key");
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Position position = new Position(
                                    doc.getDouble("latitude"),
                                    doc.getDouble("longitude"));
                            this.eventList.add(new Event(
                                    doc.getString("name"),
                                    position,
                                    doc.getString("date"),
                                    doc.getString("time"),
                                    doc.getString("category"),
                                    doc.getString("image")
                            ));
                        }
                        this.event = this.eventList.get(this.eventNo);
                        System.out.println(this.event.getPosition().toString());
                    }
                });
        return view;

    }
}