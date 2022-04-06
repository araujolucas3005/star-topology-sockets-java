package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

// classe responsável por receber as mensagens enviadas do servidor
public class ClientReceiver implements Runnable {

  private final Scanner in;

  public ClientReceiver(Socket serverSocket) throws IOException {
    this.in = new Scanner(serverSocket.getInputStream());
  }

  @Override
  public void run() {
    String msg;

    while (true) {
      try {
        // aguarda alguma mensagem do servidor
        msg = in.nextLine();
      } catch (NoSuchElementException e) {
        // servidor não está mais ativo
        break;
      }

      // printa na tela a mensagem enviada pelo servidor
      System.out.printf("SERVIDOR: %s\n", msg);
    }

    // saiu do while, então o servidor não está mais ativo
    System.out.println("CONEXÃO COM O SERVIDOR TERMINADA");
    System.out.println("MENSAGENS NÃO SERÃO MAIS ENVIADAS E NEM RECEBIDAS");
    in.close();
  }
}
