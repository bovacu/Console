package customConsole;

import addOns.MathCommand;

public class Main {
    public static void main(String args[]){
        Console.generateGraphicWindow();
        Console.addConsoleCommand(new MathCommand("jmath "));
        Console.addConsoleCommandKeywords();
    }
}
