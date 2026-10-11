package domain;

public class SnowflakeIdGenerator {
    private long nodeID;
    private long sequence = 0;
    private long lastTimestamp = System.currentTimeMillis();

    public SnowflakeIdGenerator(long nodeID) {
        this.nodeID = nodeID;
    }

    public synchronized long generateID(){
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp){
            throw new IllegalStateException("Годинник на комп'ютері збився");
        }

        if (currentTimestamp == lastTimestamp){
            sequence += 1;
        }

        if (currentTimestamp > lastTimestamp){
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return currentTimestamp | sequence | lastTimestamp;
    }
}
