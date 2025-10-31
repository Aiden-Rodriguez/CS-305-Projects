import javiergs.tulip.GitHubHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that deals with the fetching of github files
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */

public final class FileHandler {

    private final static String token = "";

    private FileHandler(){}

    /**
     * Gets a list of file URLs from a GitHub directory recursively.
     * Returns full blob URLs that can be used directly with getFile().
     */
    public static List<String> getFileList(String url){
        try {
            GitHubHandler gh = new GitHubHandler(token);

            Blackboard.getInstance().setStatusBarMessage("Connecting to GitHub...");
            List<String> files = gh.listFilesRecursive(url);

            if (files == null || files.isEmpty()) {
                Blackboard.getInstance().setStatusBarMessage("No files found");
                return Collections.emptyList();
            }

            Blackboard.getInstance().setStatusBarMessage("Processing " + files.size() + " files...");

            String baseUrl = url;
            if (url.contains("/tree/")) {
                baseUrl = url.substring(0, url.indexOf("/tree/"));
            }

            // Get the branch/ref part
            String branchPart = "";
            if (url.contains("/tree/")) {
                int treeIndex = url.indexOf("/tree/");
                String afterTree = url.substring(treeIndex + 6); // skip "/tree/"
                int nextSlash = afterTree.indexOf('/');
                if (nextSlash != -1) {
                    branchPart = afterTree.substring(0, nextSlash);
                } else {
                    branchPart = afterTree;
                }
            }

            List<String> javaFileUrls = new ArrayList<>();
            for (String relativePath : files) {
                if (relativePath.endsWith(".java")) {
                    // Construct full blob URL: baseUrl/blob/branch/relativePath
                    String fullUrl = baseUrl + "/blob/" + branchPart + "/" + relativePath;
                    javaFileUrls.add(fullUrl);
                }
            }

            displayFiles(javaFileUrls);
            Blackboard.getInstance().setStatusBarMessage("Found " + javaFileUrls.size() + " Java files");
            return javaFileUrls;

        } catch (IOException ex) {
            System.err.println("IO Error: " + ex.getMessage());
            Blackboard.getInstance().setStatusBarMessage("IO Error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            Blackboard.getInstance().setStatusBarMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return Collections.emptyList();
    }

    public static String getFile(String url){
        try {
            GitHubHandler gh = new GitHubHandler(token);
            String file = gh.getFileContentFromUrl(url);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayFiles(List<String> files) {
        StringBuilder sb = new StringBuilder();
        sb.append("Files found: ").append(files.size()).append("\n\n");
        for (String file : files) {
            sb.append(file).append("\n");
        }
        System.out.println(sb.toString());
    }
}