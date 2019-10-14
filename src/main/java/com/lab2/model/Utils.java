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
public class Utils {
    
    public static Nonterminal getNonTerminal (String symbol, ArrayList<Nonterminal> nonterminalArray){
        for (Nonterminal nonterminal: nonterminalArray){
            if (nonterminal.getSymbol().equals(symbol)){
                return nonterminal;
            }
        }
        return null;
    }
    
}
