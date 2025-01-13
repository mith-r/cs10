/**
 * @author Mithun Rameshkumar, Dartmouth CS10, Winter 2025
 * represents dartmouth grad with grad year and department
 * 1/13/25
 */
public class Graduate extends Attendee {
    int gradYear;
    String department;

    /**
     * Constructor for graduate method
     * @param name - name from Attendee class
     * @param rsvp - rrsvp status from attendee class
     * @param gradYear - graduation year of dartmouth grad
     * @param department - department of dartmouth grad
     */
    public Graduate(String name, boolean rsvp, int gradYear, String department){
        super(name, rsvp);
        this.gradYear = gradYear;
        this.department = department;
    }

    /**
     * string representation of graduate
     * @return - attendee toString in addition to grad year and department
     */
    @Override
    public String toString(){
        return super.toString() + " " + gradYear + " " + department;
    }
}
