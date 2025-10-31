import javiergs.tulip.GitHubHandler;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Handles fetching files from GitHub using the GitHubHandler
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */
public final class FileHandler {

    private final static String TOKEN = "";

    private FileHandler() {}

    public static void getFileListAsync(String url, FileListCallback callback) {
        new FileListWorker(url, callback).execute();
    }

    public static String getFile(String url) {
        try {
            GitHubHandler gh = new GitHubHandler(TOKEN);
            return gh.getFileContentFromUrl(url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch file: " + url, e);
        }
    }

    private static class FileListWorker extends SwingWorker<List<String>, String> {
        private final String url;
        private final FileListCallback callback;

        FileListWorker(String url, FileListCallback callback) {
            this.url = url;
            this.callback = callback;
        }

        @Override
        protected List<String> doInBackground() throws Exception {
            publish("Connecting to GitHub...");

            List<String> relativePaths = fetchFilesFromGitHub(url);
            if (relativePaths == null || relativePaths.isEmpty()) {
                publish("No files found");
                return Collections.emptyList();
            }

            publish("Processing " + relativePaths.size() + " files...");

            List<String> javaFileUrls = buildJavaFileUrls(url, relativePaths);

            publish("Found " + javaFileUrls.size() + " Java files");
            return javaFileUrls;
        }

        @Override
        protected void process(List<String> chunks) {
            String latest = chunks.get(chunks.size() - 1);
            Blackboard.getInstance().setStatusBarMessage(latest);
        }

        @Override
        protected void done() {
            try {
                List<String> result = get();
                callback.onSuccess(result);
            } catch (CancellationException e) {
                Blackboard.getInstance().setStatusBarMessage("Operation canceled");
            } catch (Exception e) {
                handleError("Error: " + e.getMessage());
                callback.onError(e);
            }
        }
    }

    private static List<String> fetchFilesFromGitHub(String url) throws IOException {
        GitHubHandler gh = new GitHubHandler(TOKEN);
        return gh.listFilesRecursive(url);
    }

    private static List<String> buildJavaFileUrls(String baseTreeUrl, List<String> relativePaths) {
        URLBuilder urlBuilder = new URLBuilder(baseTreeUrl);
        List<String> javaFileUrls = new ArrayList<>();

        for (String relativePath : relativePaths) {
            if (relativePath.endsWith(".java")) {
                String fullUrl = urlBuilder.buildBlobUrl(relativePath);
                javaFileUrls.add(fullUrl);
            }
        }
        return javaFileUrls;
    }

    private static void handleError(String message) {
        System.err.println(message);
        SwingUtilities.invokeLater(() ->
                Blackboard.getInstance().setStatusBarMessage(message)
        );
    }

    private static class URLBuilder {
        private final String baseUrl;
        private final String branchName;

        public URLBuilder(String treeUrl) {
            this.baseUrl = extractBaseUrl(treeUrl);
            this.branchName = extractBranchName(treeUrl);
        }

        public String buildBlobUrl(String relativePath) {
            return baseUrl + "/blob/" + branchName + "/" + relativePath;
        }

        private String extractBaseUrl(String url) {
            if (url.contains("/tree/")) {
                return url.substring(0, url.indexOf("/tree/"));
            }
            return url;
        }

        private String extractBranchName(String url) {
            if (!url.contains("/tree/")) {
                return "main";
            }
            int treeIndex = url.indexOf("/tree/");
            String afterTree = url.substring(treeIndex + 6);
            int nextSlash = afterTree.indexOf('/');
            return nextSlash != -1 ? afterTree.substring(0, nextSlash) : afterTree;
        }
    }

    public interface FileListCallback {
        void onSuccess(List<String> javaFileUrls);
        void onError(Exception e);
    }
}