package ToolBox;

public class General {

    public static void activateError(Exception e, String errorMessage, int exit) {
        System.err.println(errorMessage + "\n");
        if (e != null) {
            e.printStackTrace();
        }
        System.exit(exit);
    }

}
