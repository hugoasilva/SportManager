package pt.ipbeja.sportsmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipbeja.sportsmanager.data.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private ArrayList<Event> eventList;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameView;
        public TextView dateView;

        public EventViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            nameView = itemView.findViewById(R.id.name_view);
            dateView = itemView.findViewById(R.id.date_view);
        }
    }

    public EventAdapter(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.event_view, parent, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event currentEvent = this.eventList.get(position);
        holder.imageView.setImageResource(currentEvent.getIcon());
        holder.nameView.setText(currentEvent.getName());
        holder.dateView.setText(currentEvent.getDate());
    }

    @Override
    public int getItemCount() {
        return this.eventList.size();
    }
}
