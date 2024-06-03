package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class TopAgenciesFineAmount implements DataSerializable {

    private String agencyName;
    private double fineAmount;

    public TopAgenciesFineAmount() {
        //Serialization
    }

    public TopAgenciesFineAmount(String agencyName, double fineAmount) {
        this.agencyName = agencyName;
        this.fineAmount = fineAmount;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(agencyName);
        out.writeDouble(fineAmount);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agencyName = in.readUTF();
        fineAmount = in.readDouble();
    }
}
