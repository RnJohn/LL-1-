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
    
    public DefaultTableModel recognize(String s){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Pila");
        model.addColumn("Entrada");
        model.addColumn("Salida");
        String stack = "$"+nonterminalArray.get(0).getSymbol();
        String entry = s+"$";
        boolean accept = true;
        String firstEntry = entry.substring(0,1);
        String production = nonterminalArray.get(0).getHash(firstEntry);
        while (accept){
            firstEntry = entry.substring(0,1);
            String check = stack.substring(stack.length()-1,stack.length());
            if (check.equals("'")){
                check = stack.substring(stack.length()-2,stack.length()-1)+check;
            }
            Nonterminal checkNon;
            if ((checkNon = Utils.getNonTerminal(check, nonterminalArray))!=null){ //if the rightmost symbol in the stack is a nonterminal
                production = checkNon.getHash(firstEntry);
                if (accept){
                    if(production == null){
                        model.addRow(new Object[] {stack,entry,"No reconoce"});
                    }else{
                        model.addRow(new Object[] {stack,entry,check+"->"+production});
                    }
                    
                }else{
                    model.addRow(new Object[] {stack,entry,"No reconoce"});
                }
                
                if (production == null){
                    accept = false;
                }else{
                    if(check.length()==1){
                        stack = stack.substring(0,stack.length()-1);
                    }else{
                        stack = stack.substring(0,stack.length()-2);
                    }
                    if(!production.equals("&")){
                        stack = transfer(stack,production);
                    }
                }

            }else{ //If the rightmost symbol in the stack is a terminal
                
                if(accept){
                    model.addRow(new Object[] {stack,entry});
                }else{
                    model.addRow(new Object[] {stack,entry,"No reconoce"});
                }
                
                
                if (firstEntry.equals(check)){
                    entry = entry.substring(1,entry.length());
                    stack = stack.substring(0,stack.length()-1);
                }else{
                    accept = false;
                }
                
            }
            if (stack.equals("$") && entry.equals("$")){
                model.addRow(new Object[] {"$","$","Acepta"});
                accept = false;
            }
        }
        
        
        
        
        return model;
    }
    
    
    public String transfer(String s1, String s2){
        String copy = s2;
        String answer = s1;
        while(!copy.equals("")){
            String sub = copy.substring(copy.length()-1,copy.length());
            if (sub.equals("'")){
                sub = copy.substring(copy.length()-2,copy.length());
                copy = copy.substring(0,copy.length()-2);
            }else{
                copy = copy.substring(0,copy.length()-1);
            }
            answer = answer+sub;
        }
        return answer;
    }
    
    
}
