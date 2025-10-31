import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        public String toStatusMessage() {
            return String.format(
                    "%,d lines | if:%d switch:%d for:%d while:%d | @author:%s @version:%s",
                    lineCount, ifCount, switchCount, forCount, whileCount,
                    yesNo(hasAuthor), yesNo(hasVersion)
            );
        }

        private static String yesNo(boolean b) { return b ? "yes" : "no"; }
    }

    /** Analyze already-fetched file content (case-insensitive counts). */
    public static AnalysisResult analyze(String content) {
        if (content == null) content = "";

        // Count lines (handles \n, \r\n, \r)
        long lineCount = content.isEmpty() ? 0
                : content.split("\\R", -1).length; // \R = any linebreak

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
