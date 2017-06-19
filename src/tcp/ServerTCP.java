package tcp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import udp.Tauler;


class ServerTCP{
	
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;
    
    public  ArrayList<ClientTCP> clientes;
	static int clients;
    DatagramSocket socket;
    ArrayList<Integer> ports;
    static int n;
    static String s;
    public Tauler tauler;
    public Jugada jugada;
    
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
        clients=1;
        ports= new ArrayList<Integer>();
        tauler = new Tauler();
        clientes = new ArrayList<>();
    }
    
    public void runServer() throws IOException, ClassNotFoundException, InterruptedException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        
        while(clients!=0) {
        	//creo un paquete con la informacion que recibo del cliente
            DatagramPacket packet = new DatagramPacket(receivingData,1024);
            socket.receive(packet);
           
            sendingData = processData(packet.getData(),packet.getLength());
            
            //Llegim el port i l'adreça del client per on se li ha d'enviar la resposta
            clientIP = packet.getAddress();
            clientPort = packet.getPort();                  
          //  comprobarPort(clientPort);
            DatagramPacket send = new DatagramPacket(sendingData,sendingData.length, clientIP,clientPort);
            socket.send(send);
            
            this.sendMultiTaulers();
            
        }
    }
    
	public static void main(String argv[]) throws Exception{
		
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(8888);
		
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient =
			new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Recibido: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}
	}
}