package customConsole;

public class Main {
    public static void main(String args[]){
        Console.generateGraphicWindow(true);
        Console.printFailure("This piece of code failed 12");
        Console.printSuccess("This piece of code worked correctly");
        Console.printWarning("This piece of code can create problems 12");
    }
}
