package Blockchain.Core;

import Blockchain.CryptoBalami.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    public String hash;
    public String previousBlockHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
    private String information;
    private long timeStamp;

    private int nonce;

    public Block(String previousHash ) {
        this.previousBlockHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        return DigitalFingerPrintGenerator.applySHA256(previousBlockHash + Long.toString(timeStamp)+ Integer.toString(nonce) + merkleRoot);
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

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((!Objects.equals(previousBlockHash, "0"))) {
            if((!transaction.processTransactions())) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}

