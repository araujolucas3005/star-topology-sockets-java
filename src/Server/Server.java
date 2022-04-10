package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

// classe para lidar com a conexão do cliente
public class Server implements Runnable {

  private final Socket clientSocket;

  public Server(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    try {
      Scanner is = new Scanner(clientSocket.getInputStream());
      String msg;
      String receiverPort;
      while (true) {
        try {
          // tenta ler a mensagem enviada de algum cliente para outro
          // mensagem que será encaminhada para o destinatário
          msg = is.nextLine();
        } catch (NoSuchElementException e) {
          // conexão com o cliente foi terminada
          break;
        }

        // PORT@msg
        String[] data = msg.split("@", 2);
        receiverPort = data[0];
        msg = data[1];

        // procura o cliente pela porta na hastable de clientes ativos
        Socket receiver = ServerStarter.clients.get(Integer.parseInt(receiverPort));

        // se o cliente não tiver na hashtable de ativos
        if (receiver == null) {
          // pega o cliente remetente da mensagem
          Socket sender = ServerStarter.clients.get(clientSocket.getPort());
          PrintStream out = new PrintStream(sender.getOutputStream());

          // informa para o remetente que o destinário não existe
          out.println("cliente destinatário não existente");
          continue;
        }

        // se existir o destinatário, envia a mensagem
        PrintStream out = new PrintStream(receiver.getOutputStream());
        out.printf("%s:%s te mandou a mensagem > %s\n",
            clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort(), msg);
      }

      // saiu do while, então o cliente foi desconectado
      System.out.printf("Cliente %s:%d desconectado\n",
          clientSocket.getInetAddress().getHostAddress(),
          clientSocket.getPort());

      is.close();
      clientSocket.close();
    } catch (IOException e) {
      System.out.printf("Cliente %s:%d foi desconectado",
          clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
    } finally {
      // remove o cliente desconectado da hashtable de clientes ativos
      ServerStarter.clients.remove(clientSocket.getPort());
    }
  }
}
