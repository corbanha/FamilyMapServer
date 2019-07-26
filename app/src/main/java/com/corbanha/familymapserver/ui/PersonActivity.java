package com.corbanha.familymapserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Event;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.model.Person;
import com.corbanha.familymapserver.ui.PersonExpandableListAdaptor.ItemRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PersonActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "Person";

    private TextView nameTextView;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter personElistAdaptor;
    private Person person;

    private Model model;

    //junk data
    private List<String> titles;
    private HashMap<String, List<ItemRow>> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        nameTextView = findViewById(R.id.person_name_text_view);
        expandableListView = findViewById(R.id.login_expandable_list_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String personID = getIntent().getStringExtra(EXTRA_PERSON_ID);

        person = Model.getInstance().getPerson(getIntent().getStringExtra(EXTRA_PERSON_ID));

        nameTextView.setText(person.getFirstName() + " " + person.getLastName() + " (" + person.getGender() + ")");

        model = Model.getInstance();

        //fill in with junk data

        titles = new ArrayList<String>();
        titles.add("Life Events");
        titles.add("Family");

        listDetails = new HashMap<>();
        
        ArrayList<ItemRow> lifeEvents = new ArrayList<>();
        //events come from the model presorted
        ArrayList<String> eventIDs = Model.getInstance().getFilteredPersonEvents(personID);

        for(int i = 0; i < eventIDs.size(); i++){
            Event e = Model.getInstance().getEvent(eventIDs.get(i));
            if(e != null)
                lifeEvents.add(new ItemRow(R.drawable.map_pin,
                        e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")",
                        person.getFirstName() + " " + person.getLastName(), e.getEventID(),
                        e.getEventType(), e.getYear()));
        }

        ArrayList<ItemRow> familyMembers = new ArrayList<>();

        //parents
        if(person.getFatherID() != null){
            Person father = model.getPerson(person.getFatherID());
            familyMembers.add(new ItemRow(R.drawable.male, father.getFirstName() + " " + father.getLastName(), "Father", father.getPersonID()));
        }

        if(person.getMotherID() != null){
            Person mother = model.getPerson(person.getMotherID());
            familyMembers.add(new ItemRow(R.drawable.female, mother.getFirstName() + " " + mother.getLastName(), "Mother", mother.getPersonID()));
        }

        //spouse
        if(person.getSpouseID() != null){
            Person spouse = model.getPerson(person.getSpouseID());
            familyMembers.add(new ItemRow(spouse.getGender().equals("m") ? R.drawable.male :
                    R.drawable.female, spouse.getFirstName() + " " + spouse.getLastName(), "Spouse", spouse.getPersonID()));
        }

        //children
        ArrayList<String> childrenIDs = model.getChildren(person.getPersonID());
        for(int i = 0; i < childrenIDs.size(); i++){
            Person child = model.getPerson(childrenIDs.get(i));
            familyMembers.add(new ItemRow(child.getGender().equals("m") ? R.drawable.male :
                    R.drawable.female, child.getFirstName() + " " + child.getLastName(), child.getGender().equals("m") ? "Son" : "Daughter", child.getPersonID()));
        }

        listDetails.put("Life Events", lifeEvents);
        listDetails.put("Family", familyMembers);

        personElistAdaptor = new PersonExpandableListAdaptor(this, titles, listDetails);

        expandableListView.setAdapter(personElistAdaptor);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);

        //Listen for when a group is expanded
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 0){
                    //Life Events open up a new Event Activity
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    System.out.println("SENDING EVENT ID : " + listDetails.get(titles.get(groupPosition)).get(childPosition).eventOrPersonId);
                    intent.putExtra(EventActivity.EXTRA_EVENT_ID, listDetails.get(titles.get(groupPosition)).get(childPosition).eventOrPersonId);
                    startActivity(intent);

                }else if(groupPosition == 1){
                    //Family Members, open up a new Person Activity
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(EXTRA_PERSON_ID, listDetails.get(titles.get(groupPosition)).get(childPosition).eventOrPersonId);
                    startActivity(intent);
                }

                return false;
            }
        });

    }

}
