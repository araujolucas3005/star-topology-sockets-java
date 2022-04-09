package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerStarter {

  // hashtable para armazenar os clientes ativos
  protected static Map<Integer, Socket> clients = new HashMap<>();

  public static void main(String[] args) throws IOException {
    // cria o socket do servidor
    ServerSocket socketServidor = new ServerSocket(5050);

    // cria a thread responsável por lidar com mensagens broadcast do servidor
    Thread broadcastThread = new Thread(new ServerBroadcast());
    broadcastThread.start();

    while (true) {
      System.out.println("Aguardando conexão do cliente...");

      try {
        // aguarda algum cliente
        Socket client = socketServidor.accept();

        System.out.printf("Cliente %s:%d conectado\n", client.getInetAddress().getHostAddress(),
            client.getPort());

        // insere os cliente conectado na hashtable
        clients.put(client.getPort(), client);

        Server server = new Server(client);

        // thread para lidar com a conexão do cliente com o servidor
        Thread threadServer = new Thread(server);

        threadServer.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
