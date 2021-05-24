package request;

import studygroup.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface RequestSender {
    void sendRequest(SocketChannel socketChannel, Request request) throws IOException;
}
