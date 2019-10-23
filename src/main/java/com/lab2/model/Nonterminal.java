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
    private ArrayList<String> follow;

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
        this.first.add(first);
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
        this.follow.add(follow);
    }
    
}

