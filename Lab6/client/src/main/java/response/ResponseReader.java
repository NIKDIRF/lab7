package response;

import studygroup.Response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ResponseReader {
    Response getResponse(SocketChannel socketChannel) throws IOException, ClassNotFoundException;
}
