import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    private static final List<String> equationHistory = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            System.out.println("Started Run");

            try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    equationHistory.add(inputLine);

                    System.out.println("Received equation: " + inputLine);
                    printEquationSummary();
                }

                System.out.println("Finished Run");
            } catch (IOException e) {
                System.err.println("Client handling error: " + e.getMessage());
            }
        }

        private void printEquationSummary() {
            System.out.println("\nCurrent Equation History (" + equationHistory.size() + " entries):");
                equationHistory.forEach(System.out::println);
                System.out.println();
        }
    }
}