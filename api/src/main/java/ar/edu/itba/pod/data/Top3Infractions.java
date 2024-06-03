package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class Top3Infractions implements DataSerializable {
    private String top1;
    private String top2;
    private String top3;
    private int amountOfTicketsTop1=0;
    private int amountOfTicketsTop2=0;
    private int amountOfTicketsTop3=0;

    public Top3Infractions() {
    }

    public Top3Infractions(String top1, String top2, String top3, int amountOfTicketsTop1, int amountOfTicketsTop2, int amountOfTicketsTop3) {
        this.top1 = top1;
        this.top2 = top2;
        this.top3 = top3;
        this.amountOfTicketsTop1 = amountOfTicketsTop1;
        this.amountOfTicketsTop2 = amountOfTicketsTop2;
        this.amountOfTicketsTop3 = amountOfTicketsTop3;
    }

    public String getTop1() {
        return top1;
    }

    public String getTop2() {
        return top2;
    }

    public String getTop3() {
        return top3;
    }

    public int getAmountOfTicketsTop1() {
        return amountOfTicketsTop1;
    }

    public int getAmountOfTicketsTop2() {
        return amountOfTicketsTop2;
    }

    public int getAmountOfTicketsTop3() {
        return amountOfTicketsTop3;
    }

    public void setTop1(String top1) {
        this.top1 = top1;
    }

    public void setTop2(String top2) {
        this.top2 = top2;
    }

    public void setTop3(String top3) {
        this.top3 = top3;
    }

    public boolean isInTop1(String infraction){
        return infraction.equals(top1);
    }

    public boolean isInTop2(String infraction){
        return infraction.equals(top2) || isInTop1(infraction);
    }

    public boolean isInTop3(String infraction){
        return infraction.equals(top1) || infraction.equals(top2) || infraction.equals(top3);
    }

    public void setAmountOfTicketsTop1(int amountOfTicketsTop1) {
        this.amountOfTicketsTop1 = amountOfTicketsTop1;
    }

    public void setAmountOfTicketsTop2(int amountOfTicketsTop2) {
        this.amountOfTicketsTop2 = amountOfTicketsTop2;
    }

    public void setAmountOfTicketsTop3(int amountOfTicketsTop3) {
        this.amountOfTicketsTop3 = amountOfTicketsTop3;
    }

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Top3Infractions)) return false;
        Top3Infractions t = (Top3Infractions) o;
        return t.top1.equals(top1) && t.top2.equals(top2) && t.top3.equals(top3) && t.amountOfTicketsTop1 == amountOfTicketsTop1 && t.amountOfTicketsTop2 == amountOfTicketsTop2 && t.amountOfTicketsTop3 == amountOfTicketsTop3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(top1, top2, top3, amountOfTicketsTop1, amountOfTicketsTop2, amountOfTicketsTop3);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(top1);
        objectDataOutput.writeUTF(top2);
        objectDataOutput.writeUTF(top3);
        objectDataOutput.writeInt(amountOfTicketsTop1);
        objectDataOutput.writeInt(amountOfTicketsTop2);
        objectDataOutput.writeInt(amountOfTicketsTop3);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        top1 = objectDataInput.readUTF();
        top2 = objectDataInput.readUTF();
        top3 = objectDataInput.readUTF();
        amountOfTicketsTop1 = objectDataInput.readInt();
        amountOfTicketsTop2 = objectDataInput.readInt();
        amountOfTicketsTop3 = objectDataInput.readInt();
    }
}
