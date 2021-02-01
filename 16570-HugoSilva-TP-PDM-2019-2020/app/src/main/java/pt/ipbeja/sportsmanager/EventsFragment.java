package pt.ipbeja.sportsmanager;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {
    private RecyclerView eventRecyclerView;
    private RecyclerView.Adapter eventAdapter;
    private RecyclerView.LayoutManager eventLayoutManager;

//    ListView eventList;
//    private List<Event> allEvents = new ArrayList<Event>();
//
//    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,
                container, false);
//        ListView list = view.findViewById(R.id.lst_view);
//        db = FirebaseFirestore.getInstance();
//
//        this.populateEvents();
//        ArrayAdapter<Event> adapter = new MyListAdapter();
//        list.setAdapter(adapter);
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(new Event(
                1, R.drawable.ic_basketball,
                "Beja vs Cuba",
                "38.040485, -7.859489",
                "20-02-2021",
                "19:00",
                "basketball"));
        eventList.add(new Event(
                1, R.drawable.ic_handball,
                "Beja vs Ferreira",
                "39.040485, -8.859489",
                "21-02-2021",
                "20:00",
                "handball"));
        eventRecyclerView = view.findViewById(R.id.recyclerView);
        eventRecyclerView.setHasFixedSize(true);
        eventLayoutManager = new LinearLayoutManager(this.getContext());
        eventAdapter = new EventAdapter(eventList);
        eventRecyclerView.setLayoutManager(eventLayoutManager);
        eventRecyclerView.setAdapter(eventAdapter);
        return view;
    }
}