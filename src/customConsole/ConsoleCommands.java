package customConsole;

import java.util.List;

public interface ConsoleCommands {
    public void exec(String command);
    public String getHeader();
    public List<String> getCommands();
    public List<String> getCommandsDescription();
    public List<String> getKeyWords();
    public String getLibraryName();
}
