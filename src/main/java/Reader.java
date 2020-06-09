import bean.Directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static constant.Const.NOT_FOUND;

public class Reader {

    private final String root;

    public Reader() {
        this.root = System.getProperty("user.dir") + File.separator + "etc";
    }

    public Reader(final String rootDirectory) {
        this.root = rootDirectory;
    }

    public Directory read() {
        return this.readDirectory(root);
    }

    private Directory readDirectory(final String currentDir) {
        final Directory resultDir = new Directory();
        resultDir.setPath(currentDir);
        resultDir.setName(this.getShotName(currentDir));
        int dirCount = 0;

        try (Stream<Path> walk = Files.walk(Paths.get(currentDir))) {

            List<Path> pathList = walk.collect(Collectors.toList());

            List<String> files = pathList.stream()
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().endsWith(".java")
                            && f.getParent().toString().equals(currentDir))
                    .map(Path::toString)
                    .collect(Collectors.toList());

            for (final String fPath : files) {
                final bean.File f = new bean.File();
                f.setPath(fPath);
                f.setName(this.getShotName(fPath));
                this.readFile(f);
                dirCount += f.getCount();

                resultDir.getFileList().add(f);
            }

            List<String> directories = pathList.stream()
                    .filter(p -> Files.isDirectory(p)
                            && !p.toString().equals(currentDir)
                            && p.getParent().toString().equals(currentDir))
                    .map(Path::toString)
                    .collect(Collectors.toList());

            for (final String dic : directories) {
                final Directory subDir = this.readDirectory(dic);
                if (subDir != null) {
                    dirCount += subDir.getCount();
                    resultDir.getDirectoryList().add(subDir);
                }
            }

            resultDir.setCount(dirCount);
            return resultDir;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getShotName(final String fullName) {
        if (root.equals(fullName)) {
            return "root";
        } else {
            final String pattern = Pattern.quote(System.getProperty("file.separator"));
            final String arr[] = fullName.split(pattern);
            return arr[arr.length - 1];
        }
    }

    private void readFile(final bean.File file) {
        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {

            final List<String> codeLines = stream.collect(Collectors.toList());

            int count = 0;
            boolean isBegan = false;

            for (String line : codeLines) {
                line = line.trim();
                if ("".equals(line)) {
                    continue;
                }
                if (isBegan) {
                    if (commentEnded(line)) {
                        line = line.substring(line.indexOf("*/") + 2).trim();//cut */
                        isBegan = false;
                        if ("".equals(line) || line.startsWith("//")) {
                            continue;
                        }
                    } else {
                        //continue long_comment
                        continue;
                    }
                }
                if (isCodeLine(line)) {
                    count++;
                }
                if (commentBegan(line)) {
                    isBegan = true;
                }
            }

            file.setCount(count);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // /*
    private boolean commentBegan(String line) {
        int index = line.indexOf("/*");
        if (index == NOT_FOUND) {
            return false;
        }
        int quoteStartIndex = line.indexOf("\"");
        if (quoteStartIndex != NOT_FOUND && quoteStartIndex < index) {
            while (quoteStartIndex > NOT_FOUND) {
                line = line.substring(quoteStartIndex + 1);
                int quoteEndIndex = line.indexOf("\"");
                line = line.substring(quoteEndIndex + 1);
                quoteStartIndex = line.indexOf("\"");
            }
            return commentBegan(line);
        }
        return !commentEnded(line.substring(index + 2));
    }

    // */
    private boolean commentEnded(String line) {
        int index = line.indexOf("*/");
        if (index == NOT_FOUND) {
            return false;
        } else {
            String subString = line.substring(index + 2).trim();//cut */
            if ("".equals(subString) || subString.startsWith("//")) {
                return true;
            }
            return !commentBegan(subString);
        }
    }

    private boolean isCodeLine(String line) {
        line = line.trim();
        if ("".equals(line) || line.startsWith("//")) {
            return false;
        }
        if (line.length() == 1) {
            return true;
        }
        int index = line.indexOf("/*");
        if (index != 0) {
            return true;
        } else {
            while (line.length() > 0) {
                line = line.substring(index + 2);
                int endCommentIndex = line.indexOf("*/");
                if (endCommentIndex < 0) {
                    return false;
                }
                if (endCommentIndex == line.length() - 2) {
                    return false;
                } else {
                    String subString = line.substring(endCommentIndex + 2).trim();
                    if ("".equals(subString) || subString.indexOf("//") == 0) {
                        return false;
                    } else {
                        if (subString.startsWith("/*")) {
                            line = subString;
                            continue;
                        }
                        return true;
                    }
                }

            }
        }
        return false;
    }

}
