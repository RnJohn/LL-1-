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
import java.util.HashMap;
import java.util.Map;
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
    

    public static void first(Nonterminal nonterminal, ArrayList<Nonterminal> nonterminalArray){
        for (String production: nonterminal.getProductions()){
            String firstSymbol = production.substring(0, 1);
            Nonterminal check;
            if ((check = Utils.getNonTerminal(firstSymbol, nonterminalArray)) == null){
                if (!nonterminal.first.contains(firstSymbol)){
                    nonterminal.addFirst(firstSymbol);
                    if(firstSymbol.equals("&")){
                        nonterminal.addHash(nonterminal.getSymbol(),production);
                    }else{
                        nonterminal.addHash(firstSymbol, production);
                    }
                    
                }
            }else{
                first(check, nonterminalArray);
                nonterminal.addFirstArray(check.getFirst());
                if (firstSymbol.equals("&")){
                    nonterminal.addHash(nonterminal.getSymbol(), "&");
                }else if (checkEpsilonFirst(check)){
                    nonterminal.addHash(firstSymbol, "&");
                }else{
                    for (String first: check.getFirst()){
                        nonterminal.addHash(first,production);
                    }
                }
                
            }
        }
    }
    
    
    public static void cleanHash(ArrayList<Nonterminal> nonterminalArray){
        for (Nonterminal non: nonterminalArray){
            HashMap<String,String> copy = new HashMap();
            Utils.copyHashValues(non.getHashMap(), copy);
            for (Map.Entry<String,String> entry: non.getHashMap().entrySet()){
                Nonterminal check;
                if ((check = Utils.getNonTerminal(entry.getKey(), nonterminalArray))!=null){
                    ArrayList<String> checkFollow = check.getFollow();
                    for (String follow: checkFollow){
                        copy.put(follow, "&");
                    }
                    copy.remove(entry.getKey(), "&");
                }
            }
            non.hash = new HashMap();
            non.hash = copy;
        }
        for (Nonterminal non: nonterminalArray){
            for (Map.Entry<String,String> entry: non.getHashMap().entrySet()){
                System.out.println("NONTERMINAL -> "+non.getSymbol()+" TERMINAL -> "+entry.getKey()+" PRODUCTION -> "+entry.getValue());
            }
        }
    }
    
    public static void copyHashValues(HashMap<String,String> hash1, HashMap<String,String> hash2){
        for (Map.Entry<String,String> map : hash1.entrySet()){
            hash2.put(map.getKey(), map.getValue());
        }
    }
    
    
    public static void follow(ArrayList<Nonterminal> nonterminalArray){
        boolean first = true;
        for (Nonterminal nonterminal: nonterminalArray){
            if(first){
                nonterminal.addFollow("$");
                first = false;
            }
            followNon(nonterminal, nonterminalArray);
        }
        addFollowSecondIteration(nonterminalArray);
        
    }
    
    public static void addFollowSecondIteration(ArrayList<Nonterminal> nonterminalArray){
        boolean check = true;
        while (check){
            check = false;
            for (Nonterminal nonterminal: nonterminalArray){
                ArrayList<String> followbackup = new ArrayList();
                boolean terminalCheck = true;
                for (String follow: nonterminal.getFollow()){
                    Nonterminal check2;
                    if ((check2 = Utils.getNonTerminal(follow, nonterminalArray))!=null){
                        check = true;
                        Nonterminal check3;
                        for (String follow2: check2.getFollow()){
                            if((check3 = Utils.getNonTerminal(follow2, nonterminalArray))!=null){
                                terminalCheck = false;
                            }else{
                                followbackup.add(follow2);
                            }
                        }
                    }else{
                        followbackup.add(follow);
                    }
                }
                if(terminalCheck){
                    nonterminal.follow = new ArrayList();
                    nonterminal.addFollowArray(followbackup);
                
                }

            }
        }
    }
    
    public static void followNon(Nonterminal nonterminal, ArrayList<Nonterminal> nonterminalArray){
        for (Nonterminal non: nonterminalArray){
            for (String str: non.getProductions()){
                if (str.contains(nonterminal.getSymbol())){
                    int index = str.indexOf(nonterminal.getSymbol()); //Take the index of the position where the nonterminal is in the position
                    if (nonterminal.getSymbol().contains("'") && str.substring(index+1,index+2).equals("'")){ //If it's a prime
                        if (index+1 == str.length()-1 && !nonterminal.getSymbol().equals(non.getSymbol())){ //If it's at the end of the production and it is not itself
                            nonterminal.addFollow(non.getSymbol());
                        }
                    }else{ //If it's not a prime
                        if (index == str.length()-1){ //If the symbol is at the end of the production
                            if (non.getSymbol().length()==1){
                                nonterminal.addFollow(non.getSymbol());
                            }
                        }else{ //If it's not at the end of the production
                            String checkString = str.substring(index+1,index+2); //the symbol next to the evaluated
                            if (index+2 != str.length()){
                                if (str.substring(index+2,index+3).equals("'")){
                                    checkString = checkString+"'";
                                }
                            }
                            Nonterminal check;
                            if ((check = Utils.getNonTerminal(checkString, nonterminalArray))==null){ //if the symbol next to the evaluated is a terminal
                                if (!checkString.equals("'")){
                                    nonterminal.addFollow(checkString);
                                }
                            }else{
                                nonterminal.addFollowArray(check.getFirst());
                                boolean eps = checkEpsilonProduction(check);
                                if (eps){
                                    nonterminal.addFollow(checkString);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static boolean checkEpsilonProduction(Nonterminal nonterminal){
        for (String production: nonterminal.getProductions()){
            if (production.equals("&")){
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkEpsilonFirst(Nonterminal nonterminal){
        for (String first: nonterminal.getFirst()){
            if (first.equals("&")){
                return true;
            }
        }
        return false;
    }
    
    public static void removeRecursionAndFactorization(ArrayList<Nonterminal> nonterminalTempArrayList, ArrayList<Nonterminal> nonterminalArray){
        for (Nonterminal nonterminal: nonterminalTempArrayList){
            checkRecursion(nonterminal, nonterminalArray);
        }
    }
    
    
}
