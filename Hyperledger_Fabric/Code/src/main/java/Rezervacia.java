import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;
import org.json.JSONArray;

public class Rezervacia {

    @Property()
    private String reservationNr;
    @Property()
    private String[] customerNames;
    @Property()
    private String customerEmail;
    @Property()
    private String flightNr;
    @Property()
    private int nrOfSeats;
    @Property()
    private String status;


    public Rezervacia(String reservationNr, String[] customerNames, String customerEmail, String flightNr, int nrOfSeats, String status) {
        this.reservationNr = reservationNr;
        this.customerNames = customerNames;
        this.customerEmail = customerEmail;
        this.flightNr = flightNr;
        this.nrOfSeats = nrOfSeats;
        this.status = status;
    }

    public Rezervacia() {

    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }



    public static Rezervacia fromJSONString(String json) {
        String[] customerNames = new String[0];

        String reservationNr = new JSONObject(json).getString("reservationNr");
        String customerEmail = new JSONObject(json).getString("customerEmail");
        String flightNr = new JSONObject(json).getString("flightNr");
        int nrOfSeats = new JSONObject(json).getInt("nrOfSeats");
        String status = new JSONObject(json).getString("status");
        JSONArray customers = new JSONObject(json).getJSONArray("customerNames");

        for (int i = 0; i < customers.length(); i++) {
            String[] newArray = new String[customerNames.length + 1];
            System.arraycopy(customerNames, 0, newArray, 0, customerNames.length);
            customerNames = newArray;
            customerNames[customerNames.length - 1] = customers.getString(i);
        }
        return new Rezervacia(reservationNr, customerNames, customerEmail, flightNr, nrOfSeats, status);
    }

    public String getReservationNr() {
        return reservationNr;
    }

    public void setReservationNr(String reservationNr) {
        this.reservationNr = reservationNr;
    }

    public String[] getCustomerNames() {
        return customerNames;
    }

    public void setCustomerNames(String[] customerNames) {
        this.customerNames = customerNames;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getFlightNr() {
        return flightNr;
    }

    public void setFlightNr(String flightNr) {
        this.flightNr = flightNr;
    }

    public int getNrOfSeats() {
        return nrOfSeats;
    }

    public void setNrOfSeats(int nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
