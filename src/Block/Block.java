package Block;

import java.util.Date;

public class Block {
    private String hash;
    private String previousBlockHash;
    private String information;
    private long timeStamp;

    public Block(String data, String previousBlockHash ) {
        this.information = data;
        this.previousBlockHash = previousBlockHash;
        this.timeStamp = new Date().getTime();
    }
}

