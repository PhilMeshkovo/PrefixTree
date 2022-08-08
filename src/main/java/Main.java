import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/numbers.txt"));
        TreeNode root = new TreeNode(' ');
        for (String line : lines) {
            root.insert(line);
        }
        writeTreeToFile("src/main/resources/data.txt", root);
        TreeNode fromFile = readTreeNode("src/main/resources/data.txt");
        List<String> extractedNumbers = new ArrayList<>();
        fromFile.getAllNumbers("", extractedNumbers);
    }

    static class TreeNode {
        char value;
        List<TreeNode> children;

        public TreeNode(char value) {
            this.value = value;
        }

        public void insert(String data) {
            if (data.length() == 0) {
                return;
            }
            if (children == null) {
                children = new ArrayList<>();
            }
            char c = data.charAt(0);
            TreeNode child = findNodeByChar(c);
            if (child == null) {
                child = new TreeNode(c);
                children.add(child);
            }
            child.insert(data.substring(1));
        }

        public void getAllNumbers(String path, List<String> result) {
            if (value != ' ') {
                path = path + value;
            }
            if (children != null) {
                for (TreeNode child : children) {
                    child.getAllNumbers(path, result);
                }
            }
            else {
                result.add(path);
            }
        }

        public void readFromFile(FileReader reader) throws IOException {
            char ch;
            while ((ch = (char) reader.read()) != ']') {
                TreeNode treeNode = new TreeNode(ch);
                treeNode.readFromFile(reader);
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(treeNode);
            }
        }

        public void writeToFile(PrintWriter writer) {
            writer.write(value);
            if (children != null) {
                for (TreeNode child : children) {
                    child.writeToFile(writer);
                }
            }
            writer.write("]");

        }

        private TreeNode findNodeByChar(char c) {
            if (children != null) {
                for (TreeNode child : children) {
                    if (child.value == c) {
                        return child;
                    }
                }
            }
            return null;
        }

        private boolean containString(String str) {
            TreeNode current = this;
            for (int i = 0; i < str.length(); i++) {
                current = current.findNodeByChar(str.charAt(i));
                if (current == null) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void writeTreeToFile(String path, TreeNode root) {
        try {
            PrintWriter out = new PrintWriter(path);
            root.writeToFile(out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static TreeNode readTreeNode(String path)  {
        TreeNode  root = new TreeNode(' ');
        try {
            FileReader reader = new FileReader(path);
            reader.read();
            root.readFromFile(reader);
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return root;
    }
}
