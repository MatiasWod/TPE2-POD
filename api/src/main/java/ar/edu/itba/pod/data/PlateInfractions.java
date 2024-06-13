package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class PlateInfractions implements DataSerializable {
    private String plate;
    private int infractionsAmount;

    public PlateInfractions() {}

    public PlateInfractions(String plate, int infractionsAmount) {
        this.plate = plate;
        this.infractionsAmount = infractionsAmount;
    }

    public String getPlate() {
        return plate;
    }

    public int getInfractionsAmount() {
        return infractionsAmount;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setInfractionsAmount(int infractionsAmount) {
        this.infractionsAmount = infractionsAmount;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeInt(infractionsAmount);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        infractionsAmount = objectDataInput.readInt();
    }
}
