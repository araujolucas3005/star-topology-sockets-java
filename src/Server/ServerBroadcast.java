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

      // lista para adicionar possíveis clientes desconectados
      List<Integer> deadClientsPorts = new ArrayList<>();

      // envia a mensagem do servidor para cada cliente ativo
      ServerStarter.clients
          .forEach((port, client) -> {
            try {
              PrintStream outputStream = new PrintStream(client.getOutputStream(), true);
              outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
              outputStream.write('\n');
              outputStream.flush();
            } catch (SocketException e) {
              deadClientsPorts.add(port);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });

      // remove possíveis clientes desconectados
      deadClientsPorts.forEach(port -> ServerStarter.clients.remove(port));
    }
  }
}
