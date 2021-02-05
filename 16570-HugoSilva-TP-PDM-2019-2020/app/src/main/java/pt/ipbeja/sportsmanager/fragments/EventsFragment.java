package pt.ipbeja.sportsmanager.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import pt.ipbeja.sportsmanager.R;
import pt.ipbeja.sportsmanager.data.Event;


/**
 * Events Fragment Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-05
 */
public class EventsFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView eventRecyclerView;

//    private ArrayList<Event> eventList = new ArrayList<>();

    private FirestoreRecyclerAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events,
                container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        eventRecyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton addEventButton = view.findViewById(R.id.add_event);


        // Query
        Query query = firebaseFirestore.collection("events");
        // RecyclerOptions
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Event, EventViewHolder>(options) {
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.event_view, parent, false);
                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder,
                                            int position, @NonNull Event model) {
                holder.imageView.setImageResource(model.getIcon());
                holder.nameView.setText(model.getName());
                holder.dateView.setText(model.getDate());
            }
        };

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        eventRecyclerView.setAdapter(adapter);

        addEventButton.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(
                        R.id.frg_space, new AddEventFragment()).commit());
        return view;
    }

    private class EventViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView nameView;
        private TextView dateView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            nameView = itemView.findViewById(R.id.name_view);
            dateView = itemView.findViewById(R.id.date_view);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}