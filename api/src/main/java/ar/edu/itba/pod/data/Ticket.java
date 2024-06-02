package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Ticket implements DataSerializable {

    private String plate;
    private int infractionCode;
    private String countyName;
    private int fineAmount;

    public Ticket() {
        //Serialization
    }

    public Ticket(final String plate, final int infractionCode, final String countyName, final int fineAmount) {
        this.plate = plate;
        this.infractionCode = infractionCode;
        this.countyName = countyName;
        this.fineAmount = fineAmount;
    }

    public String getPlate() {
        return plate;
    }

    public int getInfractionCode() {
        return infractionCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public int getFineAmount() {
        return fineAmount;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeInt(infractionCode);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeInt(fineAmount);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        infractionCode = objectDataInput.readInt();;
        countyName = objectDataInput.readUTF();
        fineAmount = objectDataInput.readInt();;
    }
}
