import java.net.*;
import java.io.*;

public class Server {

    // user input
    static private BufferedReader inputReader;
    static private BufferedWriter outputWriter;

    // file input
    static private BufferedReader userReader;
    static private BufferedWriter userWriter;
    static private BufferedReader passReader;
    static private BufferedWriter passWriter;

    boolean connected;

    public void startServer() throws IOException {

        // connect to client
        ServerSocket serverSocket = new ServerSocket(6000);
        System.out.println("Awaiting connection from client on port 6000");
        Socket client = serverSocket.accept();
        System.out.println("Connection with client accepted\nAwaiting Command...");

        // initialise readers/writers
        inputReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outputWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        userReader = new BufferedReader(new FileReader("src/usernames.txt"));
        passReader = new BufferedReader(new FileReader("src/passwords.txt"));
        userWriter = new BufferedWriter(new FileWriter("src/usernames.txt", true));
        passWriter = new BufferedWriter(new FileWriter("src/passwords.txt", true));

        ServerThread st = new ServerThread(inputReader, outputWriter);
        st.start();
    }

    public static void runServer() {
        try {
            // prompt client for 1st command
            outputWriter.write("Command 'r' to register, 'l' to login:\n");
            outputWriter.flush();

            // receive input from client
            String command = inputReader.readLine();
            System.out.println("Command: '" + command + "' received from client");

            switch (command) {
                case "r":
                    addUser();
                    break;
                case "l":
                    login();
                    break;
                default:
                    outputWriter.write("Command not recognised\n");
                    outputWriter.flush();
            }
        } catch (Exception e) {}
    }

    public static void login() {
        try {
            // prompt for username
            outputWriter.write("Enter your username\n");
            outputWriter.flush();
            String username = inputReader.readLine();
            System.out.println("Client username: " + username);

            // prompt for password
            outputWriter.write("Enter your password\n");
            outputWriter.flush();
            String password = inputReader.readLine();
            System.out.println("Client password: " + password);

            boolean authenticate = false;
            String uCurrentLine;
            String pCurrentLine;

            // is client registered?
            while ((uCurrentLine = userReader.readLine()) != null && (pCurrentLine = passReader.readLine()) != null) {
                if (username.equals(uCurrentLine) && password.equals(pCurrentLine)) {
                    authenticate = true;
                }
            }

            if (authenticate) {
                outputWriter.write("You have successfully logged in!" + "\n");
                outputWriter.flush();
            } else {
                System.out.println("No user found with these credentials");
                outputWriter.write("Invalid username and/or password");
                outputWriter.flush();
            }
            //outputWriter.close();

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public static void addUser() {
        try {
            // prompt for username
            outputWriter.write("Enter your username\n");
            outputWriter.flush();
            String username = inputReader.readLine();
            System.out.println("Client username: " + username);

            // prompt for password1
            outputWriter.write("Enter your password\n");
            outputWriter.flush();
            String password1 = inputReader.readLine();
            System.out.println("Client password1: " + password1);

            // FIXME detect if password1 and password2 are the same (provide client end)
//            // prompt for password2
//            outputWriter.write("Re-enter your password\n");
//            outputWriter.flush();
//            String password2 = inputReader.readLine();
//            System.out.println("Client password2: " + password2);

            boolean authenticate = false;
            String uCurrentLine;

            // is client already registered?
            while ((uCurrentLine = userReader.readLine()) != null) {
                if (username.equals(uCurrentLine)) {
                    // user is already registered
                    outputWriter.write("A user is already registered with these credentials!\n");
                    outputWriter.flush();
                    // FIXME Rhys help
                    return;
                } else {
                    authenticate = true;
                }
            }

//            } else if (!password1.equals(password2)) {
//                // passwords do not match
//                outputWriter.write("Passwords must match!");
//                outputWriter.flush();
//            }
            /*if (password1.equals(password2)) {*/
            if (authenticate) {
                //enter credentials into user lists
                userWriter.write("\n" + username);
                userWriter.flush();
                passWriter.write("\n" + password1);
                passWriter.flush();
                // FIXME The below does not display
                outputWriter.write("Registration successful!\n");
                outputWriter.flush();
                login();
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    private class ServerThread extends Thread {

        BufferedReader inputStream;
        BufferedWriter outputStream;

        public ServerThread(BufferedReader in, BufferedWriter out) {
            inputStream = in;
            outputStream = out;
        }

        public void run() {
            while(true)
                runServer();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.startServer();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}