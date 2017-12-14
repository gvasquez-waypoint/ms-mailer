package cl.waypoint.ms.mailer.dto;

import java.io.Serializable;
import java.util.Date;

public class BounceSummary implements Serializable {

    private static final long serialVersionUID = -7953962222825090525L;

    private Date last;
    private long count;
    private String reason;

    public BounceSummary(String reason, Date date) {
        last = date;
        count = 1;
        this.reason = reason;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
