package Blockchain.CryptoBalami;

import Blockchain.Core.DigitalFingerPrintGenerator;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    // this is also the hash of the transactions
    public String transactionId;
    // Public Key of sender
    public PublicKey sender;
    // Recipients address/public key.
    public PublicKey reciepient;
    public float value;
    // this is to prevent anybody else from spending funds in our wallet.
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    // a rough count of how many transactions have been generated.
    private static int sequence = 0;

    // Constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private String calulateHash() {
        //increase the sequence to avoid 2 identical transactions having the same hash
        sequence++;
        return DigitalFingerPrintGenerator.applySHA256(
                DigitalFingerPrintGenerator.getStringFromKey(sender) +
                        DigitalFingerPrintGenerator.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence );
    }

    //Signs all the data we dont wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = DigitalFingerPrintGenerator.getStringFromKey(sender) + DigitalFingerPrintGenerator.getStringFromKey(reciepient) + Float.toString(value);
        signature = DigitalFingerPrintGenerator.applyECDSASig(privateKey,data);
    }
    //Verifies the data we signed hasnt been tampered with
    public boolean verifiySignature() {
        String data = DigitalFingerPrintGenerator.getStringFromKey(sender) + DigitalFingerPrintGenerator.getStringFromKey(reciepient) + Float.toString(value);
        return DigitalFingerPrintGenerator.verifyECDSASig(sender, data, signature);
    }
}
