package pt.ipbeja.sportsmanager;

import androidx.fragment.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class EventsFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView eventRecyclerView;

//    private ArrayList<Event> eventList = new ArrayList<>();

    private FirestoreRecyclerAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,
                container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        eventRecyclerView = view.findViewById(R.id.recyclerView);


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
                holder.imageView.setImageResource(model.getImageResource());
                holder.nameView.setText(model.getName());
                holder.dateView.setText(model.getDate());
            }
        };

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        eventRecyclerView.setAdapter(adapter);


//        this.populateEvents();

//        eventList.add(new Event(
//                1, R.drawable.ic_basketball,
//                "Beja vs Cuba",
//                "38.040485, -7.859489",
//                "20-02-2021",
//                "19:00",
//                "basketball"));
//        eventList.add(new Event(
//                1, R.drawable.ic_handball,
//                "Beja vs Ferreira",
//                "39.040485, -8.859489",
//                "21-02-2021",
//                "20:00",
//                "handball"));
//        eventRecyclerView = view.findViewById(R.id.recyclerView);
//        eventRecyclerView.setHasFixedSize(true);
//        eventLayoutManager = new LinearLayoutManager(this.getContext());
//        eventAdapter = new EventAdapter(this.eventList);
//        eventRecyclerView.setLayoutManager(eventLayoutManager);
//        eventRecyclerView.setAdapter(eventAdapter);
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

//    // TODO TESTING ONLY, REMOVE BEFORE DELIVERY
//    private void populateEvents() {
//        firebaseFirestore.collection("events")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot doc : task.getResult()) {
//                            this.eventList.add(new Event(
//                                    R.drawable.ic_handball,
//                                    doc.getString("name"),
//                                    doc.getString("location"),
//                                    doc.getString("date"),
//                                    doc.getString("time"),
//                                    doc.getString("category")
//                            ));
//                            System.out.println(doc.getString("name"));
//                        }
//                    }
//                });
//    }
}