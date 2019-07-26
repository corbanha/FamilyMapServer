package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

import dao.DataAccessException;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import handlers.Server;
import model.Event;
import model.Person;
import model.User;
import services.requests.FillRequest;
import services.results.FillResult;

public class FillService extends Service{

    public class DataHolder{
        String[] data;
    }

    public class Location{
        String country;
        String city;
        double latitude;
        double longitude;
    }

    public class LocationHolder{
        Location[] data;
    }

    private ArrayList<Person> persons = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();

    private String[] FEMALE_NAMES;
    private String[] MALE_NAMES;
    private String[] SUR_NAMES;
    private Location[] locations;

    public FillService(){
        loadInData();
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional "generations" parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     * @param fr the FillRequest containing the given userName and number of generations
     * @return the result of the attempting to perform the given request
     */
    public FillResult fill(FillRequest fr){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();

            UserDAO udao = new UserDAO(Server.getDatabase().getConnection());
            EventDAO edao = new EventDAO(Server.getDatabase().getConnection());
            PersonDAO pdao = new PersonDAO(Server.getDatabase().getConnection());

            //check if the user exists
            User u = udao.find(fr.getUserName());
            if(u == null){
                return new FillResult("This user \"" + fr.getUserName() + "\" does not exist");
            }

            if(fr.getGenerations() < 0){
                return new FillResult("Invalid number of generations, must be 0 or greater");
            }

            //delete any previous data of theirs
            edao.clearUser(fr.getUserName());
            pdao.clearUser(fr.getUserName());

            Person base = new Person(u.getPersonID(), fr.getUserName(),
                    u.getFirstName(), u.getLastName(), u.getGender(), null, null, null);

            persons.add(base);

            Event baseEvent = getRandEvent(fr.getUserName(), base.getPersonID(), "Birth",
                    varyYear(1995));
            events.add(baseEvent);

            int firstGenBirthYear = 1960;

            //additional generated generations
            if(fr.getGenerations() >= 1){
                Person dad = getRandPerson(fr.getUserName(), "m");
                Person mom = getRandPerson(fr.getUserName(), "f");

                base.setFatherID(dad.getPersonID());
                base.setMotherID(mom.getPersonID());

                events.add(getRandEvent(fr.getUserName(), dad.getPersonID(), "Birth", varyYear(firstGenBirthYear - 3)));
                events.add(getRandEvent(fr.getUserName(), dad.getPersonID(), "Baptism", varyYear(firstGenBirthYear + 8)));
                Event marriage = getRandEvent(fr.getUserName(), dad.getPersonID(), "Marriage", varyYear(firstGenBirthYear + 20));
                events.add(marriage);

                events.add(getRandEvent(fr.getUserName(), mom.getPersonID(), "Birth", varyYear(firstGenBirthYear)));
                events.add(getRandEvent(fr.getUserName(), mom.getPersonID(), "Baptism", varyYear(firstGenBirthYear + 8)));
                Event momMarriage = new Event(marriage);
                momMarriage.setEventID("EventID-" + UUID.randomUUID());
                momMarriage.setPersonID(mom.getPersonID());
                events.add(momMarriage);

                persons.add(dad);
                persons.add(mom);

                if(fr.getGenerations() >= 2){
                    String[] dadParAttr = generateParents(dad, fr.getGenerations() - 2, varyYear(firstGenBirthYear - 25));
                    dad.setFatherID(dadParAttr[0]);
                    dad.setMotherID(dadParAttr[1]);
                    dad.setLastName(dadParAttr[2]);

                    String[] momParAttr = generateParents(mom, fr.getGenerations() - 2, varyYear(firstGenBirthYear - 25));
                    mom.setFatherID(momParAttr[0]);
                    mom.setMotherID(momParAttr[1]);
                    mom.setLastName(momParAttr[2]);

                    dad.setSpouseID(mom.getPersonID());
                    mom.setSpouseID(dad.getPersonID());
                }
            }

            pdao.insert(persons.toArray(new Person[0]));
            edao.insert(events.toArray(new Event[0]));

            success = true;
            return new FillResult("Successfully added " + persons.size() + " persons and " + events.size() + " events to the database.");
        }catch(DataAccessException e){
            e.printStackTrace();
            return new FillResult("There was an error filling the user " + fr.getUserName() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }

    private String[] generateParents(Person child, int numFurtherGenerations, int currentYear){
        String username = child.getAssociatedUsername();
        Person dad = getRandPerson(username, "m");
        Person mom = getRandPerson(username, "f");

        String[] parAttributes = new String[3];
        parAttributes[0] = dad.getPersonID();
        parAttributes[1] = mom.getPersonID();
        parAttributes[2] = dad.getLastName();

        events.add(getRandEvent(username, dad.getPersonID(), "Birth", varyYear(currentYear - 3)));
        events.add(getRandEvent(username, dad.getPersonID(), "Death", varyYear(currentYear + 70)));
        Event marriage = getRandEvent(username, dad.getPersonID(), "Marriage", varyYear(currentYear + 20));
        events.add(marriage);

        events.add(getRandEvent(username, mom.getPersonID(), "Birth", varyYear(currentYear)));
        events.add(getRandEvent(username, mom.getPersonID(), "Death", varyYear(currentYear + 70)));
        Event momMarriage = new Event(marriage);
        momMarriage.setEventID("EventID-" + UUID.randomUUID());
        momMarriage.setPersonID(mom.getPersonID());
        events.add(momMarriage);

        dad.setSpouseID(mom.getPersonID());
        mom.setSpouseID(dad.getPersonID());

        persons.add(dad);
        persons.add(mom);

        if(numFurtherGenerations >= 1){

            String[] dadParAttr = generateParents(dad, numFurtherGenerations - 1, varyYear(currentYear - 27));
            dad.setFatherID(dadParAttr[0]);
            dad.setMotherID(dadParAttr[1]);
            dad.setLastName(dadParAttr[2]);
            parAttributes[2] = dadParAttr[2];

            String[] momParAttr = generateParents(mom, numFurtherGenerations - 1, varyYear(currentYear - 27));
            mom.setFatherID(momParAttr[0]);
            mom.setMotherID(momParAttr[1]);
            mom.setLastName(momParAttr[2]);
        }

        return parAttributes;
    }


    /**
     * Fills a person with individual fake data and also creates fake events for them
     * @return
     */
    private Person getRandPerson(String ausername, String gender){

        String fFirst = "";
        if(gender.toLowerCase().equals("m")){
            fFirst = MALE_NAMES[(int) Math.floor(Math.random() * MALE_NAMES.length)];
        }else{
            fFirst = FEMALE_NAMES[(int) Math.floor(Math.random() * FEMALE_NAMES.length)];
        }

        String fLast = SUR_NAMES[(int) Math.floor(Math.random() * SUR_NAMES.length)];

        Person p = new Person("PersonID-" + UUID.randomUUID(), ausername, fFirst,
                fLast, gender, null, null, null);

        return p;
    }

    private Event getRandEvent(String ausername, String personID, String eventType, int year){

        Location loc = locations[(int) Math.round(Math.floor(Math.random() * locations.length))];

        Event e = new Event("EventID-" + UUID.randomUUID(), ausername, personID,
                Double.toString(loc.latitude), Double.toString(loc.longitude),
                loc.country, loc.city, eventType, year);
        return e;
    }

    private int varyYear(int year){
        return year + ((int) Math.round(Math.random() * 5)) - 2;
    }

    private void loadInData(){
        Gson gson = new GsonBuilder().create();

        try{
            InputStream is = new FileInputStream("json/fnames.json");

            DataHolder femaleNames = gson.fromJson(new InputStreamReader(is), DataHolder.class);
            FEMALE_NAMES = femaleNames.data;

            InputStream is2 = new FileInputStream("json/mnames.json");

            DataHolder maleNames = gson.fromJson(new InputStreamReader(is2), DataHolder.class);
            MALE_NAMES = maleNames.data;

            InputStream is3 = new FileInputStream("json/snames.json");

            DataHolder surNames = gson.fromJson(new InputStreamReader(is3), DataHolder.class);
            SUR_NAMES = surNames.data;

            InputStream is4 = new FileInputStream("json/locations.json");

            LocationHolder locations_ = gson.fromJson(new InputStreamReader(is4), LocationHolder.class);
            locations = locations_.data;
        }catch(FileNotFoundException e){
            System.out.println("Couldn't find the files: " + e.getMessage());
        }
    }
}