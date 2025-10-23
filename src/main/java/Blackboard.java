/**
 * Class that holds shared global variables
 * accessible throughout the application.
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class Blackboard {

    private static String statusMessage = "Status Bar";
    public static StatusBar statusBar;
    public static FileGrid fileGrid;


    public static long MaxLinesInFile;
    public static long MaxControlStatementsInFile;

    public static long CurrentLinesInFile;
    public static long CurrentControlStatementsInFile;

    public static Boolean IsAuthorInFile;
    public static Boolean IsVersionInFile;

    public static void setStatusBarMessage(String message) {
        statusMessage = message;
        if (statusBar != null) {
            statusBar.setMessage(message);
        }
    }

    public static void setMaxLinesInFile(long maxLinesInFile) {
        MaxLinesInFile = maxLinesInFile;
    }
    public static void setMaxControlStatementsInFile(long maxControlStatementsInFile) {
        MaxControlStatementsInFile = maxControlStatementsInFile;
    }

    public static void setCurrentLinesInFile(long currentLinesInFile) {
        CurrentLinesInFile = currentLinesInFile;
    }
    public static void setCurrentControlStatementsInFile(long currentControlStatementsInFile) {
        CurrentControlStatementsInFile = currentControlStatementsInFile;
    }

    public static void setIsAuthorInFile(Boolean isAuthorInFile) {
        IsAuthorInFile = isAuthorInFile;
    }
    public static void setIsVersionInFile(Boolean isVersionInFile) {
        IsVersionInFile = isVersionInFile;
    }

    public static String getStatusBarMessage() {
        return statusMessage;
    }
}
