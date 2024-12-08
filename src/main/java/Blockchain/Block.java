package Blockchain;

import java.util.Date;

public class Block {
    public String hash;
    public String previousBlockHash;
    private String information;
    private long timeStamp;

    public Block(String data, String previousBlockHash ) {
        this.information = data;
        this.previousBlockHash = previousBlockHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        return DigitalFingerPrintGenerator.applySHA256(previousBlockHash + Long.toString(timeStamp) + information);
    }
}

