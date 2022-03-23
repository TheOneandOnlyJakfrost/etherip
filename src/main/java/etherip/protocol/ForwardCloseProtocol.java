package etherip.protocol;

import etherip.util.CIPConstants;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import static etherip.types.CNPath.ConnectionManager;
import static etherip.types.CNService.CM_ForwardClose;

/**
 * The Forward_Close Service (Service Code = 4E hex ) is used to close a connection with a Target
 * Device (and all other nodes in the connection path). The Forward_Close request shall remove a
 * connection from all the nodes participating in the original connection.
 */
public class ForwardCloseProtocol extends ProtocolAdapter {

    private static final Logger logger = Logger.getLogger(ForwardCloseProtocol.class.getSimpleName());

    private final ProtocolEncoder encoder;
    private final short connectionSerialNumber;

    public ForwardCloseProtocol(short connectionSerialNumber) {
        this.encoder = new MessageRouterProtocol(CM_ForwardClose, ConnectionManager(), new ProtocolAdapter());
        this.connectionSerialNumber = connectionSerialNumber;
    }

    @Override
    public int getRequestSize() {
        int result = this.encoder.getRequestSize() + 16; // 16 bytes of data are encoded
        return result;
    }

    @Override
    public void encode(ByteBuffer buf, StringBuilder log) throws Exception {
        this.encoder.encode(buf, log);
        buf.put((byte) 10); // Tick time
        buf.put((byte) 5); // Time-out ticks
        // Connection serial number
        buf.putShort(connectionSerialNumber);
        // Originator vendor id and serial number
        buf.putShort(CIPConstants.ORIGINATOR_VENDOR_ID);
        buf.putInt(CIPConstants.ORIGINATOR_SERIAL_NUMBER);
        // Connection_Path_Size - The number of 16 bit words in the Connection_Path field (USINT)
        buf.put((byte) 2);
        // Reserved
        buf.put((byte) 0);
        // Connection Path - Indicates the route to the Remote Target Device.
        buf.putInt(0x01240220);  // 20 02 24 01
    }
}
