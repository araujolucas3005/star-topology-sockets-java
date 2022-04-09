package Client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import utils.NumberUtils;

// classe responsável por enviar mensagens, que irão passar pelo servidor, aos outros clientes
public class ClientSender implements Runnable {

  private final PrintStream os;

  public ClientSender(Socket serverSocket) throws IOException {
    os = new PrintStream(serverSocket.getOutputStream(), true);
  }

  @Override
  public void run() {
    Scanner scanner = new Scanner(System.in);
    String msg;
    String clientReceiverPort;

    while (true) {
      System.out.print("Digite a porta do destinatário: ");

      // recebe a porta do destinatário
      // apenas números são aceitos
      clientReceiverPort = scanner.nextLine();
      while (!NumberUtils.isDigit(clientReceiverPort)) {
        System.out.print(
            "Apenas números são permitidos! Digite novamente a porta do destinatário: ");
        clientReceiverPort = scanner.nextLine();
      }

      System.out.print("Agora digite a mensagem: ");
      // lê a mensagem que será enviada para o destinatário
      // não pode ser em branco
      msg = scanner.nextLine();
      while (msg.isEmpty()) {
        System.out.print("Mensagens em branco não podem ser enviadas! Digite uma mensagem válida: ");
        msg = scanner.nextLine();
      }

      try {
        // envia uma mensagem para o servidor, que é formada pela porta do destinatário e pela mensagem escrita
        os.printf("%s@%s\n", clientReceiverPort, msg);
        os.flush();
      } catch (NoSuchElementException e) {
        // servidor não está mais ativo
        break;
      }

      // printa na tela que está enviando a mensagem
      System.out.printf(
          "Enviando mensagem para o cliente na porta %d > %s\n",
          Integer.parseInt(clientReceiverPort), msg);
    }

    os.close();
  }
}
