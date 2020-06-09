import bean.Directory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class BaseTest {
    static Reader reader;
    static Writer writer;
    final static File resourcesDirectory
            = new File("src" + File.separator + "test" + File.separator + "resources");

    @BeforeClass
    public static void setupClass() {
        reader = new Reader(resourcesDirectory.getAbsolutePath());
        writer = new Writer();
    }

    @Test
    public void testRead() {
        final Directory directory = reader.read();
        Assert.assertEquals("root", directory.getName());
    }

    @Test
    public void testWrite() {
        final Directory directory = new Directory();
        final String name = "dictionary";
        directory.setName(name);
        directory.setCount(10);

        final StringBuilder builder = writer.getTextForWrite(directory);
        System.out.println(builder);
        Assert.assertEquals("dictionary:10\n", builder.toString());
    }
}
