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

    public Nonterminal(String symbol) {
        this.symbol = symbol;
        productions = new ArrayList();
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
    
    
    
}

