import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that gives the file statistics.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public final class FileAnalyzer {

    private FileAnalyzer() {}

    public static final class AnalysisResult {
        public final String fileName;
        public final long lineCount;
        public final int ifCount, switchCount, forCount, whileCount;
        public final boolean hasAuthor, hasVersion;

        public AnalysisResult(String fileName, long lineCount,
                              int ifCount, int switchCount, int forCount, int whileCount,
                              boolean hasAuthor, boolean hasVersion) {
            this.fileName   = fileName;
            this.lineCount  = lineCount;
            this.ifCount    = ifCount;
            this.switchCount= switchCount;
            this.forCount   = forCount;
            this.whileCount = whileCount;
            this.hasAuthor  = hasAuthor;
            this.hasVersion = hasVersion;
        }

        public String toStatusMessage() {
            return String.format(
                    "%s - %,d lines | if:%d switch:%d for:%d while:%d | @author:%s @version:%s",
                    fileName, lineCount, ifCount, switchCount, forCount, whileCount,
                    yesNo(hasAuthor), yesNo(hasVersion)
            );
        }

        private static String yesNo(boolean b) { return b ? "yes" : "no"; }
    }

    public static AnalysisResult analyze(File file) throws IOException {
        if (file == null || !file.isFile()) {
            throw new IOException("Not a valid file: " + file);
        }

        Path p = file.toPath();

        long lineCount;
        try (var lines = Files.lines(p)) {
            lineCount = lines.count();
        }

        String content = Files.readString(p, StandardCharsets.UTF_8);

        // Case-insensitive counts using (?i)
        int ifCount     = countWord(content, "(?i)\\bif\\b");
        int switchCount = countWord(content, "(?i)\\bswitch\\b");
        int forCount    = countWord(content, "(?i)\\bfor\\b");
        int whileCount  = countWord(content, "(?i)\\bwhile\\b");

        boolean hasAuthor  = containsTag(content, "@author");
        boolean hasVersion = containsTag(content, "@version");

        return new AnalysisResult(
                file.getName(), lineCount, ifCount, switchCount, forCount, whileCount, hasAuthor, hasVersion
        );
    }

    private static int countWord(String text, String regexWord) {
        Matcher m = Pattern.compile(regexWord).matcher(text);
        int count = 0;
        while (m.find()) count++;
        return count;
    }

    private static boolean containsTag(String text, String tag) {
        Pattern p = Pattern.compile("(?i)" + Pattern.quote(tag));
        return p.matcher(text).find();
    }
}