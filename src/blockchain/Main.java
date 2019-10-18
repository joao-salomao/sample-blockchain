/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author João Salomão
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        
        for(int i = 0; i < 1000000; i++) {
            blockchain.createNewBlock();
        }
        if (blockchain.validateBlockChain()) {
            System.out.println("O BLOCK É VÁLIDO");
        } else {
            System.out.println("O BLOCK NÃO É VÁLIDO");
        }
    }
    
}

class Blockchain {
    ArrayList<Block> blockList;
    
    Blockchain() {
        this.blockList = new ArrayList<>();
    }
    
    public void createNewBlock() {
        
        Block block;
        String generatedString;
        String generatedHash;
        int id = this.blockList.size() + 1;
        long timeStamp = new Date().getTime(); // 1539795682545 represents 17.10.2018, 20:01:22.545 
        
        if (this.blockList.isEmpty()) {
            generatedString = this.generateRandomString();
            generatedHash = this.applySha256(generatedString);
            block = new Block(id, timeStamp, "", generatedHash);
        } else {
            int lastBlockIndex = this.blockList.size() - 1;
            String lastBlockHash = this.blockList.get(lastBlockIndex).getHash();
            generatedHash = this.applySha256(lastBlockHash);
            block = new Block(id, timeStamp, lastBlockHash, generatedHash);
        }
        this.blockList.add(block);
    }
    
    void soutBlockChain() {
        this.blockList.forEach((block) -> {
            // Get block informations;
            int id = block.getId();
            long timeStamp = block.getTimeStamp();
            String hash = block.getHash();
            String previousBlockHash = block.getPreviousBlockHash();
            
            System.out.println("ID: "+id+" DATETIME: "+timeStamp);
            System.out.println("HASH: "+hash+"     Previous Block Hash: "+previousBlockHash+"\n");
        });
    }
    
    boolean validateBlockChain() {
        for (int i = 0; i < this.blockList.size()-1; i++) {
            if (i == 0) {
                continue;
            }
            String currentBlockHash = this.blockList.get(i).getPreviousBlockHash();
            String previousBlockHash = this.blockList.get(i-1).getHash();
            
            if (!currentBlockHash.equalsIgnoreCase(previousBlockHash)) {
                return false;
            }
            
        }
        return true;
    }
    
    String generateRandomString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
    
    /* Applies Sha256 to a string and returns a hash. */
    public String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}

class Block {
    int id;
    long timeStamp;
    String previousBlockHash;
    String hash;
    
    Block(int id, long timeStamp, String previousBlockHash, String hash) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.previousBlockHash = previousBlockHash;
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
