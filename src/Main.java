import FilesIO_and_decorators.*;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        FileReader PlainTextReader = new PlainTextReader("input.txt");
        try
        {
            System.out.println(PlainTextReader.read());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

    }
}
