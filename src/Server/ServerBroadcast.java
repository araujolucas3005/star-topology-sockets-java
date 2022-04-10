package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// classe responsável por enviar mensagens broadcast aos clientes
public class ServerBroadcast implements Runnable {

  private final Scanner scanner = new Scanner(System.in);

  @Override
  public void run() {
    while (true) {
      System.out.print("Envie uma mensagem para os clientes: ");
      String msg = scanner.nextLine();

      synchronized (ServerStarter.clients) {
        // envia a mensagem do servidor para cada cliente ativo
        ServerStarter.clients
            .forEach((port, client) -> {
              try {
                PrintStream os = new PrintStream(client.getOutputStream(), true);
                os.println(msg);
              } catch (IOException e) {
                System.out.println("Não foi possível enviar para o cliente da porta " + port);
              }
            });
      }
    }
  }
}
