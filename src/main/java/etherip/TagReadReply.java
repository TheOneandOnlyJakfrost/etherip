package etherip;

import etherip.types.CIPData;

/**
 * Response after requesting reading a tag in a PLC.
 */
public class TagReadReply {

    private String tag;
    private int status;
    private CIPData data;

    public TagReadReply(String tag, int status, CIPData data) {
        this.tag = tag;
        this.status = status;
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public int getStatus(){
        return status;
    }

    public CIPData getData() {
        return data;
    }

    /**
     * Returns whether the service was successfully performed or not. For a description of all
     * the codes see CIP Volume 1, Table B-1.1 CIP General Status Codes (page 1222)
     */
    public boolean isValid() {
        return status == 0;
    }



}
