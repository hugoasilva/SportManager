package pt.ipbeja.sportsmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment implements View.OnClickListener {
    ListView eventList;
    private List<Event> allEvents = new ArrayList<Event>();

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,
                container, false);
        ListView list = view.findViewById(R.id.lst_view);
        db = FirebaseFirestore.getInstance();

        this.populateEvents();
        ArrayAdapter<Event> adapter = new MyListAdapter();
        list.setAdapter(adapter);

        return view;
    }

    // TODO TESTING ONLY, REMOVE BEFORE DELIVERY
    private void populateEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            allEvents.add(new Event(
//                                    Integer.parseInt(doc.getId()),
                                    doc.getString("name"),
                                    doc.getString("location"),
                                    doc.getString("date"),
                                    doc.getString("time"),
                                    doc.getString("category")
                            ));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

    private class MyListAdapter extends ArrayAdapter<Event> {
        public MyListAdapter() {
            super(EventsFragment.this.getActivity(), R.layout.event_view, allEvents);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.event_view, parent, false);

            }
            Event currentEvent = allEvents.get(position);

//            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            imageView.setImageResource(currentEvent.getImageid());

            TextView name = itemView.findViewById(R.id.event_name);
            name.setText(currentEvent.getName());
            System.out.println(currentEvent.getName());

            TextView date = itemView.findViewById(R.id.event_date);
            date.setText("Data: " + currentEvent.getDate());

            TextView time = itemView.findViewById(R.id.event_time);
            time.setText("Hora: " + currentEvent.getTime());
            return itemView;
        }
    }
}