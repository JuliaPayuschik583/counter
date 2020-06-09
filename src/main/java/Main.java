import bean.Directory;

public class Main {

    private final static Reader reader = new Reader();
    private final static Writer writer = new Writer();


    public static void main(String[] args) {

        final Directory result = reader.read();

        writer.write(result);
    }
}
