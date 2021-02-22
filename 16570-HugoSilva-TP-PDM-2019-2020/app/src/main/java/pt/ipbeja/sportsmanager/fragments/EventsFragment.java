/*
 * Copyright 2021 Hugo Silva @ IPBeja
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ipbeja.sportsmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * @version 2021-02-22
 */
public class EventsFragment extends Fragment {
    private RecyclerView eventRecyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    /**
     * Creates fragment
     *
     * @param savedInstanceState bundle object
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().moveTaskToBack(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Creates view
     *
     * @param inflater           layout inflater object
     * @param container          view group object
     * @param savedInstanceState bundle object
     * @return view
     */
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
        System.out.println(query.get().toString());
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
                int icon = 0;
                switch (model.getCategory()) {
                    case "Basquetebol":
                    case "Basketball":
                        icon = R.drawable.ic_basketball;
                        break;
                    case "Futebol":
                    case "Football":
                        icon = R.drawable.ic_baseball;
                        break;
                    case "Andebol":
                    case "Handball":
                        icon = R.drawable.ic_handball;
                        break;
                }
                holder.imageView.setImageResource(icon);
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

    /**
     * Listens to adapter start
     */
    @Override
    public void onStart() {
        super.onStart();
        this.adapter.startListening();
    }

    /**
     * Listens to adapter stop
     */
    @Override
    public void onStop() {
        super.onStop();
        this.adapter.stopListening();
    }

    /**
     * Event view holder inner class
     */
    private class EventViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView nameView;
        private TextView dateView;

        /**
         * Constructor
         *
         * @param itemView item in list
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            nameView = itemView.findViewById(R.id.name_view);
            dateView = itemView.findViewById(R.id.date_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                System.out.println(position);
                Bundle bundle = new Bundle();
                bundle.putInt("key", position);
                Fragment detailsFragment = new EventDetailsFragment();
                detailsFragment.setArguments(bundle);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frg_space, detailsFragment)
                        .commit();
            });
        }
    }
}