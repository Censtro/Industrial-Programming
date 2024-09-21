package FilesIO_and_decorators;


import java.io.IOException;

public abstract class FileReaderDecorator implements FileReader
{
    protected FileReader wrapee;

    public FileReaderDecorator(FileReader fileReader)
    {
        this.wrapee = fileReader;
    }

    @Override
    public String read() throws IOException
    {
        return wrapee.read();
    }
}
