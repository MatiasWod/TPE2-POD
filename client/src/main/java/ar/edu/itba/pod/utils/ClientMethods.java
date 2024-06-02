package ar.edu.itba.pod.utils;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientMethods {

    private static final String PROPERTY_ADDRESSES = "addresses";
    private static final String PROPERTY_CITY = "city";
    private static final String PROPERTY_INPATH = "inPath";
    private static final String PROPERTY_OUTPATH = "outPath";
    private static final String PROPERTY_N = "n";
    private static final String PROPERTY_FROM = "from";
    private static final String PROPERTY_TO = "to";

    public static HazelcastInstance clientConfiguration(String[] addresses) {
        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new
                GroupConfig().setName("g10").setPassword("g10-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    public static List<Object> parseArguments(int queryNumber) {
        List<Object> toReturn = new ArrayList<>();
        final String addresses = System.getProperty(PROPERTY_ADDRESSES);
        final String city = System.getProperty(PROPERTY_CITY);
        final String inPath =  System.getProperty(PROPERTY_INPATH);
        final String outPath = System.getProperty(PROPERTY_OUTPATH);

        toReturn.add(addresses);
        toReturn.add(city);
        toReturn.add(inPath);
        toReturn.add(outPath);

        if (queryNumber == 3){
            final int n = Integer.parseInt(System.getProperty(PROPERTY_N));
            toReturn.add(n);
        }
        else if(queryNumber == 4){
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            final LocalDate from = LocalDate.parse(System.getProperty(PROPERTY_FROM),formatter);
            final LocalDate to = LocalDate.parse(System.getProperty(PROPERTY_TO),formatter);
            toReturn.add(from);
            toReturn.add(to);
        }

        return toReturn;
    }
}
