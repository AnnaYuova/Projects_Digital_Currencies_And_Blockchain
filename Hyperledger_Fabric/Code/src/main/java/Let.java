import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;


@DataType()
public class Let {

    @Property()
    private String flightNr;
    @Property()
    private String flyFrom;
    @Property()
    private String flyTo;
    @Property()
    private String dateTime;
    @Property()
    private int availablePlaces;

    public Let(String flightNr, String flyFrom, String flyTo, String dateTime, int availablePlaces) {
        this.flightNr = flightNr;
        this.flyFrom = flyFrom;
        this.flyTo = flyTo;
        this.dateTime = dateTime;
        this.availablePlaces = availablePlaces;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static Let fromJSONString(String json) {
        String flightNr = new JSONObject(json).getString("flightNr");
        String flyFrom = new JSONObject(json).getString("flyFrom");
        String flyTo = new JSONObject(json).getString("flyTo");
        String dateTime = new JSONObject(json).getString("dateTime");
        int availablePlaces = new JSONObject(json).getInt("availablePlaces");

        return new Let(flightNr, flyFrom, flyTo, dateTime, availablePlaces);
    }


    public String getFlightNr() {
        return flightNr;
    }

    public void setFlightNr(String flightNr) {
        this.flightNr = flightNr;
    }

    public String getFlyFrom() {
        return flyFrom;
    }

    public void setFlyFrom(String flyFrom) {
        this.flyFrom = flyFrom;
    }

    public String getFlyTo() {
        return flyTo;
    }

    public void setFlyTo(String flyTo) {
        this.flyTo = flyTo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

}
