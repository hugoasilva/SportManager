package pt.ipbeja.sportsmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import pt.ipbeja.sportsmanager.data.Event;
import pt.ipbeja.sportsmanager.data.Position;

public class AddEventFragment extends Fragment implements OnMapReadyCallback {

    private static final int PHOTO_REQUEST_CODE = 1001;

    private Marker marker;
    private ImageView photoImageView;
    private Spinner spinner;
    private Bitmap photoBitmap;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event,
                container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        EditText nameInput = view.findViewById(R.id.event_name_input);
        EditText dateInput = view.findViewById(R.id.event_date_input);
        EditText timeInput = view.findViewById(R.id.event_time_input);
        Button createBtn = view.findViewById(R.id.add_event_btn);
        this.spinner = view.findViewById(R.id.spinner);
//        this.photoImageView = view.findViewById(R.id.event_photo);

        createBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String date = dateInput.getText().toString();
            String time = timeInput.getText().toString();
            String sport = spinner.getSelectedItem().toString();

            if (name.isEmpty() || date.isEmpty()
                    || time.isEmpty() || sport.isEmpty() || marker == null) {
                Snackbar.make(nameInput, "Prencher campos", Snackbar.LENGTH_SHORT).show();
            } else {
                LatLng latLng = marker.getPosition();
                Position position = new Position(latLng.latitude, latLng.longitude);
                System.out.println(latLng);
//                String filename = null;

//                // Vamos converter um bitmap para bytes
//                byte[] photoBytes = BitmapUtils.toBytes(photoBitmap);
//
//                if (photoBytes != null) {
//                    // https://developer.android.com/training/data-storage
//                    File folder = getActivity()
//                            .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                    filename = UUID.randomUUID().toString() + ".jpg"; // Um filename aleatório (podiamos usar um timestamp)
//                    File file = new File(folder, filename);
//                    try {
//                        Files.write(file.toPath(), photoBytes); // gravar os bytes no ficheiro
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                // Atenção que este exemplo mostra 2 formas de guardar os bytes do bitmap
                // Por um lado guarda os bytes na BD (menos correcto, mas ok para 'poucos' bytes, ver BLOB) - photoBytes
                // Por outro guarda o caminho para onde o ficheiro está guardado (mais correcto)  - filename
                Event event = new Event(1, name, String.valueOf(position.getLat()),
                        String.valueOf(position.getLng()), date, time, sport);
//                ChatDatabase.getInstance(getApplicationContext())
//                        .contactDao()
//                        .insert(contact);

                System.out.println(event.getName() + " " + event.getDate() + " " + event.getLatitude());
                getActivity().getSupportFragmentManager().beginTransaction().replace(
                        R.id.frg_space, new EventsFragment()).commit();
            }
        });

//        this.photoImageView.setOnClickListener(v -> takePhoto());

        return view;
    }

//    private void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, PHOTO_REQUEST_CODE);
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            this.photoBitmap = data.getParcelableExtra("data");
            photoImageView.setImageBitmap(photoBitmap);
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMapClickListener(latLng -> {

            if (this.marker == null) {
                this.marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                );
            } else {
                this.marker.setPosition(latLng);
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
}
