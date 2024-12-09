package Blockchain.CryptoBalami;

import Blockchain.Core.BalamiChainMain;
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

    //Signs all the data we don't wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = DigitalFingerPrintGenerator.getStringFromKey(sender) + DigitalFingerPrintGenerator.getStringFromKey(reciepient) + Float.toString(value);
        signature = DigitalFingerPrintGenerator.applyECDSASig(privateKey,data);
    }
    //Verifies the data we signed haven't been tampered with
    public boolean verifiySignature() {
        String data = DigitalFingerPrintGenerator.getStringFromKey(sender) + DigitalFingerPrintGenerator.getStringFromKey(reciepient) + Float.toString(value);
        return DigitalFingerPrintGenerator.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransactions() {

        if(!verifiySignature()) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : inputs) {
            i.UTXO = BalamiChainMain.UTXOs.get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if(getInputsValue() < BalamiChainMain.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            BalamiChainMain.UTXOs.put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            BalamiChainMain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }
    //returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            total += i.UTXO.value;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }


}
