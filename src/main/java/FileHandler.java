import javiergs.tulip.GitHubHandler;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class FileHandler {

    private final static String token = "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";

    private FileHandler(){}

    public static List<String> getFileList(String url){

        try {
            GitHubHandler gh = new GitHubHandler(token);
            List<String> files = gh.listFilesRecursive(url);
            displayFiles(files);
            return files;

            //centerPanel.displayFiles(files);

        } catch (IOException ex) {
            //centerPanel.displayError("IO Error: " + ex.getMessage());
            /*JOptionPane.showMessageDialog(this,
                    "Failed to fetch files:\n" + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);*/
            ex.printStackTrace();

        } catch (Exception ex) {
            //centerPanel.displayError("Error: " + ex.getMessage());
            /*JOptionPane.showMessageDialog(this,
                    "An error occurred:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);*/
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

    public static void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }
}
