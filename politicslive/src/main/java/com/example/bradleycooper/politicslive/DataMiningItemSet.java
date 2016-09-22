package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 4/30/2016.
 */
public class DataMiningItemSet {

    private String itemSetName;
    private int support;

    public DataMiningItemSet(){
        support = 0;
    }
    public String getItemSetName() {
        return itemSetName;
    }
    public void setItemSetName(String itemSetName) {
        this.itemSetName = itemSetName;
    }
    public int getSupport() {
        return support;
    }
    public void setSupport(int support) {
        this.support = support;
    }
}
