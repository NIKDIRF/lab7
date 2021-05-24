package response;

import studygroup.Response;

public interface Creator {
    public Response createResponse(String message, boolean success, boolean routeRequired);

    public Response createResponse();

    public void addToMsg(String msg);
}
