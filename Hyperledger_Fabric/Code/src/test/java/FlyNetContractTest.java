import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.Test;

public final class FlyNetContractTest {

    @Nested
    class GetAllFLights {

        @Test
        public void noFlights() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("EC")).thenReturn(new byte[] {});
            when(stub.getState("BS")).thenReturn(new byte[] {});

            String result = contract.getAllFlights(ctx);
            assertEquals("{flights: []}", result);
        }

        @Test
        public void ec1Flight_bsEmpty() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            String response = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            when(stub.getState("EC")).thenReturn("1".getBytes());
            when(stub.getState("EC1")).thenReturn(response.getBytes());
            when(stub.getState("BS")).thenReturn(new byte[] {});

            String result = contract.getAllFlights(ctx);
            assertEquals("{flights: [{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}]}", result);
        }

        @Test
        public void ec1Flight_bs1Flight() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            String response1 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            String response2 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            when(stub.getState("EC")).thenReturn("1".getBytes());
            when(stub.getState("EC1")).thenReturn(response1.getBytes());
            when(stub.getState("BS")).thenReturn("1".getBytes());
            when(stub.getState("BS1")).thenReturn(response2.getBytes());

            String result = contract.getAllFlights(ctx);
            assertEquals("{flights: [{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}, {\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}]}", result);
        }

        @Test
        public void ec2Flights_bs1Flight() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            String response1 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            String response2 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            String response3 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC2\",\"availablePlaces\":360,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            when(stub.getState("EC")).thenReturn("2".getBytes());
            when(stub.getState("EC1")).thenReturn(response1.getBytes());
            when(stub.getState("EC2")).thenReturn(response3.getBytes());
            when(stub.getState("BS")).thenReturn("1".getBytes());
            when(stub.getState("BS1")).thenReturn(response2.getBytes());

            String result = contract.getAllFlights(ctx);
            assertEquals("{flights: [{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}, {\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC2\",\"availablePlaces\":360,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}, {\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}]}", result);
        }

        @Test
        public void ec2Flight_bs2Flight() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            String response1 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            String response2 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            String response3 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC2\",\"availablePlaces\":360,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            String response4 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS2\",\"availablePlaces\":360,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";
            when(stub.getState("EC")).thenReturn("2".getBytes());
            when(stub.getState("EC1")).thenReturn(response1.getBytes());
            when(stub.getState("EC2")).thenReturn(response3.getBytes());
            when(stub.getState("BS")).thenReturn("2".getBytes());
            when(stub.getState("BS1")).thenReturn(response2.getBytes());
            when(stub.getState("BS2")).thenReturn(response4.getBytes());

            String result = contract.getAllFlights(ctx);
            assertEquals("{flights: [{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}, {\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC2\",\"availablePlaces\":360,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}, {\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}, {\"dateTime\":\"42691337-0420\",\"flightNr\":\"BS2\",\"availablePlaces\":360,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}]}", result);
        }
    }

    @Nested
    class GetFLight {

        @Test
        public void notInLedger() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("EC")).thenReturn(new byte[] {});
            when(stub.getState("BS")).thenReturn(new byte[] {});
            when(stub.getState("EC7")).thenReturn(new byte[] {});

            String result = contract.getFlight(ctx, "EC7");
            assertEquals("", result);
        }

        @Test
        public void presentInLedger() {
            FlyNetContract contract = new FlyNetContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String response1 = "{\"dateTime\":\"42691337-0420\",\"flightNr\":\"EC1\",\"availablePlaces\":1337,\"flyFrom\":\"NZ\",\"flyTo\":\"BA\"}";

            when(stub.getState("EC")).thenReturn("1".getBytes());
            when(stub.getState("BS")).thenReturn(new byte[] {});
            when(stub.getState("EC1")).thenReturn(response1.getBytes());

            String result = contract.getFlight(ctx, "EC1");
            assertEquals(response1, result);
        }
    }
}
