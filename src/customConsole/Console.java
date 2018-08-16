package customConsole;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Console {

    private static final String RESET = "\u001B[0m";

    private static final String BOLD = "\u001B[1m";

    private static final String ITALIC = "\033[3m";
    private static final String UNDERLINE = "\u001B[4m";
    private static final String BLINK = "\u001B[5m";
    private static final String RAPID_BLINK = "\u001B[6m";
    private static final String REVERSE_VIDEO = "\u001B[7m";

    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    private static final String BACKGROUND_BLACK = "\u001B[40m";
    private static final String BACKGROUND_RED = "\u001B[41m";
    private static final String BACKGROUND_GREEN = "\u001B[42m";
    private static final String BACKGROUND_YELLOW = "\u001B[43m";
    private static final String BACKGROUND_BLUE = "\u001B[44m";
    private static final String BACKGROUND_PURPLE = "\u001B[45m";
    private static final String BACKGROUND_CYAN = "\u001B[46m";
    private static final String BACKGROUND_WHITE = "\u001B[47m";

    private static GraphicConsole window;
    private static final Font FONT_BOLD = new Font(Font.DIALOG, Font.BOLD, 18);
    private static final Font FONT_ITALIC = new Font(Font.DIALOG, Font.ITALIC, 18);
    private static Font welcomeFont;

    private Console(){ }

    public static void printLine(ConsoleColors text, String message, boolean stack, Optional<String> secondMessage){
        if(GraphicConsole.window == null)
            System.out.println(consoleColorToTextColor(text) + message + Console.RESET);
        else{
            JLabel label = new JLabel(message);
            label.setForeground(consoleColorToAwtColor(text));

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setForeground(consoleColorToAwtColor(text));
            GraphicConsole.window.addMessage(label, label2, false, stack);
        }
    }

    public static void printLineBold(ConsoleColors text, String message, boolean stack, Optional<String> secondMessage){
        if(Console.window == null)
            System.out.println(consoleColorToTextColor(text) + Console.BOLD + message + Console.RESET);
        else{
            JLabel label = new JLabel(message);
            label.setFont(Console.FONT_BOLD);
            label.setForeground(consoleColorToAwtColor(text));

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setFont(Console.FONT_BOLD);
            label2.setForeground(consoleColorToAwtColor(text));

            Console.window.addMessage(label, label2, false, stack);
        }
    }

    public static void printLineWithBackground(ConsoleColors text, ConsoleColors background, String message
            , boolean stack, Optional<String> secondMessage){
        if(Console.window == null)
            System.out.println(consoleColorToTextColor(text) + consoleColorToBackgroundColor(background) + message
                    + Console.RESET);
        else{
            JLabel label = new JLabel(message);
            label.setForeground(consoleColorToAwtColor(text));
            label.setBackground(consoleColorToAwtColor(background));

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setForeground(consoleColorToAwtColor(text));
            label2.setBackground(consoleColorToAwtColor(background));

            Console.window.addMessage(label, label2, false, stack);
        }
    }

    public static void printLineWithBackgroundAndBold(ConsoleColors text, ConsoleColors background, String message
            , boolean stack, Optional<String> secondMessage){
        if(Console.window == null)
            System.out.println(consoleColorToTextColor(text) + consoleColorToBackgroundColor(background) + Console.BOLD
                    + message + Console.RESET);
        else{
            JLabel label = new JLabel(message);
            label.setFont(Console.FONT_BOLD);
            label.setForeground(consoleColorToAwtColor(text));
            label.setBackground(consoleColorToAwtColor(background));

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setFont(Console.FONT_BOLD);
            label2.setForeground(consoleColorToAwtColor(text));
            label2.setBackground(consoleColorToAwtColor(background));

            Console.window.addMessage(label, label2, false, stack);
        }
    }

    //=============================================== UNDERLINE ========================================================

    public static void printLineUnderline(ConsoleColors text, String message, boolean stack, Optional<String> secondMessage){
        System.out.println(consoleColorToTextColor(text) + Console.UNDERLINE + message + Console.RESET);
    }

    public static void printLineBoldUnderline(ConsoleColors text, String message, boolean stack, Optional<String> secondMessage){
        System.out.println(consoleColorToTextColor(text) + Console.BOLD + Console.UNDERLINE + message + Console.RESET);
    }

    public static void printLineWithBackgroundUnderline(ConsoleColors text, ConsoleColors background, String message
            , boolean stack, Optional<String> secondMessage){
        System.out.println(consoleColorToTextColor(text) + consoleColorToBackgroundColor(background) + Console.UNDERLINE
                + message + Console.RESET);
    }

    public static void printLineWithBackgroundAndBoldUnderline(ConsoleColors text, ConsoleColors background, String message
            , boolean stack, Optional<String> secondMessage){
        System.out.println(consoleColorToTextColor(text) + consoleColorToBackgroundColor(background) + Console.BOLD
                + Console.UNDERLINE + message + Console.RESET);
    }

    //============================================== MESSAGES ==========================================================

    public static void printFailure(String failure, boolean stack, Optional<String> secondMessage){
        if(Console.window == null)
            System.out.println(Console.RED + Console.BOLD + Console.UNDERLINE + failure.toUpperCase() + Console.RESET);
        else{
            JLabel label = new JLabel(failure.toUpperCase());
            label.setFont(Console.FONT_BOLD);
            label.setForeground(Color.RED);

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setFont(Console.FONT_BOLD);
            label2.setForeground(Color.RED);

            Console.window.addMessage(label, label2, false, stack);
        }
    }

    public static void printSuccess(String success, boolean stack, Optional<String> secondMessage){
        if(Console.window == null)
            System.out.println(Console.GREEN + Console.BOLD + Console.UNDERLINE + success.toUpperCase() + Console.RESET);
        else{
            JLabel label = new JLabel(success.toUpperCase());
            label.setFont(Console.FONT_BOLD);
            label.setForeground(Color.GREEN);

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setFont(Console.FONT_BOLD);
            label2.setForeground(Color.GREEN);

            Console.window.addMessage(label, label2, false, stack);
        }
    }

    public static void printWarning(String warning, boolean stack, Optional<String> secondMessage){
        if(Console.window == null)
            System.out.println(Console.YELLOW + Console.BOLD + warning + Console.RESET);
        else{
            JLabel label = new JLabel(warning);
            label.setFont(Console.FONT_ITALIC);
            label.setForeground(Color.YELLOW);

            JLabel label2 = new JLabel(secondMessage.orElseGet(Console::getFullInfo));
            label2.setFont(FONT_ITALIC);
            label2.setForeground(Color.YELLOW);

            Console.window.addMessage(label, label2, false, stack);
        }
    }

    private static String getFullInfo(){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

        return "Class: " + className + ", Method: " + (methodName.equals("<init>") ? "Constructor"
                : methodName) + ", Line: " + lineNumber;
    }

    private static String consoleColorToTextColor(ConsoleColors color){
        switch (color) {
            case RED : return RED;
            case BLUE : return BLUE;
            case CYAN : return CYAN;
            case BLACK : return BLACK;
            case GREEN : return GREEN;
            case WHITE : return WHITE;
            case PURPLE : return PURPLE;
            case YELLOW : return YELLOW;
            default : return null;
        }
    }

    private static String consoleColorToBackgroundColor(ConsoleColors color){
        switch (color) {
            case RED : return BACKGROUND_RED;
            case BLUE : return BACKGROUND_BLUE;
            case CYAN : return BACKGROUND_CYAN;
            case BLACK : return BACKGROUND_BLACK;
            case GREEN : return BACKGROUND_GREEN;
            case WHITE : return BACKGROUND_WHITE;
            case PURPLE : return BACKGROUND_PURPLE;
            case YELLOW : return BACKGROUND_YELLOW;
            default : return null;
        }
    }

    private static Color consoleColorToAwtColor(ConsoleColors color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case CYAN:
                return Color.CYAN;
            case BLACK:
                return Color.BLACK;
            case GREEN:
                return Color.GREEN;
            case WHITE:
                return Color.WHITE;
            case PURPLE:
                return Color.MAGENTA;
            case YELLOW:
                return Color.YELLOW;
            default:
                return null;
        }
    }

    public static void generateGraphicWindow(){
        welcomeFont = AddFont.createFont();
        Console.window = GraphicConsole.getWindowInstance();
    }

    public static void addConsoleCommand(ConsoleCommands cc){
        if(Console.window != null)
            window.addCustomCommand(cc);
    }

    public static void addConsoleCommandKeywords(){
        Console.window.addAutoCompletation();
    }

    private static class GraphicConsole extends JFrame{
        private static GraphicConsole window = null;
        private JScrollPane messageAndLocationScrollPane;
        private JSplitPane messageAndLocationSplitPane;
        private JPanel leftPane;
        private JPanel rightPane;
        private JSplitPane mainSplitPane;
        private JTextField input;
        private JPanel inputAndButtonPanel;
        private JButton run;
        private JButton custom;
        final String COMMIT_ACTION = "commit";

        private List<ConsoleCommands> customCommands;
        private List<String> keywords;

        private GraphicConsole(){
            super.setVisible(true);
            super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            super.setSize(1000, 500);
            super.setTitle("Console");
            this.messageAndLocationSplitPane = new JSplitPane();

            this.leftPane = new JPanel();
            this.leftPane.setLayout(new BoxLayout(this.leftPane, BoxLayout.PAGE_AXIS));
            this.leftPane.setPreferredSize(new Dimension(400, 600));
            this.leftPane.setBackground(new Color(43, 43, 43));

            this.rightPane = new JPanel();
            this.rightPane.setLayout(new BoxLayout(this.rightPane, BoxLayout.PAGE_AXIS));
            this.rightPane.setPreferredSize(new Dimension(400, 600));
            this.rightPane.setBackground(new Color(43, 43, 43));

            this.messageAndLocationSplitPane.setBorder(BorderFactory.createLineBorder(new Color(43, 43, 43)));
            this.messageAndLocationSplitPane.setPreferredSize(new Dimension(800, 600));
            this.messageAndLocationSplitPane.setForeground(new Color(43, 43, 43));
            this.messageAndLocationSplitPane.setRightComponent(this.rightPane);
            this.messageAndLocationSplitPane.setLeftComponent(this.leftPane);
            this.messageAndLocationSplitPane.setResizeWeight(0.5);
            this.messageAndLocationSplitPane.setDividerSize(1);
            this.messageAndLocationSplitPane.setUI(new BasicSplitPaneUI() {
                public BasicSplitPaneDivider createDefaultDivider() {
                    return new BasicSplitPaneDivider(this) {
                        public void setBorder(Border b) {
                        }

                        @Override
                        public void paint(Graphics g) {
                            g.setColor(new Color(43, 43, 43));
                            g.fillRect(0, 0, getSize().width, getSize().height);
                            super.paint(g);
                        }
                    };
                }
            });
            this.messageAndLocationSplitPane.setBorder(null);

            this.messageAndLocationScrollPane = new JScrollPane(this.messageAndLocationSplitPane);
            this.messageAndLocationScrollPane.getVerticalScrollBar().setBackground(new Color(89, 91, 93));

            this.input = new JTextField();
            this.input.setBackground(new Color(43, 43, 43));
            this.input.setForeground(Color.WHITE);
            this.input.setFont(FONT_BOLD);
            this.input.setCaretColor(Color.WHITE);
            this.run = new JButton("Run");
            this.run.setFont(this.run.getFont().deriveFont(20.0f));
            this.run.setBackground(new Color(43, 43, 43));
            this.run.setForeground(Color.WHITE);
            this.runFunction();

            this.custom = new JButton("Custom");
            this.custom.setFont(this.custom.getFont().deriveFont(20.0f));
            this.custom.setBackground(new Color(43, 43, 43));
            this.custom.setForeground(Color.WHITE);
            this.customFunction();

            this.inputAndButtonPanel = new JPanel(new BorderLayout());
            this.inputAndButtonPanel.add(this.input);
            this.inputAndButtonPanel.add(this.custom, BorderLayout.LINE_START);
            this.inputAndButtonPanel.add(this.run, BorderLayout.LINE_END);
            this.mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    this.messageAndLocationScrollPane, this.inputAndButtonPanel);
            this.mainSplitPane.setDividerLocation((super.getWidth() / 3) + (super.getWidth() / 25));
            this.mainSplitPane.setDividerSize(1);
            this.mainSplitPane.setBackground(new Color(43, 43, 43));
            this.mainSplitPane.setBorder(BorderFactory.createLineBorder(new Color(43, 43, 43)));
            this.mainSplitPane.setUI(new BasicSplitPaneUI() {
                public BasicSplitPaneDivider createDefaultDivider() {
                    return new BasicSplitPaneDivider(this) {
                        public void setBorder(Border b) {
                        }

                        @Override
                        public void paint(Graphics g) {
                            g.setColor(new Color(43, 43, 43));
                            g.fillRect(0, 0, getSize().width, getSize().height);
                            super.paint(g);
                        }
                    };
                }
            });
            this.mainSplitPane.setBorder(null);

            super.add(this.mainSplitPane);
            this.welcomeMessage();
            this.addTopLabels();
            ImageIcon img = new ImageIcon("consoleAssets/console.png");
            super.setIconImage(img.getImage());
            super.revalidate();
            super.repaint();

            this.customCommands = new ArrayList<>();
        }

        public static GraphicConsole getWindowInstance(){
            if(GraphicConsole.window == null)
                GraphicConsole.window = new GraphicConsole();
            return GraphicConsole.window;
        }

        public void addCustomCommand(ConsoleCommands consoleCommands){
            this.customCommands.add(consoleCommands);
        }

        public void addMessage(JLabel message, JLabel location, boolean isCommand, boolean stack){
            if(!stack || isCommand){
                ((JPanel) this.messageAndLocationSplitPane.getLeftComponent()).add(message);
                ((JPanel) this.messageAndLocationSplitPane.getRightComponent()).add(location);
            }else{
                int pos = 0;
                if((pos = isMessageRepeated(message.getText(), location.getText())) != -1){
                    int number = Integer.valueOf(((JLabel)this.leftPane.getComponent(pos)).getText().split(":x")[1]);
                    number++;

                    if(((JLabel)this.rightPane.getComponent(pos)).getText().contains("Line: ")){
                        String lines[] = ((JLabel)this.rightPane.getComponent(pos)).getText().split("Line:")[1].split("\\s+");
                        if(lines.length == 1){
                            if(!lines[0].trim().equals(location.getText().split("Line:")[1].split("\\s+")[1].trim())){
                                ((JLabel)this.rightPane.getComponent(pos)).setText(((JLabel)this.rightPane.getComponent(pos)).getText() +
                                        " " + location.getText().split("Line:")[1].split("\\s+")[1]);
                            }
                        }else{
                            boolean equal = false;
                            for(String s : lines){
                                if(s.trim().equals(location.getText().split("Line:")[1].split("\\s+")[1].trim())){
                                    equal = true;
                                }
                            }

                            if(!equal)
                                ((JLabel)rightPane.getComponent(pos)).setText(((JLabel)rightPane.getComponent(pos)).getText() +
                                        " " + location.getText().split("Line:")[1].split("\\s+")[1]);
                        }
                    }

                    ((JLabel)this.leftPane.getComponent(pos)).setText(((JLabel)this.leftPane.getComponent(pos))
                            .getText().split(":x")[0] + ":x" + number);
                }else{
                    if(this.leftPane.getComponentCount() == 0)
                        message.setText(message.getText());
                    else
                        message.setText(message.getText() + " :x1");
                    ((JPanel)this.messageAndLocationSplitPane.getLeftComponent()).add(message);
                    ((JPanel)this.messageAndLocationSplitPane.getRightComponent()).add(location);
                }
            }
            this.messageAndLocationSplitPane.revalidate();
            this.messageAndLocationSplitPane.repaint();
        }

        private int isMessageRepeated(String message, String location){
            for(int i = 0; i < this.leftPane.getComponentCount(); i++){
                if(((JLabel)this.leftPane.getComponent(i)).getText().split(":x")[0].trim().equals(message.split(":x")[0].trim())
                        &&
                        ((JLabel)this.rightPane.getComponent(i)).getText().equals(location))
                    return i;

                if(((JLabel)this.leftPane.getComponent(i)).getText().split(":x")[0].trim().equals(message.split(":x")[0].trim())
                        &&
                        ((JLabel)this.rightPane.getComponent(i)).getText().split("Class: ")[1].split(",")[0]
                                .equals(location.split("Class: ")[1].split(",")[0]))
                    return i;
            }
            return -1;
        }

        private void welcomeMessage(){
            JLabel left = new JLabel("      WELCOME       TO", SwingConstants.CENTER);
            left.setFont(Console.welcomeFont);
            left.setForeground(Color.WHITE);
            left.setFont(left.getFont().deriveFont(50.0f));

            JLabel right = new JLabel("THE      CONSOLE ", SwingConstants.CENTER);
            right.setFont(Console.welcomeFont);
            right.setForeground(Color.WHITE);
            right.setFont(right.getFont().deriveFont(50.0f));

            this.addMessage(left, right, true, false);
            JLabel space = new JLabel("     ");
            space.setFont(Console.FONT_BOLD);
            JLabel space2 = new JLabel("    ");
            space2.setFont(Console.FONT_BOLD);
            this.addMessage(space, space2, true, false);
        }

        private void addTopLabels(){
            JLabel messageLabel = new JLabel("                  Messages                    ", SwingConstants.CENTER);
            messageLabel.setFont(Console.FONT_BOLD);
            messageLabel.setOpaque(true);
            messageLabel.setBackground(new Color(89, 91, 93));
            messageLabel.setForeground(new Color(187, 187,187));

            JLabel locationLabel = new JLabel("                  Location                    ", SwingConstants.CENTER);
            locationLabel.setFont(Console.FONT_BOLD);
            locationLabel.setOpaque(true);
            locationLabel.setBackground(new Color(89, 91, 93));
            locationLabel.setForeground(new Color(187, 187,187));

            addMessage(messageLabel, locationLabel, true, false);

        }

        private void runFunction(){
            this.run.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(input.getText() != null && !input.getText().trim().equals(""))
                        analyzeCommand(input.getText());
                }
            });
        }

        private void customFunction(){
            this.custom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(input.getText() != null && !input.getText().trim().equals(""))
                        for(ConsoleCommands cc : customCommands){
                            if(input.getText().contains(cc.getHeader())){
                                cc.exec(input.getText());
                                break;
                            }
                        }
                }
            });
        }

        private void analyzeCommand(String command){
            if(command.trim().toLowerCase().equals("info")){
                executeInfo();
            }else if(command.contains("search M ") || command.contains("search L ")){
                String toSearch = command.substring(9, command.length());
                executeSearch(toSearch, command.split("\\s+")[1]);
            }
        }

        private void executeInfo(){
            JLabel label = new JLabel("\n\n       Available commands      ");
            label.setFont(Console.FONT_BOLD);
            Font font = label.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
            label.setFont(font.deriveFont(attributes));
            label.setForeground(Color.GREEN);

            JLabel label2 = new JLabel("\n\n      Function        ");
            label2.setFont(Console.FONT_BOLD);
            font = label2.getFont();
            attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
            label2.setFont(font.deriveFont(attributes));
            label2.setForeground(Color.GREEN);

            Console.window.addMessage(label, label2, true, false);

            //==================================================================
            JLabel command = new JLabel("Info");
            command.setFont(Console.FONT_BOLD);
            command.setForeground(Color.WHITE);

            JLabel description = new JLabel("Shows available commands");
            description.setFont(Console.FONT_BOLD);
            description.setForeground(Color.WHITE);

            Console.window.addMessage(command, description, true, false);

            //==================================================================
            command = new JLabel("Search M -to_search-");
            command.setFont(Console.FONT_BOLD);
            command.setForeground(Color.WHITE);

            description = new JLabel("Search for words in message column");
            description.setFont(Console.FONT_BOLD);
            description.setForeground(Color.WHITE);

            Console.window.addMessage(command, description, true, false);

            //==================================================================
            command = new JLabel("Search L -to_search- \n\n\n\n");
            command.setFont(Console.FONT_BOLD);
            command.setForeground(Color.WHITE);

            description = new JLabel("Search for words in location column \n\n\n\n");
            description.setFont(Console.FONT_BOLD);
            description.setForeground(Color.WHITE);

            Console.window.addMessage(command, description, true, false);

            if(this.customCommands.size() > 0){
                //=================================================================
                JLabel customLabel = new JLabel("\n\n       Custom commands      ");
                customLabel.setFont(Console.FONT_BOLD);
                font = customLabel.getFont();
                attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
                customLabel.setFont(font.deriveFont(attributes));
                customLabel.setForeground(Color.GREEN);

                JLabel customLabel2 = new JLabel("\n\n      Function        ");
                customLabel2.setFont(Console.FONT_BOLD);
                font = customLabel2.getFont();
                attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
                customLabel2.setFont(font.deriveFont(attributes));
                customLabel2.setForeground(Color.GREEN);

                Console.window.addMessage(customLabel, customLabel2, true, false);

                for(int i = 0; i < this.customCommands.size(); i++){
                    command = new JLabel(this.customCommands.get(i).getLibraryName() + ((i + 1 == this.customCommands.size())
                            ? "\n\n\n\n" : ""));
                    command.setFont(Console.FONT_BOLD);
                    command.setForeground(Color.orange);

                    description = new JLabel("  " + ((i + 1 == this.customCommands.size())
                            ? "\n\n\n\n" : ""));
                    description.setFont(Console.FONT_BOLD);
                    description.setForeground(Color.WHITE);
                    Console.window.addMessage(command, description, true, false);

                    for(int com = 0; com < this.customCommands.get(i).getCommands().size(); com++){
                        command = new JLabel(this.customCommands.get(i).getCommands().get(com)
                                + ((i + 1 == this.customCommands.size())
                                ? "\n\n\n\n" : ""));
                        command.setFont(Console.FONT_BOLD);
                        command.setForeground(Color.WHITE);

                        description = new JLabel(this.customCommands.get(i).getCommandsDescription().get(com)
                                + ((i + 1 == this.customCommands.size())
                                ? "\n\n\n\n" : ""));
                        description.setFont(Console.FONT_BOLD);
                        description.setForeground(Color.WHITE);

                        Console.window.addMessage(command, description, true, false);
                    }
                }
            }
        }

        private void executeSearch(String toSearch, String column){
            this.cleanSearch();
            if(column.equals("M")){
                for(int i = 0; i < this.leftPane.getComponentCount(); i++){
                    if(((JLabel)this.leftPane.getComponent(i)).getText().contains(toSearch)){
                        ((JLabel) this.leftPane.getComponent(i)).setOpaque(true);
                        this.leftPane.getComponent(i).setBackground(Color.ORANGE);
                    }
                }
            }else{
                for(int i = 0; i < this.rightPane.getComponentCount(); i++){
                    if(((JLabel)this.rightPane.getComponent(i)).getText().contains(toSearch)){
                        ((JLabel) this.rightPane.getComponent(i)).setOpaque(true);
                        this.rightPane.getComponent(i).setBackground(Color.ORANGE);
                    }
                }
            }

            super.revalidate();
            super.repaint();
        }

        private void cleanSearch(){
            for(int i = 0; i < this.leftPane.getComponentCount(); i++)
                if(i != 0)
                    ((JLabel) this.leftPane.getComponent(i)).setOpaque(false);

            for(int i = 0; i < this.rightPane.getComponentCount(); i++)
                if(i != 0)
                    ((JLabel)this.rightPane.getComponent(i)).setOpaque(false);

            super.revalidate();
            super.repaint();
        }

        private void addMoreKeywords(List<String> keywords){
            this.keywords.addAll(keywords);
        }

        private void addAutoCompletation(){
            this.keywords = new ArrayList<>();
            this.keywords.add("info");
            this.keywords.add("search");

            for(ConsoleCommands cc : this.customCommands)
                this.keywords.addAll(cc.getKeyWords());

            AutoComplete autoComplete = new AutoComplete(this.input, keywords);
            this.input.getDocument().addDocumentListener(autoComplete);
            this.input.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
            this.input.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
            this.input.setFocusTraversalKeysEnabled(false);
        }
    }

    private static class AddFont {
        private static Font ttfBase = null;
        private static Font telegraficoFont = null;
        private static InputStream myStream = null;
        private static final String FONT_PATH_TELEGRAFICO = "consoleAssets/karmaFuture.ttf";

        public static Font createFont() {
            try {
                myStream = new BufferedInputStream(
                        new FileInputStream(FONT_PATH_TELEGRAFICO));
                ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
                telegraficoFont = ttfBase.deriveFont(Font.PLAIN, 24);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("Font not loaded.");
            }
            return telegraficoFont;
        }
    }

}

