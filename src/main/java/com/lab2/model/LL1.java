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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

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
            Utils.first(nonterminal, nonterminalArray);
        }
        Utils.follow(nonterminalArray);
        
        for (Nonterminal nonterminal: nonterminalArray){
            for (String production : nonterminal.getProductions()){
                System.out.println(nonterminal.getSymbol()+" -> "+production);
            }
//            System.out.println("SYMBOL: "+nonterminal.getSymbol());
//            for (String first: nonterminal.getFirst()){
//                System.out.println(first);
//            }
        }
//        
//        for (Nonterminal nonterminal: nonterminalArray){
//            System.out.println("NONTERMINAL ==> "+nonterminal.getSymbol());
//            for (String follow: nonterminal.getFollow()){
//                System.out.println(follow);
//            }
//        }
        
        Utils.cleanHash(nonterminalArray);
        
        
    }
    
    

    public ArrayList<Nonterminal> obtainGrammar(){
        return nonterminalArray;
    }
    
    public DefaultTableModel MTableModel(){
        DefaultTableModel model = new DefaultTableModel();
        terminalArray.add("$");
        model.addColumn("NT/T");
        for(String terminal: terminalArray){
            model.addColumn(terminal);
        }
        model.setRowCount(nonterminalArray.size());
        int index = 0;
        for (Nonterminal non: nonterminalArray){
            model.setValueAt(non.getSymbol(),index,0);
            for(Map.Entry<String,String> map: non.getHashMap().entrySet()){
                model.setValueAt(non.getSymbol()+"->"+map.getValue(),index,terminalArray.indexOf(map.getKey())+1);
            }
            index++;
        }
        return model;
    }
    
    
}
