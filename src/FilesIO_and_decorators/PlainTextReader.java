package FilesIO_and_decorators;

import java.io.BufferedReader;
import java.io.IOException;

public class PlainTextReader implements FileReader
{
    private String filePath;

    public PlainTextReader(String filePath)
    {
        this.filePath = filePath;
    }

    String getFilePath(){
        return filePath;
    }

    @Override
    public String read() throws IOException
    {
        StringBuilder content = new StringBuilder();
        try ( BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath)) )
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                content.append(line).append(" ");
            }
            return content.toString().trim();
        }
    }
}
