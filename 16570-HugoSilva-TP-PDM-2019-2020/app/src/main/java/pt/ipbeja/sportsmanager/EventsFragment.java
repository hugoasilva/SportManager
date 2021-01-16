package pt.ipbeja.sportsmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {
    ListView eventList;
    private List<Event> allEvents = new ArrayList<Event>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,
                container, false);
        ListView list = view.findViewById(R.id.lst_view);
        populateEvents();
        ArrayAdapter<Event> adapter = new MyListAdapter();
        list.setAdapter(adapter);

        return view;
    }

    // TODO TESTING ONLY, REMOVE BEFORE DELIVERY
    private void populateEvents() {
        allEvents.add(new Event(
                "Beja vs Cuba",
                "38.040485, -7.859489",
                "20-01-2021",
                "19:00",
                "football"
        ));
        allEvents.add(new Event(
                "Évora vs Portalegre",
                "38.568827, -7.880373",
                "20-01-2021",
                "20:00",
                "basketball"
        ));
        allEvents.add(new Event(
                "Moura vs Serpa",
                "38.164269, -7.404805",
                "20-01-2021",
                "21:00",
                "handbball"
        ));
        allEvents.add(new Event(
                "Ferreira vs Trigaches",
                "38.093039, -8.104675",
                "20-01-2021",
                "22:00",
                "football"
        ));

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

            TextView date = itemView.findViewById(R.id.event_date);
            date.setText("Data: " + currentEvent.getDate());

            TextView time = itemView.findViewById(R.id.event_time);
            time.setText("Hora: " + currentEvent.getTime());
            return itemView;
        }
    }
}