package org.zad6;


import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a function: ");
        String function = scanner.nextLine();

        new SAAnimation(function);
        new PSOAnimation(function);
    }
}
