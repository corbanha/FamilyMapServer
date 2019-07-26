package services.results;

import model.Person;

public class PersonResult extends Result {

    private Person person;

    /**
     * Creates a person Result object with an error message
     * @param message the error message that resulted when trying to get the person
     */
    public PersonResult(String message){
        super(message);
    }

    /**
     * Creates a person Result object with the associated person that was obtained
     * @param person the person found in the search
     */
    public PersonResult(Person person){
        super("");
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
