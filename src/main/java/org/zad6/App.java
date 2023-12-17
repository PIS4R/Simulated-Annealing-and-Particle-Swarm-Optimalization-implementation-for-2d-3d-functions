package org.zad6;


import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a function: ");
        String function = scanner.nextLine();

        //(1-x)^2 + 100(y - x^2)^2
        //-(y + 47) sin( sqrt(abs((x/2) + (y + 47) ))) - x sin( sqrt( abs( x - (y + 47) )) )

        new SAAnimation(function);
        new PSOAnimation(function);
    }
}
