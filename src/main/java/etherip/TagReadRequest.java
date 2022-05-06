package etherip;

/**
 * Request to read a tag from the PLC
 */
public class TagReadRequest {

    private Integer sensorId;
    private String tag;

    public TagReadRequest(Integer sensorId, String tag) {
        this.sensorId = sensorId;
        this.tag = tag;
    }

    public Integer getSensorId () {
        return sensorId;
    }

    public String getTag() {
        return tag;
    }
}
