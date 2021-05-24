package response;

import studygroup.Response;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public interface ResponseSender {
    void sendResponse(SocketChannel channel, Response response) throws IOException, ClassNotFoundException;
}
