package Blockchain.Core;

import java.util.Date;

public class Block {
    public String hash;
    public String previousBlockHash;
    private String information;
    private long timeStamp;

    private int nonce;

    public Block(String data, String previousBlockHash ) {
        this.information = data;
        this.previousBlockHash = previousBlockHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        return DigitalFingerPrintGenerator.applySHA256(previousBlockHash + Long.toString(timeStamp)+ Integer.toString(nonce) + information);
    }

    public void mineBlock(int difficulty) {
        // Create a string with difficulty * "0"
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0 ,difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Blocked Mine !!! : " + hash);
    }
}

