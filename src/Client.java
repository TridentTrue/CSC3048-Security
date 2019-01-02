import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private BufferedReader inputReader;
    private BufferedWriter outputWriter;
    private Scanner scanner = new Scanner(System.in);

    public void setUpConnections() {
        try {
            //Create socket connection
            Socket socket = new Socket(InetAddress.getLocalHost(), 6000);
            // initialise reader/writer
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            ClientThread clientThread = new ClientThread(inputReader);
            clientThread.start();
        }
        catch (Exception e) {}

    }

    public void sendLogIn() {
        try {
            // receive command prompt from server
            System.out.println("SERVER: " + inputReader.readLine());
            // send the command to the server
            outputWriter.write(scanner.nextLine() + "\n");
            outputWriter.flush();

            // Get username prompt from the server
            System.out.println("SERVER: " + inputReader.readLine());
            // send username to the server
            outputWriter.write(scanner.nextLine() + "\n");
            outputWriter.flush();

            // Get password prompt from the server
            System.out.println("SERVER: " + inputReader.readLine());
            // send password to the server
            outputWriter.write(scanner.nextLine() + "\n");
            outputWriter.flush();

            // Get response from the server
            System.out.println("SERVER: " + inputReader.readLine());
        } catch (UnknownHostException e) {
            System.out.println(e);
            System.exit(1);
        } catch (IOException e){
            System.out.println(e);
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        Client client = new Client();
        client.setUpConnections();
    }

    private class ClientThread extends Thread {

        BufferedReader input;

        public ClientThread(BufferedReader in) {
            input = in;
        }

        public void run() {
            try {
                while(true)
                    sendLogIn();
            } catch (Exception e) {

            }
        }
    }
}