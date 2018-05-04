/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP.Servidor;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 *
 * @author mrgab
 */
public class ThreadTratarRequisicao extends Thread{
    private DatagramPacket pacoteRecebido;
    public ThreadTratarRequisicao(DatagramPacket pacoteRecebido) {
        this.pacoteRecebido = pacoteRecebido;
    }
    private void salvaLog( InetAddress ip, int tipodesolicitacao) throws IOException{
        String[] tipos = {"Listar Usuarios", "Listar Arquivos", "Procurar Arquivo", "", "", "", "Buscar Arquivo"};
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        FileWriter log = new FileWriter("Arquivo de Log.txt", true);
        BufferedWriter bf = new BufferedWriter(log);
        String ipString = ip.getHostAddress();
        bf.write(ipString);
        bf.write(" ");
        bf.write(tipos[tipodesolicitacao - 1]);
        bf.write(" ");
        bf.write(df.format(dateobj));
        bf.newLine();
        bf.close();
        log.close();
    }
    private int contemArquivo(String nomeDoArquivo, LinkedList listaDeArquivos){
        for(int i = 0; i < listaDeArquivos.size(); i++){
            File a = (File) listaDeArquivos.get(i);
            if(a.getName().equalsIgnoreCase(nomeDoArquivo)) return i;
        }
        return -1;
    }
    private LinkedList carregaArquivos(){
        LinkedList listaDeArquivos;
        File diretorioCorrente = new File("Pasta Compartilhada");
//        File diretorioCorrente = new File("").getAbsoluteFile();
        listaDeArquivos = new LinkedList<File>();
        File[] lista = diretorioCorrente.listFiles();
        for(File a : lista) {
            listaDeArquivos.add(a);
        }
        return listaDeArquivos;
    }
    public void run(){
        byte[] bufferIn, bufferOut;
        ByteArrayInputStream arrayIn;
        ObjectInputStream in;
        ByteArrayOutputStream arrayOut;
        ObjectOutputStream out;
        int codigoRequisicao;
        InetAddress endereco;
        DatagramPacket pacoteEnviar;
        DatagramSocket socket;
        Socket socketTCP;
        try{
            bufferIn = pacoteRecebido.getData();
            arrayIn = new ByteArrayInputStream(bufferIn);
            in = new ObjectInputStream(arrayIn);
            codigoRequisicao = (int) in.readObject();
            arrayOut = new ByteArrayOutputStream();    
            out = new ObjectOutputStream(arrayOut);
            LinkedList listaDeArquivos;
            String nomeDoArquivo;
            System.out.println(codigoRequisicao);
            switch(codigoRequisicao){
                case(1):
                    salvaLog(pacoteRecebido.getAddress(), 1);

//                    endereco = InetAddress.getByName(getIp());
                    out.writeObject(4);
//                    out.writeObject(endereco);
                    pacoteEnviar = new DatagramPacket(arrayOut.toByteArray(), arrayOut.toByteArray().length, pacoteRecebido.getAddress(), 12345);
                    socket = new DatagramSocket();
                    socket.send(pacoteEnviar);
                    socket.close();
                    break;
                case (2):
                    salvaLog(pacoteRecebido.getAddress(), 2);
                    listaDeArquivos = carregaArquivos();
                    out.writeObject(5);
                    out.writeObject(listaDeArquivos);
                    pacoteEnviar = new DatagramPacket(arrayOut.toByteArray(), arrayOut.toByteArray().length, pacoteRecebido.getAddress(), 12345);
                    socket = new DatagramSocket();
                    socket.send(pacoteEnviar);
                    socket.close();
                    break;
                case(3):
                    System.out.println("Entrou no caso 3");
                    salvaLog(pacoteRecebido.getAddress(), 3);
                    listaDeArquivos = carregaArquivos();
                    nomeDoArquivo = (String) in.readObject();
                    int tamanho = contemArquivo(nomeDoArquivo, listaDeArquivos);
                    if (tamanho > -1){
                        File a = (File) listaDeArquivos.get(tamanho);
                        out.writeObject(6);
                        out.writeObject(a.length());
                    } else {
                        out.writeObject(6);
                        out.writeObject( (long) -1);
                    }
                        pacoteEnviar = new DatagramPacket(arrayOut.toByteArray(), arrayOut.toByteArray().length, pacoteRecebido.getAddress(), 12345);
                        socket = new DatagramSocket();
                        socket.send(pacoteEnviar);
                        socket.close();
                    
                    break;
                case(4):
//                    endereco = (InetAddress) in.readObject();
                    endereco = pacoteRecebido.getAddress();
                    System.out.println("**** Lista de Usuarios ****");
                    System.out.println( endereco.getHostAddress() );
                    break;
                case(5):
                    listaDeArquivos = (LinkedList) in.readObject();
                    System.out.println("**** Lista de Arquivos ****");
                    for(int i = 0; i < listaDeArquivos.size(); i++){
                        File a = (File) listaDeArquivos.get(i);
                        System.out.println(pacoteRecebido.getAddress().getHostAddress() + " " + a.getName());                    
                    }
                    break;
                case(6):
                    System.out.println("Entrou no caso 3");
                    long tamanhoDoArquivo = (long) in.readObject();
                    System.out.println("Tamanho do arquivo: " + tamanhoDoArquivo);
                    if (tamanhoDoArquivo < 0){
                        System.out.println("Arquivo nao encontrado");
                    } else System.out.println ("Tamanho do arquivo: " + tamanhoDoArquivo + " IP: " + pacoteRecebido.getAddress().getHostAddress());
                    break;
                case(7):
                    salvaLog(pacoteRecebido.getAddress(), 7);
                    endereco = (InetAddress) in.readObject();
                    System.out.println(endereco.getHostAddress());
                    nomeDoArquivo = (String) in.readObject();
                    System.out.println(nomeDoArquivo);
                    String[] divisao = nomeDoArquivo.split("\\.");
//                    System.out.println("Tamanho do divisao: " + divisao.length);
                    
                    
                    
                    socketTCP = new Socket(endereco, 12345);
                    System.out.println("Cliente conectado");
                    in = new ObjectInputStream(socketTCP.getInputStream());
                    out = new ObjectOutputStream(socketTCP.getOutputStream());
                    out.writeObject(nomeDoArquivo);
                    tamanhoDoArquivo = (long) in.readObject();
                    System.out.println("Tamanho do arquivo recebido");
                    
                    String extensao = divisao[divisao.length - 1];
                    FileOutputStream file = new FileOutputStream("Pasta Compartilhada//novoarquivo."+extensao);
                    bufferIn =  (byte[]) in.readObject();
                    file.write(bufferIn);
                    file.close();
                    socketTCP.close();
                    break;
            }/// fim switch
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
        }catch (Exception ball){
            ball.printStackTrace();
        }
    }/// fim run

    private String getIp() throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();
        int i = 0;
        while(en.hasMoreElements()){
            NetworkInterface ni=(NetworkInterface) en.nextElement();
            Enumeration ee = ni.getInetAddresses();
            while(ee.hasMoreElements()) {
            InetAddress ia= (InetAddress) ee.nextElement();
            i++;
            if(i == 6) return ia.getHostAddress();
            }
        }
        return null;
    }
}/// fim class
