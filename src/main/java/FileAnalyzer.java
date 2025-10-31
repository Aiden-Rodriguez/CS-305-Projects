import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that holds analyzes file string
 * and gives the metrics
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */

public final class FileAnalyzer {

    private FileAnalyzer() {}

    public static final class AnalysisResult {
        public final long lineCount;
        public final int ifCount, switchCount, forCount, whileCount;
        public final boolean hasAuthor, hasVersion;

        public AnalysisResult(long lineCount,
                              int ifCount, int switchCount, int forCount, int whileCount,
                              boolean hasAuthor, boolean hasVersion) {
            this.lineCount   = lineCount;
            this.ifCount     = ifCount;
            this.switchCount = switchCount;
            this.forCount    = forCount;
            this.whileCount  = whileCount;
            this.hasAuthor   = hasAuthor;
            this.hasVersion  = hasVersion;
        }
    }

    public static AnalysisResult analyze(String content) {
        if (content == null) content = "";

        // Count lines (handles \n, \r\n, \r)
        long lineCount = content.isEmpty() ? 0
                : content.split("\\R", -1).length;

        int ifCount     = countWord(content, "(?i)\\bif\\b");
        int switchCount = countWord(content, "(?i)\\bswitch\\b");
        int forCount    = countWord(content, "(?i)\\bfor\\b");
        int whileCount  = countWord(content, "(?i)\\bwhile\\b");

        boolean hasAuthor  = containsTag(content, "@author");
        boolean hasVersion = containsTag(content, "@version");

        return new AnalysisResult(
                lineCount, ifCount, switchCount, forCount, whileCount, hasAuthor, hasVersion
        );
    }

    private static int countWord(String text, String regexWord) {
        Matcher m = Pattern.compile(regexWord).matcher(text);
        int count = 0;
        while (m.find()) count++;
        return count;
    }

    private static boolean containsTag(String text, String tag) {
        return Pattern.compile("(?i)" + Pattern.quote(tag)).matcher(text).find();
    }
}
