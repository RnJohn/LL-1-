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
                    if ((nonterminal = Utils.getNonTerminal(split2[i], nonterminalTempArray)) == null && !terminalArray.contains(split2[i])){
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
            ArrayList<String> alpha = new ArrayList();
            ArrayList<String> beta = new ArrayList();
            for (String production: nonterminal.getProductions()){
                String split[] = production.split("");
                if(split[0].equals(nonterminal.getSymbol())){
                    String temp[] = production.split(nonterminal.getSymbol());
                    alpha.add(temp[1]+prime.getSymbol());
                }else{
                    beta.add(production+prime.getSymbol());
                }
            }
            alpha.add("&");
            standard.addProductions(beta);
            prime.addProductions(alpha);
            nonterminalArray.add(standard);
            nonterminalArray.add(prime);
        }else{
            checkFactorization(nonterminal, nonterminalArray);
        }
        
    }
    
    
    public static void checkFactorization(Nonterminal nonterminal, ArrayList<Nonterminal> nonterminalArray){
        boolean check = false;
        for (String production: nonterminal.getProductions()){
            String common[] = production.split("");
            for (String production2: nonterminal.getProductions()){
                String test[] = production2.split("");
                if (common[0].equals(test[0]) && !production.equals(production2)){
                    check = true;
                }
            }
        }
        if (check){
            Nonterminal standard = new Nonterminal(nonterminal.getSymbol());
            Nonterminal prime = new Nonterminal(nonterminal.getSymbol()+"'");
            String match = "";
            for (String production: nonterminal.getProductions()){
                for (String production2: nonterminal.getProductions()){
                    String match2;
                    if (!production.equals(production2)){
                        match2 = commonFinder(production,production2);
                        if (match.equals("")){
                            match = match2;
                        }else if (match2.length() < match.length() && !match2.equals("")){
                            match = match2;
                        }
                    }
                }
            }
            standard.addProduction(match+prime.getSymbol());
            for (String production: nonterminal.getProductions()){
                String[] split = production.split(match);
                if (split.length==0){
                    prime.addProduction("&");
                }else if(!split[0].equals("") && split[0].length() == production.length()){
                    standard.addProduction(production);
                }else{
                    prime.addProduction(split[1]);
                }
            }
            nonterminalArray.add(standard);
            nonterminalArray.add(prime);
        }else{
            nonterminalArray.add(nonterminal);
        }
    }
    
    
    
    
    public static String commonFinder(String string1, String string2){
    String answer = "";
    int len1 = string1.length();
    int len2 = string2.length();
    for (int i = 0; i < len1; i++) {
        for (int j = 0; j < len2; j++) {
            int temp = 0;
            String match = "";
            while ((i+temp<len1) && (j+temp<len2) && string1.substring(0, i+temp+1).equals(string2.substring(0, j+temp+1))){
                match=match+string2.substring(j+temp, j+temp+1);
                temp++;
            }
            if (match.length() > answer.length()){
                answer = match;
            }
        }
    }
    return answer;
    }
    
    
    
    
    
    public static void removeRecursionAndFactorization(ArrayList<Nonterminal> nonterminalTempArrayList, ArrayList<Nonterminal> nonterminalArray){
        for (Nonterminal nonterminal: nonterminalTempArrayList){
            checkRecursion(nonterminal, nonterminalArray);
        }
    }
    
    
}
