package request;

import studygroup.Request;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public interface RequestReader {
    Request getRequest(SelectionKey key) throws IOException, ClassNotFoundException;
}
