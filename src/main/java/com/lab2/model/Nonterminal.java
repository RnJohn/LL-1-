/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab2.model;

import java.util.ArrayList;

/**
 *
 * @author jefop
 */
public class Nonterminal {
    private String symbol;
    private ArrayList<String> productions;
    public ArrayList<String> first;
    public ArrayList<String> follow;

    public Nonterminal(String symbol) {
        this.symbol = symbol;
        productions = new ArrayList();
        first = new ArrayList();
        follow = new ArrayList();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ArrayList<String> getProductions() {
        return productions;
    }

    public void addProduction(String production) {
        this.productions.add(production);
    }
    
    public void addProductions(ArrayList<String> productions){
        this.productions.addAll(productions);
    }
    
    public void addFirst(String first){
        if (!this.first.contains(first)){
            this.first.add(first);
        }  
    }
    
    public void addFirstArray(ArrayList<String> firstArray){
        for (String first: firstArray){
            if (!this.first.contains(first)){
                this.first.add(first);
            }
        }
    }
    
    public ArrayList<String> getFirst(){
        return this.first;
    }
            
    public void addFollow(String follow){
        if (!this.follow.contains(follow)){
            this.follow.add(follow);
        }
    }
    
    public void addFollowArray(ArrayList<String> followArray){
        for (String follow: followArray){
            if(!this.follow.contains(follow) && !follow.equals("&")){
                this.follow.add(follow);
            }
        }
    }
    
    public ArrayList<String> getFollow(){
        return this.follow;
    }
    
    
    public void removeFollow(String follow){
        int index = -1;
        for (int i = 0; i < this.follow.size(); i++) {
            if (this.follow.get(i).equals(follow)){
                index = i;
            }
        }
        this.follow.remove(index);
    }
    
}

