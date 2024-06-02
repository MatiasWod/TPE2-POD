package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) {
        logger.info("hz-config Server Starting ...");

        // Config
        Config config = new Config();

        // Group Config
        GroupConfig groupConfig = new GroupConfig()
                .setName("g10")
                .setPassword("g10-pass");

        config.setGroupConfig(groupConfig);


        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList("127.0.0.*"))
                .setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        config.setProperty("hazelcast.logging.type","none");

        config.getMultiMapConfig("g10-namespace-multimap").setValueCollectionType(MultiMapConfig.ValueCollectionType.LIST);

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}

