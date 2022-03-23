package etherip;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

public class DemoTest {

    private static final Logger logger = Logger.getLogger(DemoTest.class.getName());

    public static void main(String[] args) throws SocketException {
        logger.info("Initializing DemoTest");
        try {
            EtherNetIP plc = new EtherNetIP("10.0.1.100", 0);
            plc.connectTcp();
            List<TagReadReply> datas = plc.connectAndReadTags("Sensor1", "Sensor2" , "Sensor10");
            datas.forEach(each -> {
                logger.info("Tag name:" + each.getTag() + " is valid ? " + each.isValid());
            });

        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Exception occurred:" + e.getMessage());
        }

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface nif: Collections.list(nets)) {
            //do something with the network interface
            logger.info(nif.getName());
        }
    }
}
