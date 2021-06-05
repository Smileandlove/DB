import DBException.QueryErrorException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class DBServer
{
    public DBServer(int portNumber)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Interpreter interpreter = new Interpreter();
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter,interpreter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter,
                                    Interpreter interpreter) throws IOException, NullPointerException
    {
        String incomingCommand = socketReader.readLine();
        String message = null;
        try {
            message = interpreter.preformQuery(incomingCommand);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (QueryErrorException e) {
            e.printStackTrace();
            message=e.toString();
        }
        System.out.println("Received message: " + incomingCommand);
        //socketWriter.write("[OK] Thanks for your message: " + incomingCommand);
        socketWriter.write(message+"\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }


}
