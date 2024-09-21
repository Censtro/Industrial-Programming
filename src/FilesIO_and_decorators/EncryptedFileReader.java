package FilesIO_and_decorators;

import java.io.IOException;
import java.util.Base64;

public class EncryptedFileReader extends FileReaderDecorator
{

    public EncryptedFileReader(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    public String read() throws IOException {
        return decrypt(super.read());
    }

    private String decrypt(String data)
    {
        return new String(Base64.getDecoder().decode(data));
    }
}