package server;

import log.Log;

public class Server {
    public static void main(String[] args) {
        if (args.length == 5) {
            Log.getLogger().info("Hello, world!");
            Application application = new Application();
            try {
                application.start(args[0], Integer.parseInt(args[1]), args[2], args[3], args[4]);
            } catch (NumberFormatException e) {
                Log.getLogger().error("Invalid port");
            }
        } else {
            Log.getLogger().error("Expected arguments: port url login password");
        }
    }
}
