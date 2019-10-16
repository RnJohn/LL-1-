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
public class Utils {
    
    public static Nonterminal getNonTerminal (String symbol, ArrayList<Nonterminal> nonterminalArray){
        for (Nonterminal nonterminal: nonterminalArray){
            if (nonterminal.getSymbol().equals(symbol)){
                return nonterminal;
            }
        }
        return null;
    }
    
    
    public static void checkNonTerminalSymbols(File file, ArrayList<Nonterminal> nonterminalTempArray) throws IOException{
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null){
                Nonterminal nonterminal;
                String split[] = line.split("->");
                if ((nonterminal = Utils.getNonTerminal(split[0], nonterminalTempArray)) == null){
                    nonterminal = new Nonterminal(split[0]);
                    nonterminal.addProduction(split[1]);
                    nonterminalTempArray.add(nonterminal);
                }else{
                    nonterminal.addProduction(split[1]);
                }
                line = reader.readLine();
            }
            reader.close();
            for (Nonterminal nonterminal: nonterminalTempArray){
                System.out.println("NONTERMINAL: "+nonterminal.getSymbol());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LL1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void checkTerminalSymbols(File file,ArrayList<Nonterminal> nonterminalTempArray, ArrayList<String> terminalArray){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null){
                String split[] = line.split("->");
                String split2[] = split[1].split("");
                for (int i=0; i< split2.length; i++){
                    Nonterminal nonterminal;
                    if ((nonterminal = Utils.getNonTerminal(split2[i], nonterminalTempArray)) == null){
                        terminalArray.add(split2[i]);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
            for (String terminal: terminalArray){
                System.out.println("TERMINAL: "+terminal);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }

    }
    
    
    public static void checkRecursion(Nonterminal nonterminal, ArrayList<Nonterminal> nonterminalArray){
        boolean check = false;
        for (String production: nonterminal.getProductions()){
            String split[] = production.split("");
            if(split[0].equals(nonterminal.getSymbol())){
                check = true;
            }
        }
        if (check){
            Nonterminal standard = new Nonterminal(nonterminal.getSymbol());
            Nonterminal prime = new Nonterminal(nonterminal.getSymbol()+"'");
            for (String production: nonterminal.getProductions()){
                String split[] = production.split("");
                if(split[0].equals(nonterminal.getSymbol())){
                    String temp[] = production.split(nonterminal.getSymbol());
                    prime.addProduction(temp[1]+prime.getSymbol());
                    prime.addProduction("&");
                }else{
                    standard.addProduction(production+prime.getSymbol());
                }
            }
            nonterminalArray.add(standard);
            nonterminalArray.add(prime);
        }else{
            checkFactorization(nonterminal,nonterminalArray);
        }
        
    }
    
    
    public static void checkFactorization(Nonterminal nonterminal, ArrayList<Nonterminal> nonterminalArray){
    
    }
    
    
}
