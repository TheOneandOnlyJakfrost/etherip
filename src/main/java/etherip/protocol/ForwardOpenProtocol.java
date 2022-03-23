package etherip.protocol;

import etherip.types.CNService;
import etherip.util.CIPConstants;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Logger;

import static etherip.types.CNPath.ConnectionManager;
import static etherip.types.CNService.CM_ForwardOpen;

/**
 * Protocol PDU that uses {@link CNService#CM_ForwardOpen}. The Forward Open Service (Service Code = 54 hex ) and
 * Large_Forward_Open service (Service Code = 5B hex ) are used to establish a Connection with a Target Device 1 . These
 * services result in local connection establishment on each link along the path. Opens a connection, maximum data size
 * is 511 bytes
 *
 * CIP Vol 1, 3-5.5.2, page 104
 */
public class ForwardOpenProtocol extends ProtocolAdapter {

    private static final Logger logger = Logger.getLogger(ForwardOpenProtocol.class.getSimpleName());

    private final ProtocolEncoder encoder;
    private final Random random = new Random();
    private int networkConnectionId;
    private short connectionSerialNumber;

    public ForwardOpenProtocol() {
        this.encoder = new MessageRouterProtocol(CM_ForwardOpen, ConnectionManager(), new ProtocolAdapter());
    }

    @Override
    public int getRequestSize() {
        int result = this.encoder.getRequestSize() + 40; // 40 bytes of data are encoded here
        return result;
    }

    @Override
    public void encode(ByteBuffer buf, StringBuilder log) throws Exception {
        this.encoder.encode(buf, log);
        buf.put((byte) 10); // Tick time
        buf.put((byte) 5); // Time-out ticks
        // O->T connection ID
        buf.putInt(0);
        // T->O Network connection id, create a random int and store it
        networkConnectionId = random.nextInt();
        buf.putInt(networkConnectionId);
        // Connection serial number (UINT)
        // The connection serial number shall be a unique 16-bit value selected by the connection manager at the
        // originator of the connection.
        connectionSerialNumber = (short) random.nextInt(1 << 16); // any short
        buf.putShort(connectionSerialNumber);
        // Originator Vendor ID - UINT Vendor ID of the originating node.
        buf.putShort(CIPConstants.ORIGINATOR_VENDOR_ID);
        // Originator Serial Number UDINT Serial Number of the originating node.
        buf.putInt(CIPConstants.ORIGINATOR_SERIAL_NUMBER);
        // Connection Timeout Multiplier USINT See Object Specific Service Parameters a
        buf.put((byte) 1);
        // 3 reserved bytes
        buf.put(new byte[] {0, 0, 0});
        // O->T RPI Originator to Target requested packet rate, in microseconds (UDINT).
        buf.putInt(0x000F4240); // 1000 microseconds
        /**
         * For a better understanding of the Network Parameters, see CIP Vol 1 3-5.5.1.1 Network Connection Parameters
         * page 94
         * O->T Network Connection Parameters
         * Owner: Exclusive (1)
         * Connection Type: Point to Point (2)
         * Priority: Low Priority (0)
         * Connection Size Type: Variable (1)
         * Connection Size: 500 bytes
         */
        buf.putShort((short) 0x43F4);
        // T->O RPI Target to Originator requested packet rate, in microseconds (UDINT) same as above
        buf.putInt(0x000F4240); // 1000 microseconds
        // T->O Network Connection Parameters - same as above
        buf.putShort((short) 0x43F4);
        // Transport type / trigger (byte) 0xa3
        buf.put((byte)0xa3);
        // Connection_Path_Size - The number of 16 bit words in the Connection_Path field (USINT)
        buf.put((byte) 2);
        // Connection Path - Indicates the route to the Remote Target Device.
        buf.putInt(0x01240220);  // 20 02 24 01
    }

    @Override
    public void decode(ByteBuffer buf, int available, StringBuilder log) throws Exception {

    }

    /**
     * Returns the last network connection id that was used. If there's none, then 0 is returned
     */
    public int getNetworkConnectionId() {
        return this.networkConnectionId;
    }

    /**
     *
     * Returns the last connection serial number that was used. If there's none then 0 is returned.
     */
    public short getConnectionSerialNumber() {
        return connectionSerialNumber;
    }
}
