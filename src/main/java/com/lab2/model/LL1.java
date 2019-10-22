/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab2.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jefop
 */
public class LL1 {
    private ArrayList<Nonterminal> nonterminalTempArray = new ArrayList();;
    private ArrayList<Nonterminal> nonterminalArray = new ArrayList();;
    private ArrayList<String> terminalArray = new ArrayList();;

    public LL1(File file) throws IOException {
        Utils.checkNonTerminalSymbols(file, nonterminalTempArray);
        Utils.checkTerminalSymbols(file, nonterminalTempArray, terminalArray);
        Utils.removeRecursionAndFactorization(nonterminalTempArray, nonterminalArray);
        
        for (Nonterminal nonterminal: nonterminalArray){
            for (String production : nonterminal.getProductions()){
                System.out.println(nonterminal.getSymbol()+" -> "+production);
            }
        }
    }
    
    

    
    
}
