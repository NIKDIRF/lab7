package response;

import studygroup.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseReaderImpl implements ResponseReader{

    public Response getResponse(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
        byteBuffer.clear();
        int numRead;
        do {
            numRead = socketChannel.read(byteBuffer);
        } while ((numRead == 0));
        return deserializeRequest(byteBuffer.array());
    }

    private Response deserializeRequest(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream stream = new ObjectInputStream(byteStream);
        return (Response) stream.readObject();
    }
}
