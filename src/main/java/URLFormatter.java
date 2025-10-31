/**
 * Utility class for URL formatting and manipulation.
 * Handles conversion between GitHub URL formats and file name extraction.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public final class URLFormatter {

    private URLFormatter() {
        // Prevent instantiation
    }

    /**
     * Converts a GitHub URL to tree directory format.
     * Changes /blob/ to /tree/ and removes trailing slashes.
     */
    public static String toTreeDirUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        url = url.replace("/blob/", "/tree/");
        return url.replaceAll("/+$", "");
    }

    public static String extractFileName(String pathOrUrl) {
        if (pathOrUrl == null || pathOrUrl.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }

        String trimmed = pathOrUrl.replaceAll("/+$", "");
        int lastSlash = trimmed.lastIndexOf('/');

        if (lastSlash == -1) {
            return trimmed;
        }
        return trimmed.substring(lastSlash + 1);
    }
}