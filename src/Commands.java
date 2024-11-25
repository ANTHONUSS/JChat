public class Commands {
    public static void help(){
        StringBuilder text = new StringBuilder();
        text.append("List of available commands :\n");
        text.append("/exit \t| Stop the program.\n");
        text.append("/help \t| Show this list.\n");
        System.out.println(text);
    }
}
