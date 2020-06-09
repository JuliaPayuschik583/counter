import bean.Directory;
import bean.File;

public class Writer {

    void write(final Directory directory) {
        final StringBuilder builder = this.getTextForWrite(directory);
        System.out.println(builder);
    }

    StringBuilder getTextForWrite(final Directory directory) {
        final StringBuilder builder;
        if (directory != null) {
            builder = new StringBuilder();
            builder.append(directory.getName()).append(":").append(directory.getCount()).append("\n");
            this.writeDirectory(directory, builder, 0);
        } else {
            builder = null;
        }
        return builder;
    }

    private void writeDirectory(final Directory directory, final StringBuilder builder, int innerCount) {
        innerCount += 1;
        for (final File file : directory.getFileList()) {
            for (int i = 0; i < innerCount; i ++) {
                builder.append("    ");
            }
            builder.append(file.getName()).append(":").append(file.getCount()).append("\n");
        }
        for (final Directory dic : directory.getDirectoryList()) {
            for (int i = 0; i < innerCount; i ++) {
                builder.append("    ");
            }
            builder.append(dic.getName()).append(":").append(dic.getCount()).append("\n");
            if (!dic.getDirectoryList().isEmpty() || !dic.getFileList().isEmpty()) {
                this.writeDirectory(dic, builder, innerCount);
            }
        }
    }
}
