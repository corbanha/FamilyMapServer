package com.corbanha.familymapserver.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Event;
import com.corbanha.familymapserver.model.Filter;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.model.Person;
import com.corbanha.familymapserver.model.Settings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.min;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String CENTER_EVENT_PUT_BUNDLE = "Center_Event";

    public static final float MAP_MARKER_COLORS[] = {
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_YELLOW,
            BitmapDescriptorFactory.HUE_ROSE
    };

    public static final int MAP_LINE_COLORS[] = {
            Color.parseColor("#e74c3c"), //alizarin
            Color.parseColor("#e67e22"), //Carrot
            Color.parseColor("#f1c40f"), //Sun flower
            Color.parseColor("#2ecc71"), //emerald
            Color.parseColor("#1abc9c"), //turquoise
            Color.parseColor("#3498db"), //peter river
            Color.parseColor("#9b59b6"), //amethyst
            Color.parseColor("#34495e"), //wet asphalt
            Color.parseColor("#ecf0f1"), //clouds
    };

    public static final float MAP_TERRIAN_TYPES[] = {
            GoogleMap.MAP_TYPE_NONE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_HYBRID
    };

    private GoogleMap mMap;

    private ImageView genderImage;
    private TextView line1TextView;
    private TextView line2TextView;
    private TextView line3TextView;
    private LinearLayout eventInfo;

    private Event selectedEvent = null;
    private boolean startedWithCenteredEvent = false;

    Model model = Model.getInstance();
    Settings settings = model.getSettings();
    Filter filter = model.getFilter();

    public static MapsFragment newInstance(){
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static MapsFragment newInstance(String eventID){
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(CENTER_EVENT_PUT_BUNDLE, eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final Bundle bundle = getArguments();

        if(bundle != null){
            String centerEventID = bundle.getString(CENTER_EVENT_PUT_BUNDLE, null);
            if(centerEventID != null) {
                selectedEvent = model.getEvent(centerEventID);
                startedWithCenteredEvent = true;
                setHasOptionsMenu(false);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        genderImage = v.findViewById(R.id.event_gender_icon);
        line1TextView = v.findViewById(R.id.event_details_line_1);
        line2TextView = v.findViewById(R.id.event_details_line_2);
        line3TextView = v.findViewById(R.id.event_details_line_3);

        mapFragment.getMapAsync(this);

        genderImage.setImageResource(R.drawable.android);
        eventInfo = v.findViewById(R.id.map_lower_event_details);

        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up the Event Activity
                if(selectedEvent != null && model.getPerson(selectedEvent.getPersonID()) != null){
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    intent.putExtra(PersonActivity.EXTRA_PERSON_ID, selectedEvent.getPersonID());
                    getActivity().startActivity(intent);
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mMap != null)
            prepareMap();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;

        prepareMap();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String tag = (String) marker.getTag();
                Event e = model.getEvent(tag);
                Person p;

                if(e != null && (p = model.getPerson(e.getPersonID())) != null){
                    selectedEvent = e;

                    genderImage.setImageResource(p.getGender().equals("m") ?
                            R.drawable.male : R.drawable.female);

                    line1TextView.setText(p.getFirstName() + " " + p.getLastName());
                    line2TextView.setText(e.getEventType() + ": " + e.getCity() + ",");
                    line3TextView.setText(e.getCountry() + " " + e.getYear());
                }
                return true;
            }
        });
    }

    private void prepareMap(){
        mMap.clear();

        model = Model.getInstance();
        settings = model.getSettings();
        filter = model.getFilter();

        mMap.setMapType(settings.getMapTypeSelection());
        System.out.println("PREPARING MAP!!");

        //add all of the users markers
        ArrayList<Event> filteredEvents = model.getFilterEvents();
        for(int i = 0; i < filteredEvents.size(); i++){
            Event e = filteredEvents.get(i);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(e.getLatitude()), Float.parseFloat(e.getLongitude())))
                    .icon(BitmapDescriptorFactory.defaultMarker(model.getMarkerColor(e.getEventType())))
                    .title(e.getEventType()));
            marker.setTag(e.getEventID());
            marker.setDraggable(false);
        }

        if(selectedEvent != null){
            //we've most likely updated the markers, so let's check if the previous event is null
            if(model.getEvent(selectedEvent.getEventID()) == null || !filteredEvents.contains(selectedEvent)){
                //previous
                genderImage.setImageResource(R.drawable.android);
                line1TextView.setText("Click a marker");
                line2TextView.setText("to see event details");
                line3TextView.setText("");
                selectedEvent = null;
            }else if(startedWithCenteredEvent){
                //we must want to center on this marker
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Float.parseFloat(selectedEvent.getLatitude()),
                                Float.parseFloat(selectedEvent.getLongitude()))).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Person p = model.getPerson(selectedEvent.getPersonID());

                genderImage.setImageResource(p.getGender().equals("m") ?
                        R.drawable.male : R.drawable.female);
                if(p != null) line1TextView.setText(p.getFirstName() + " " + p.getLastName());
                line2TextView.setText(selectedEvent.getEventType() + ": " + selectedEvent.getCity() + ",");
                line3TextView.setText(selectedEvent.getCountry() + " " + selectedEvent.getYear());



                int color = 0;
                //life story lines
                if(settings.isLifeStoryLinesEnabled()){
                    color = MAP_LINE_COLORS[min(settings.getLifeStoryLinesSelection(), MAP_LINE_COLORS.length)];

                    ArrayList<String> personEvents = model.getFilteredPersonEvents(selectedEvent.getPersonID());

                    for(int i = 0; personEvents != null && i < personEvents.size() - 1; i++){
                        Event e1 = model.getEvent(personEvents.get(i));
                        Event e2 = model.getEvent(personEvents.get(i + 1));

                        float e1_long = Float.parseFloat(e1.getLongitude());
                        float e1_lat = Float.parseFloat(e1.getLatitude());
                        float e2_long = Float.parseFloat(e2.getLongitude());
                        float e2_lat = Float.parseFloat(e2.getLatitude());

                        mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(e1_lat, e1_long), new LatLng(e2_lat, e2_long))
                                .width(5).color(color));
                    }

                }

                //spouse lines
                if(settings.isSpouseLinesEnabled()){
                    color = MAP_LINE_COLORS[min(settings.getSpouseLinesSelection(), MAP_LINE_COLORS.length)];

                    ArrayList<String> spouseEvents = model.getFilteredPersonEvents(model.getPerson(selectedEvent.getPersonID()).getSpouseID());

                    if(spouseEvents != null && spouseEvents.size() > 0){
                        Event spouseEvent = model.getEvent(spouseEvents.get(0));
                        float se_long = Float.parseFloat(spouseEvent.getLongitude());
                        float se_lat = Float.parseFloat(spouseEvent.getLatitude());
                        float ce_long = Float.parseFloat(selectedEvent.getLongitude()); //current event
                        float ce_lat = Float.parseFloat(selectedEvent.getLatitude());

                        mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(se_lat, se_long), new LatLng(ce_lat, ce_long))
                                .width(5).color(color));
                    }
                }

                //family tree lines
                if(settings.isFamilyTreeLinesEnabled()){
                    color = MAP_LINE_COLORS[min(settings.getFamilyTreeLinesSelection(), MAP_LINE_COLORS.length)];

                    drawFamilyTreeLine(model.getPerson(selectedEvent.getPersonID()), color, 12);
                }
            }
        }
    }

    private void drawFamilyTreeLine(Person person, int color, int lineWidth){
        if(person == null)
            return;

        if(lineWidth < 2)
            lineWidth = 3;

        ArrayList<String> personEvents = model.getFilteredPersonEvents(person.getPersonID());

        if(personEvents == null || personEvents.size() == 0)
            return;

        Event personEvent = model.getEvent(personEvents.get(0));

        float p_long = Float.parseFloat(personEvent.getLongitude());
        float p_lat = Float.parseFloat(personEvent.getLatitude());

        if(person.getFatherID() != null){
            //draw the line for the dad
            ArrayList<String> dadEvents = model.getFilteredPersonEvents(person.getFatherID());

            Event dadEvent = null;
            if (dadEvents.size() > 0 && (dadEvent = model.getEvent(dadEvents.get(0))) != null) {
                float dad_long = Float.parseFloat(dadEvent.getLongitude());
                float dad_lat = Float.parseFloat(dadEvent.getLatitude());

                mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(p_lat, p_long), new LatLng(dad_lat, dad_long))
                        .width(lineWidth).color(color));

                drawFamilyTreeLine(model.getPerson(person.getFatherID()), color, lineWidth - 3);
            }

        }

        if(person.getMotherID() != null){
            //draw the line for the mom
            ArrayList<String> momEvents = model.getFilteredPersonEvents(person.getMotherID());

            Event momEvent = null;
            if(momEvents.size() > 0 && (momEvent = model.getEvent(momEvents.get(0))) != null){
                float mom_long = Float.parseFloat(momEvent.getLongitude());
                float mom_lat = Float.parseFloat(momEvent.getLatitude());

                mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(p_lat, p_long), new LatLng(mom_lat, mom_long))
                    .width(lineWidth).color(color));

                drawFamilyTreeLine(model.getPerson(person.getMotherID()), color, lineWidth - 3);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_maps, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.maps_menu_search:
                getActivity().startActivity(new Intent(getContext(), SearchActivity.class));
                return true;
            case R.id.maps_menu_filter:
                getActivity().startActivity(new Intent(getContext(), FilterActivity.class));
                return true;
            case R.id.maps_menu_settings:
                getActivity().startActivity(new Intent(getContext(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
