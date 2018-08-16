package addOns;

import customConsole.Console;
import customConsole.ConsoleColors;
import customConsole.ConsoleCommands;

import java.util.*;

public class MathCommand implements ConsoleCommands {

    private String header;
    private Map<String, Double> variables;
    private final String operations[] = {"var", "a(", "m(", "d(", "defined"};

    public MathCommand(String header){
        this.header = header;
        this.variables = new HashMap<>();
    }

    @Override
    public void exec(String command) {
        String parameters = command.split("jmath\\s+")[1];
        // jmath a(2 : defined integral trap -1 1 a(m(3 : x :x : x) : m(-1 : x : x) : x : -1))
        String firstOp = this.firstOperationAppear(parameters);
        switch (firstOp){
            case "var" : parameters = parameters.split("var\\s+")[1];
                this.addVariables(parameters);
                break;
            case "defined" : Console.printLineBold(ConsoleColors.GREEN, "jmath: op = " + this.definedIntegral(parameters)
                    ,false, Optional.of("   "));
                break;
            case "a(" : Console.printLineBold(ConsoleColors.GREEN, "jmath: op = " + this.sum(parameters)
                    , false, Optional.of("   "));
                break;
            case "m(" : Console.printLineBold(ConsoleColors.GREEN, "jmath: op = " + this.multiply(parameters)
                    ,false, Optional.of("   "));
                break;
            case "d(" : Console.printLineBold(ConsoleColors.GREEN, "jmath: op = " + this.divition(parameters)
                    ,false, Optional.of("   "));
                break;
        }
        try {

        }catch (Exception e){
            Console.printFailure("jmath: bad syntax",true, Optional.of("   "));
        }
    }

    @Override
    public String getHeader() {
        return this.header;
    }

    @Override
    public List<String> getCommands() {
        List<String> commands = new ArrayList<String>() {{
            add("jmath");
            add("defined integral -method- -a- -b- (-n- only if trapIter) -equation-");
            add("var -var_name- -var_value, -var_name- -var_value-, ...");
            add("a( -value- : -value- : ...)");
            add("d( -value- : -value- : ...)");
            add("m( -value- : -value- : ...)");
        }};
        return commands;
    }

    @Override
    public List<String> getCommandsDescription() {
        List<String> commands = new ArrayList<String>() {{
            add("write always in first place to use this library");
            add("solves the definite integral -methods- {trap, trapIter}");
            add("saves values to a variable to use them later");
            add("adds all numbers inside parenthesis");
            add("divides all numbers inside parenthesis");
            add("multiplies all numbers inside parenthesis");
        }};
        return commands;
    }

    @Override
    public List<String> getKeyWords() {
        List<String> keywords = new ArrayList<String>() {{
            add("jmath");
            add("defined");
            add("PI");
            add("E");
            add("integral");
            add("var");
            add("a()");
            add("d()");
            add("m()");
            add("trap");
            add("trapIter");
        }};
        return keywords;
    }

    @Override
    public String getLibraryName() {
        return "JMath";
    }

    private String firstOperationAppear(String parameters){
        for(String s : parameters.split("\\s+")){
            for(String op : this.operations)
                if(s.trim().contains(op))
                    return op;
        }
        return null;
    }

    private boolean isValueOperation(String value){
        for(String s : this.operations){
            if(value.contains(s))
                return true;
        }

        return false;
    }

    private void addVariables(String parameters){
        if(parameters.contains(",")){
            for(String var : parameters.split(","))
                this.subAddVariable(var);
        }else
            this.subAddVariable(parameters);
    }

    private void subAddVariable(String var){
        String variableName = "";
        String variableValueString = "";
        double variableValue = 0;

        var = var.replaceAll("^\\s+", "");
        variableName = var.split("\\s+")[0];
        variableValueString = var.split("\\s+")[1];

        if(var.split("\\s+")[1].equals("PI") || var.split("\\s+")[1].equals("-PI")){
            variableValue = (var.split("\\s+")[1].contains("-")) ? -Math.PI : Math.PI;
        }else if(variableValueString.equals("e") || variableValueString.equals("-e")){
            variableValue = (var.split("\\s+")[1].contains("-")) ? -Math.E : Math.E;
        }else if(this.variables.containsKey(variableValueString.replaceAll("-", "")
                .replaceAll("\\)", ""))){
            int st[] = this.signAndTimes(variableValueString);
            if(variableValueString.contains(")"))
                variableValueString = variableValueString.replace(")", "");
                variableValue = st[0] * this.variables.get(variableValueString.substring(st[1]));
        }else if(isValueOperation(variableValueString)){
            String n = variableValueString;
            for(int i = variableValueString.length() - 1; i > 0; i--){
                if(variableValueString.charAt(i) != '('){
                    n = variableValueString.substring(0, i);
                }
            }

            if(variableValueString.contains("defined"))
                n = "defined";

            switch (n){
                case "a(" : variableValue = this.sum(var.split(variableName)[1]);
                    break;
                case "m(" : variableValue = this.multiply(var.split(variableName)[1]);
                    break;
                case "d(" : variableValue = this.divition(var.split(variableName)[1]);
                    break;
                case "defined" : variableValue = this.definedIntegral(var.split(variableName)[1].trim());
                    break;
            }
        }else{
            variableValue = Double.valueOf(variableValueString);
        }
        this.variables.put(variableName, variableValue);
        Console.printLineBold(ConsoleColors.GREEN, "jmath: added variable " + variableName + ": " + variableValue
                , false, Optional.of("  "));
    }

    private int[] signAndTimes(String variable){
        int st[] = new int[2];

        int sign = 1;
        int minusCount = 0;

        if(variable.contains("-")){
            for(int i = 0; i < variable.length(); i++){
                if(variable.charAt(i) == '-') {
                    sign *= -1;
                    minusCount++;
                }else
                    break;
            }
        }
        st[0] = sign;
        st[1] = minusCount;
        return st;
    }

    private int[] limitOperation(String operation){
        int init_end[] = new int[2];
        boolean initFound = false;

        for(int i = 0; i < operation.length(); i++){
            if(operation.charAt(i) == '(' && !initFound) {
                init_end[0] = i + 1;
                initFound = true;
            } else if(operation.charAt(i) == ')')
                init_end[1] = i;
        }

        return init_end;
    }

    private List<String> splitOperands(String operands){
        List<String> operandList = new ArrayList<>();

        String toAdd = "";
        int parenthesisDetected = 0;
        for(int i = 0; i < operands.length(); i++){
            if(operands.charAt(i) == '(')
                parenthesisDetected++;

            if(operands.charAt(i) == ')')
                parenthesisDetected--;

            if(parenthesisDetected == 0 && operands.charAt(i) == ':') {
                operandList.add(toAdd);
                toAdd = "";
            }else{
                toAdd += operands.charAt(i);
            }

            if(i + 1 == operands.length()){
                operandList.add(toAdd);
            }
        }

        return operandList;
    }

    private double sum(String parameters){
        double sum = 0;
        int init_end[] = limitOperation(parameters);

        if(parameters.contains("(") || parameters.contains(")"))
            parameters = parameters.substring(init_end[0], init_end[1]);

        if(!isValueOperation(parameters)){
            for(String s : parameters.split(":")){
                s = s.trim();

                if(this.variables.containsKey(s.replaceAll("-", "").replaceAll("\\)", ""))) {
                    int st[] = this.signAndTimes(s);
                    if(s.contains(")"))
                        s = s.replace(")", "");
                    sum += st[0] * this.variables.get(s.substring(st[1]));
                } else {
                    if(s.contains(")"))
                        s = s.replace(")", "");
                    sum += Double.valueOf(s);
                }
            }
        }else{
            // jmath var x defined integral trap -1 1 a(m(3 : x :x : x) : m(-1 : x : x) : x : -1)
            for(String s : splitOperands(parameters)) {
                if(s.trim().charAt(0) == 'd' && s.trim().charAt(1) == 'e') {
                    sum += this.definedIntegral(s);
                }else if(s.trim().charAt(0) == 'm')
                    sum += this.multiply(s);
                else if(s.trim().charAt(0) == 'd')
                    sum += this.divition(s);
                else if(s.trim().charAt(0) == 'a')
                    sum += this.sum(s);
                else
                    sum += this.sum(s);
            }
        }

        return sum;
    }

    private double multiply(String parameters){
        double mul = 1;
        int init_end[] = limitOperation(parameters);

        if(parameters.contains("(") || parameters.contains(")"))
            parameters = parameters.substring(init_end[0], init_end[1]);

        if (!isValueOperation(parameters)){
            for(String s : parameters.split(":")){
                s = s.trim();

                if(this.variables.containsKey(s.replaceAll("-", "").replaceAll("\\)", ""))) {
                    int st[] = this.signAndTimes(s);
                    if(s.contains(")"))
                        s = s.replace(")", "");
                    mul *= st[0] * this.variables.get(s.substring(st[1]));
                } else {
                    if(s.contains(")"))
                        s = s.replace(")", "");

                    mul *= Double.valueOf(s);
                }
            }
        }else{
            for(String s : splitOperands(parameters)) {
                if(s.trim().charAt(0) == 'm')
                    mul *= this.multiply(s);
                else if(s.trim().charAt(0) == 'd')
                    mul *= this.divition(s);
                else if(s.trim().charAt(0) == 'a')
                    mul *= this.sum(s);
                else
                    mul *= this.multiply(s);
            }
        }

        return mul;
    }

    private double divition(String parameters){
        double div = 1;
        boolean first = true;
        int init_end[] = limitOperation(parameters);

        if(parameters.contains("(") || parameters.contains(")"))
            parameters = parameters.substring(init_end[0], init_end[1]);

        if(!isValueOperation(parameters)){
            for(String s : parameters.split(":")){
                s = s.trim();

                if(this.variables.containsKey(s.replaceAll("-", "").replaceAll("\\)", ""))) {
                    int st[] = this.signAndTimes(s);
                    if(s.contains(")"))
                        s = s.replace(")", "");
                    if(first){
                        first = false;
                        div = st[0] * this.variables.get(s.substring(st[1]));
                    }else{
                        div /= st[0] * this.variables.get(s.substring(st[1]));
                    }
                } else {
                    if(s.contains(")"))
                        s = s.replace(")", "");

                    if(first){
                        first = false;
                        div = Double.valueOf(s);
                    }else{
                        div /= Double.valueOf(s);
                    }
                }
            }
        }else{
            for(String s : splitOperands(parameters)) {
                if(s.trim().charAt(0) == 'm'){
                    if(first){
                        div = this.multiply(s);
                        first = false;
                    }else
                        div /= this.multiply(s);
                }else if(s.trim().charAt(0) == 'd'){
                    if(first){
                        div = this.divition(s);
                        first = false;
                    }else
                        div /= this.divition(s);
                }else if(s.trim().charAt(0) == 'a'){
                    if(first){
                        div = this.sum(s);
                        first = false;
                    }else
                        div /= this.sum(s);
                }else{
                    if(first){
                        div = this.divition(s);
                        first = false;
                    }else
                        div /= this.divition(s);
                }
            }
        }

        return div;
    }

    private double definedIntegral(String parameters){
        double area = 0;
        parameters = parameters.split("defined integral")[1];
        String method = parameters.trim().split("\\s+")[0];

        if(method.trim().equals("trap"))
            return this.trapezoidMethod(parameters.split("trap")[1]);
        else if(method.trim().equals("trapIter"))
            return this.trapezoidIterationMethod(parameters.split("trapIter")[1]);
        else
            return 0;
    }

    private double trapezoidMethod(String parameters){
        String a = "";
        String b = "";
        String equation = "";

        a = parameters.split("\\s+")[1].trim();
        b = parameters.split("\\s+")[2].trim();

        for(int i = 3; i < parameters.split("\\s+").length; i++){
            equation += parameters.split("\\s+")[i];
        }

        double firstNumber = (Double.valueOf(b) - Double.valueOf(a));
        String firstFuncSubstitution = equation.replaceAll("x", a);

        double secondNumber = 0;

        if(firstFuncSubstitution.charAt(0) == 'a')
            secondNumber += this.sum(firstFuncSubstitution);
        else if(firstFuncSubstitution.charAt(0) == 'm')
            secondNumber += this.multiply(firstFuncSubstitution);
        else if(firstFuncSubstitution.charAt(0) == 'd')
            secondNumber += this.divition(firstFuncSubstitution);

        String secondFuncSubstitution = equation.replaceAll("x", b);
        double thirdNumber = 0;

        if(secondFuncSubstitution.charAt(0) == 'a')
            thirdNumber += this.sum(secondFuncSubstitution);
        else if(secondFuncSubstitution.charAt(0) == 'm')
            thirdNumber += this.multiply(secondFuncSubstitution);
        else if(secondFuncSubstitution.charAt(0) == 'd')
            thirdNumber += this.divition(secondFuncSubstitution);

        double sum = secondNumber + thirdNumber;
        return firstNumber * (this.divition("d(" + sum + " : 2)"));
    }

    private double trapezoidIterationMethod(String parameters){
        String a = "";
        String b = "";
        String equation = "";
        String iters = "";

        a = parameters.split("\\s+")[1].trim();
        b = parameters.split("\\s+")[2].trim();
        iters = parameters.split("\\s+")[3].trim();

        for(int i = 4; i < parameters.split("\\s+").length; i++){
            equation += parameters.split("\\s+")[i];
        }

        double firstNumber = (Double.valueOf(b) - Double.valueOf(a)) / Double.valueOf(iters);
        String firstFuncSubstitution = equation.replaceAll("x", a);

        double secondNumber = 0;

        if(firstFuncSubstitution.charAt(0) == 'a')
            secondNumber += this.sum(firstFuncSubstitution);
        else if(firstFuncSubstitution.charAt(0) == 'm')
            secondNumber += this.multiply(firstFuncSubstitution);
        else if(firstFuncSubstitution.charAt(0) == 'd')
            secondNumber += this.divition(firstFuncSubstitution);

        String secondFuncSubstitution = equation.replaceAll("x", b);
        double thirdNumber = 0;

        if(secondFuncSubstitution.charAt(0) == 'a')
            thirdNumber += this.sum(secondFuncSubstitution);
        else if(secondFuncSubstitution.charAt(0) == 'm')
            thirdNumber += this.multiply(secondFuncSubstitution);
        else if(secondFuncSubstitution.charAt(0) == 'd')
            thirdNumber += this.divition(secondFuncSubstitution);

        double loopValue = 0;
        for(int i = 1; i < Double.valueOf(iters); i++){
            double sust = Double.valueOf(a) + i*(Double.valueOf(b) - Double.valueOf(a)) / Double.valueOf(iters);
            String loopSubstitution = equation.replaceAll("x", String.valueOf(sust));

            if(loopSubstitution.charAt(0) == 'a')
                loopValue += this.sum(loopSubstitution);
            else if(loopSubstitution.charAt(0) == 'm')
                loopValue += this.multiply(loopSubstitution);
            else if(loopSubstitution.charAt(0) == 'd')
                loopValue += this.divition(loopSubstitution);
        }

        return firstNumber * (this.divition("d(" + secondNumber + " : 2)") + loopValue
                + this.divition("d(" + thirdNumber + " : 2)"));
    }
}
