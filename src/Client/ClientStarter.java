package Client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class ClientStarter {

  public static void main(String[] args) throws IOException {
    // cria o socket com o servidor
    try {
      Socket socket = new Socket("127.0.0.1", 5050);

      System.out.println("MINHA PORTA: " + socket.getLocalPort());

      // para lidar com o envio de mensagens do cliente
      ClientSender clientSender = new ClientSender(socket);

      // para lidar com o recebido de mensagens do cliente
      ClientReceiver clientReceiver = new ClientReceiver(socket);

      Thread senderThread = new Thread(clientSender);
      senderThread.start();

      Thread receiverThread = new Thread(clientReceiver);
      receiverThread.start();
    } catch (ConnectException e) {
      System.out.println("Servidor indisponível no momento. Tente mais tarde.");
    }
  }
}