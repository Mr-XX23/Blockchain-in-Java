package Blockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class BlockchainMain {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        blockchain.add(new Block("Hi, I am first block", "0"));
        blockchain.add(new Block("Hi, I am second block", blockchain.get(blockchain.size()-1).hash));
        blockchain.add(new Block("Hi, I am third block", blockchain.get(blockchain.size()-1).hash));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }
}

