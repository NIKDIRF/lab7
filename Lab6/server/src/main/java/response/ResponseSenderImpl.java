package response;

import locale.ServerBundle;
import log.Log;
import studygroup.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ResponseSenderImpl implements ResponseSender{
    private Selector selector;

    public void sendResponse(SocketChannel socketChannel, Response response) throws IOException{
        log.Log.getLogger().info(ServerBundle.getString("server.sending_response"));
        sendBytes(serializeResponse(response), socketChannel);
        log.Log.getLogger().info(ServerBundle.getString("server.response_sent"));
    }

    private void sendBytes(byte[] bytes, SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int numWritten = 1;
        try {
            while (byteBuffer.remaining() > 0) {
                numWritten = socketChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            socketChannel.close();
        }
    }

    private byte[] serializeResponse(Response response) throws IOException{
        Log.getLogger().info(ServerBundle.getString("server.serializing_response"));
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteStream);
        stream.writeObject(response);
        return byteStream.toByteArray();
    }
}
