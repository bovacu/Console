# Console
A more visual java console for debugging


![cap1](https://user-images.githubusercontent.com/36163709/43843208-4ee313ea-9b27-11e8-84ad-28c8d34f01e4.png)

# Installation
Just import the Console.jar to your project and make sure to add the folder consoleAssets to your project too, at the same level as src folder.

# How to use it
You can use this console integrated to your IDE console (having less features) or instantiate a custom window (recommended option).
To instantiate the window, before trying to write anything with this library you have to write this line:
    
    Console.generateGraphicWindow(true);

Use true or false dependes on an option that I'll explaint later.
Once you have written this piece of code, you can choose between a bunch of printing options. Not all of them work now. Some working are:

    printFailure(String failure)
    printSuccess(String success)
    printWarning(String warning)

They write the message in the parameter in a special way. They must be called staticly.

    Console.printFailure("This piece of code failed");
    Console.printSuccess("This piece of code worked correctly");
    Console.printWarning("This piece of code can create problems");
    
And we will have an output like this:

![cap2](https://user-images.githubusercontent.com/36163709/43843977-fda14568-9b28-11e8-95d9-fceef6e88e04.png)

The other implemeted for the moment are:

    printLine(ConsoleColors text, String message)
    printLineBold(ConsoleColors text, String message)
    printLineWithBackground(ConsoleColors text, ConsoleColors background, String message)
    printLineWithBackgroundAndBold(ConsoleColors text, ConsoleColors background, String message)

The ConsoleColors is an Enum included in the jar file, it contains the names of the available colors for the text and it's backgrounds.

# True or false when creating the window
The true or false means if you want to activate the stack message feature. This feature stacks equal messages and counts the times that it appears. If a message appears many times in different lines, it is stacked, but the lines where it appears are written in the location column.

The stack only works for messages inside the same class, not outside.

# Commands
The console also supports commands. 3 for the moment, but more can be added. The 3 existing commands are:

    -info
    -search M something_to_search
    -search L something_to_search

Info gives all the available commands and what they do. search M something_to_search looks for matches in the messages column and if it finds them, the matches get orange background. search L is the same, but on the Location column

Commands MUST be written as they are up inside the box, like this:

![cap3](https://user-images.githubusercontent.com/36163709/43844632-7dfcf1de-9b2a-11e8-9593-7ff62fffb8ce.png)

Finally, a full usage example
    
    public class Main {
        public static void main(String args[]){
            Console.generateGraphicWindow(true);
            Console.printFailure("This piece of code failed 12");
            Console.printSuccess("This piece of code worked correctly");
            Console.printWarning("This piece of code can create problems 12");
        }
    }
