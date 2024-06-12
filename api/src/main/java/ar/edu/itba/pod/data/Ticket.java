package ar.edu.itba.pod.data;

import com.hazelcast.internal.serialization.impl.JavaDefaultSerializers;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Ticket implements DataSerializable {

    private String plate;
    private String issueDate;
    private String infractionCode;
    private String countyName;
    private double fineAmount;
    private String agency;

    public Ticket() {
        //Serialization
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public Ticket(String plate, String issueDate, String infractionCode, String countyName, double fineAmount, String agency) {
        this.plate = plate;
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.countyName = countyName;
        this.fineAmount = fineAmount;
        this.agency = agency;
    }

    public String getPlate() {
        return plate;
    }

    public Date getIssueDate() throws ParseException {
        return DATE_FORMAT.parse(issueDate) ;
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

    public String getAgency() {
        return agency;
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeUTF(issueDate);
        objectDataOutput.writeUTF(infractionCode);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeDouble(fineAmount);
        objectDataOutput.writeUTF(agency);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException{
        plate = objectDataInput.readUTF();
        issueDate = objectDataInput.readUTF();
        infractionCode = objectDataInput.readUTF();
        countyName = objectDataInput.readUTF();
        fineAmount = objectDataInput.readDouble();
        agency = objectDataInput.readUTF();
    }
}
