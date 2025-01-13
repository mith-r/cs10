/**
 * @author Mithun Rameshkumar, Dartmouth CS10, Winter 2025
 * stores name and RSVP status of person attending reunion
 * 1/13/25
 */

public class Attendee {
    protected String name;
    protected boolean rsvp;

    /**
     * Constructor for Attendee method
     * @param name - name of attendee
     * @param rsvp - rsvp status of attendee
     */
    public Attendee(String name, boolean rsvp){
        this.name = name;
        this.rsvp = rsvp;
    }

    /**
     * sets name of attendee
     * @param name - name of attendee
     */
    public void setName(String name){this.name = name;}

    /**
     * sets rsvp status of attendee
     * @param rsvp - boolean of rsvp status
     */
    public void setRsvp(boolean rsvp){this.rsvp = rsvp;}

    /**
     * gets name of attendee
     * @return - name of attendee
     */
    public String getName(){return name;}

    /**
     * gets rsvp status of attendee
     * @return - rsvp status of attendee
     */
    public boolean getRsvp(){return rsvp;}

    /**
     * String representation of class
     * @return - name of attendee
     */
    public String toString(){
        return name;
    }
}
