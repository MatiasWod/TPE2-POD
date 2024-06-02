package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Ticket implements DataSerializable {

    private String plate;
    private String infractionCode;
    private String countyName;
    private double fineAmount;

    public Ticket() {
        //Serialization
    }

    public Ticket(final String plate, final String infractionCode, final String countyName, final double fineAmount) {
        this.plate = plate;
        this.infractionCode = infractionCode;
        this.countyName = countyName;
        this.fineAmount = fineAmount;
    }

    public String getPlate() {
        return plate;
    }

    public String getInfractionCode() {
        return infractionCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeUTF(infractionCode);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeDouble(fineAmount);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        infractionCode = objectDataInput.readUTF();;
        countyName = objectDataInput.readUTF();
        fineAmount = objectDataInput.readDouble();;
    }
}
