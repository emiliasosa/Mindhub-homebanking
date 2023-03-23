package com.mindhub.homebanking.utils;

public final class CardUtils {

    public static int getCvv(){
        int finalNumber;
        do{
            finalNumber = (int)(Math.random()*(999 - 100)+100);
        }while(finalNumber < 100);
        return finalNumber;
    }
}
