package Model;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;
import java.lang.Exception;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrgab
 */
public class ThreadClienteUDP extends Thread{
private void imprimeMenu(){
    System.out.println("Menu do GGMtorrent");
    System.out.println("1 - Listar Usuarios da Rede");
    System.out.println("2 - Listar Arquivos da Rede");
    System.out.println("3 - Procurar Arquivo");
    System.out.println("7 - Buscar Arquivos");
}
private int leInteiro(){
    Scanner ler = new Scanner(System.in);
    return ler.nextInt();
}
private String leString() {
    Scanner ler = new Scanner(System.in);
    return ler.nextLine();
}
private boolean contemArquivo(String nomeDoArquivo){
    LinkedList listaDeArquivos = carregaArquivos();
        int i;
        for(i = 0; i < listaDeArquivos.size(); i++){
            File arquivos = (File) listaDeArquivos.get(i);
            if (nomeDoArquivo.equalsIgnoreCase(arquivos.getName())){
                return true;
            }// fim if
        }// fim for    
    return false;
}
private LinkedList carregaArquivos(){
    LinkedList listaDeArquivos;
    File diretorioCorrente = new File("Pasta Compartilhada");
//    File diretorioCorrente = new File("");
    listaDeArquivos = new LinkedList<File>();
    File[] lista = diretorioCorrente.listFiles();
    for(File a : lista) {
        listaDeArquivos.add(a);
    }
    return listaDeArquivos;
}




public void run(){
while(true){
    imprimeMenu();
    ByteArrayOutputStream b;
    ObjectOutputStream o;
    DatagramPacket pacote;
    DatagramSocket socket;
    int codigoRequisicao = leInteiro();
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    InetAddress endereco;
    byte[] buffer;
    try{
        b = new ByteArrayOutputStream();
        o = new ObjectOutputStream(b);
        o.flush();
        o.writeObject(codigoRequisicao);
        if(codigoRequisicao == 3){
                String nomeDoArquivo = leString();
//                    if(contemArquivo(nomeDoArquivo)) throw new Exception("Esse arquivo já esta em seu diretorio");
                o.writeObject(nomeDoArquivo);
        }
            if (codigoRequisicao == 7){
                String nomeDoArquivo = leString();
                String ip = leString();
                endereco = InetAddress.getByName(ip);
                new ThreadClienteTCP(nomeDoArquivo, endereco).start();
            }else {
            pacote = new  DatagramPacket(b.toByteArray(), b.toByteArray().length, InetAddress.getByName("255.255.255.255"), 12345);
//            System.out.println("Criei o pacote do cliente");
            socket = new DatagramSocket();
//            System.out.println("Criei o socket do cliente");
            socket.send(pacote);
//            System.out.println("Enviei o pacote do cliente");
            socket.close();
//            System.out.println("Fechei o socket cliente");
            pacote = null;
            b = null;
            o = null;
            }
        }
    catch(Exception bola){
        bola.printStackTrace();
    }
}
}// fim run


   





}