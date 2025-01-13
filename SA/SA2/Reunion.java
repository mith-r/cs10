/**
 * @author Mithun Rameshkumar, Dartmouth CS10, Winter 2025
 * manages attendees and their RSVP statuses for the reunion
 * 1/13/25
 */

public class Reunion {
    protected int maxAttendees;
    protected Attendee[] attendees;
    private int count = 0;

    /**
     * Constructor for attendee class
     * @param maxAttendees - max number of attendees for array to hold
     */
    public Reunion(int maxAttendees){
        this.maxAttendees = maxAttendees;
        attendees = new Attendee[maxAttendees];
    }

    /**
     * Adds object to array if array not filled
     * @param attendee - object of Attendee or graduate class
     */
    public void addAttendee(Attendee attendee){
        if (count >= maxAttendees){
            System.out.println("Unable to accommodate " + attendee + ", max attendees is " + maxAttendees);
            return;
        } else{
            attendees[count] = attendee;
            System.out.println("Added " + attendee + ", rsvp: " + attendee.getRsvp());
            count++;
        }
    }

    /**
     * changes rsvp status of user if found in array
     * @param name - name of attendee to search for
     * @param rsvp - rsvp status to change to
     */
    public void rsvp (String name, boolean rsvp){
        for (int i = 0; i<count; i++){
            if (attendees[i].getName().equals(name)) {
                attendees[i].setRsvp(rsvp);
                System.out.println("Set RSVP to " + rsvp + " for " + name);
                return;
            }
        }
        System.out.println(name + " not found");
    }

    /**
     * toString representation of reunion class
     * @return list of attendees that have and haven't rsvp'd
     */
    public String toString(){
        String rsvp = "Reunion attendees that have RSVP:\n";
        String notRsvp = "\n Reunion attendees that have NOT RSVP:\n";
        String tab = "    ";

        for (Attendee attendee: attendees){
            if (attendee != null)
                if (attendee.getRsvp()){
                    rsvp += tab + attendee + "\n";
                } else{
                    notRsvp += tab + attendee + "\n";
            }
        }

        return rsvp + notRsvp;
    }

    public static void main(String[] args) {
        Reunion r = new Reunion(5);
        r.addAttendee(new Attendee("Alice", true));
        r.addAttendee(new Attendee("Bob", false));
        r.addAttendee(new Graduate("Charlie", true, 22, "Government"));
        r.addAttendee(new Graduate("Denise", false, 21, "Econ"));
        r.addAttendee(new Graduate("Elvis", true, 23, "Computer Science"));
        r.addAttendee(new Graduate("Falcon", false, 26, "Biology"));
        System.out.println(r);
        System.out.println();

        System.out.println("Update rsvp status");
        r.rsvp("George", false);  //print not found
        r.rsvp("Bob", true);  //update
        System.out.println(r);
    }
}
