import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Random;
import java.util.ArrayList;



@Contract(
        name = "FlyNet",
        info = @Info(
                title = "FlyNet contract",
                description = "DMBLOCK assignement 3",
                version = "0.0.1"))

@Default
public class FlyNetContract implements ContractInterface {

    @Transaction()
    public boolean createFlight(Context ctx, String flyFrom, String flyTo, String dateTime, String seats) {
        if(!(ctx.getClientIdentity().getMSPID().equals("Org1MSP") || ctx.getClientIdentity().getMSPID().equals("Org2MSP"))) {
            return false;
        }

        String id = "";
        if(ctx.getClientIdentity().getMSPID().equals("Org1MSP")) {
            if(new String(ctx.getStub().getState("EC"), UTF_8).length() == 0 || new String(ctx.getStub().getState("EC"), UTF_8).equals("0")) {
                id = "EC1";
            }
            else {
                id = "EC" + (Integer.parseInt(new String(ctx.getStub().getState("EC"), UTF_8)) + 1);
            }
        }
        else {
            if(new String(ctx.getStub().getState("BS"), UTF_8).length() == 0 || new String(ctx.getStub().getState("BS"), UTF_8).equals("0")) {
                id = "BS1";
            }
            else {
                id = "BS" + (Integer.parseInt(new String(ctx.getStub().getState("BS"), UTF_8)) + 1);
            }
        }

        Let let = new Let(id, flyFrom, flyTo, dateTime, Integer.parseInt(seats));
        ctx.getStub().putState(id, let.toJSONString().getBytes(UTF_8));

        if(ctx.getClientIdentity().getMSPID().equals("Org1MSP")) {
            if(new String(ctx.getStub().getState("EC"), UTF_8).length() == 0) {
                ctx.getStub().putState("EC", "1".getBytes(UTF_8));
            }
            else {
                int count = Integer.parseInt(new String(ctx.getStub().getState("EC"), UTF_8)) + 1;
                ctx.getStub().putState("EC", Integer.toString(count).getBytes(UTF_8));
            }
        }
        else {
            if(new String(ctx.getStub().getState("BS"), UTF_8).length() == 0) {
                ctx.getStub().putState("BS", "1".getBytes(UTF_8));
            }
            else {
                int count = Integer.parseInt(new String(ctx.getStub().getState("BS"), UTF_8)) + 1;
                ctx.getStub().putState("BS", Integer.toString(count).getBytes(UTF_8));
            }
        }
        return true;
    }

    @Transaction()
    public String getCounts(Context ctx) {
        int count1 = Integer.parseInt(new String(ctx.getStub().getState("EC"), UTF_8));
        int count2 = Integer.parseInt(new String(ctx.getStub().getState("BS"), UTF_8));
        int count3 = Integer.parseInt(new String(ctx.getStub().getState("R"), UTF_8));

        return (count1 + " " + count2 + " " + count3);
    }

    @Transaction()
    public String getAllFlights(Context ctx) {
        int ec = -1;
        int bs = -1;
        if (new String(ctx.getStub().getState("EC"), UTF_8).length() == 0) {
            ec = 0;
            ctx.getStub().putState("EC", "0".getBytes(UTF_8));
        }
        else {
            ec = Integer.parseInt(new String(ctx.getStub().getState("EC"), UTF_8));
        }
        if (new String(ctx.getStub().getState("BS"), UTF_8).length() == 0) {
            bs = 0;
            ctx.getStub().putState("BS", "0".getBytes(UTF_8));
        }
        else {
            bs = Integer.parseInt(new String(ctx.getStub().getState("BS"), UTF_8));
        }

        String response = "{flights: [";
        Let let;
        if(ec != 0) {
            for(int i = 1; i <= ec; i++) {
                response += new String(ctx.getStub().getState("EC" + i),UTF_8);
                if(!(bs == 0 && i == ec)) {
                    response += ", ";
                }
            }
        }
        if(bs != 0) {
            for(int i = 1; i <= bs; i++) {
                response += new String(ctx.getStub().getState("BS" + i),UTF_8);
                if(i != bs) {
                    response += ", ";
                }
            }
        }
        response += "]}";
        return response;
    }

    @Transaction()
    public String getFlight(Context ctx, String id) {
        return new String(ctx.getStub().getState(id), UTF_8);
    }

    @Transaction()
    public boolean reserveSeats(Context ctx, String flightNr, String number, String[] customerNames, String customerEmail) {
        if(!(ctx.getClientIdentity().getMSPID().equals("Org3MSP"))) {
            return false;
        }

        if(Integer.parseInt(number) == customerNames.length) {
            String id = "";
            if(new String(ctx.getStub().getState("R"), UTF_8).length() == 0 || new String(ctx.getStub().getState("EC"), UTF_8).equals("0")) {
                id = "R1";
            }
            else {
                id = "R" + (Integer.parseInt(new String(ctx.getStub().getState("R"), UTF_8)) + 1);
            }


            Rezervacia rezervacia = new Rezervacia();
            rezervacia.setReservationNr(id);
            rezervacia.setFlightNr(flightNr);
            rezervacia.setNrOfSeats(Integer.parseInt(number));
            rezervacia.setCustomerNames(customerNames);
            rezervacia.setCustomerEmail(customerEmail);
            rezervacia.setStatus("Pending");
            ctx.getStub().putState(id, rezervacia.toJSONString().getBytes(UTF_8));

            if(new String(ctx.getStub().getState("R"), UTF_8).length() == 0) {
                ctx.getStub().putState("R", "1".getBytes(UTF_8));
            }
            else {
                int count = Integer.parseInt(new String(ctx.getStub().getState("R"), UTF_8)) + 1;
                ctx.getStub().putState("R", Integer.toString(count).getBytes(UTF_8));
            }
            return true;
        }
        return false;
    }

    @Transaction()
    public boolean bookSeats(Context ctx, String reservationNr) {
        if(!(ctx.getClientIdentity().getMSPID().equals("Org1MSP") || ctx.getClientIdentity().getMSPID().equals("Org2MSP"))) {
            return false;
        }
        Rezervacia rezervacia = Rezervacia.fromJSONString(new String(ctx.getStub().getState(reservationNr),UTF_8));
        Let let = Let.fromJSONString(new String(ctx.getStub().getState(rezervacia.getFlightNr()),UTF_8));

        if(let.getFlightNr().startsWith("EC") && ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return false;
        }
        if(let.getFlightNr().startsWith("BS") && ctx.getClientIdentity().getMSPID().equals("Org1MSP")) {
            return false;
        }

        if(let.getAvailablePlaces() >= rezervacia.getNrOfSeats()) {
            rezervacia.setStatus("Completed");
            let.setAvailablePlaces(let.getAvailablePlaces() - rezervacia.getNrOfSeats());
            ctx.getStub().putState(reservationNr, rezervacia.toJSONString().getBytes(UTF_8));
            ctx.getStub().putState(let.getFlightNr(), let.toJSONString().getBytes(UTF_8));
        }
        return true;
    }

    @Transaction()
    public boolean checkIn(Context ctx, String reservationNr, String[] passportIDs) {
        if(ctx.getClientIdentity().getMSPID().equals("Org1MSP") || ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return false;
        }

        boolean valid = true;
        Rezervacia rezervacia = Rezervacia.fromJSONString(new String(ctx.getStub().getState(reservationNr),UTF_8));
        Let let = Let.fromJSONString(new String(ctx.getStub().getState(rezervacia.getFlightNr()),UTF_8));

        String[] customerNames = rezervacia.getCustomerNames();
        if(passportIDs.length != customerNames.length) {
            valid = false;
        }

        if(valid) {
            for(int i = 0; i < passportIDs.length; i++) {                                                   // For cyklus pre vyhladanie mien v rezervacii
                for(int j = 0; j < customerNames.length; j++) {
                    if(j == customerNames.length - 1 && !passportIDs[i].equals(customerNames[j])) {
                        valid = false;                                                                      // Ak sa meno nenašlo, zmeň pomocnu premennu na false
                        break;
                    }
                    if(passportIDs[i].equals(customerNames[j])) {
                        break;
                    }
                }
                if(!valid) {                                                                                   // Ak sa niektoré meno nenašlo, zruš cyklus
                    break;
                }
            }
        }

        if(valid) {
            rezervacia.setStatus("Checked-In");
            ctx.getStub().putState(reservationNr, rezervacia.toJSONString().getBytes(UTF_8));
        }

        return valid;
    }
}
