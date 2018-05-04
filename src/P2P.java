


import UDP.Servidor.ThreadServidorUDP;
import UDP.Cliente.ThreadClienteUDP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrgab
 */
public class P2P {
    public static Iterable listaUsuarios(){
        return null;
    }
    public static Iterable listaArquivos(){
        return null;
    }
    public static boolean enviarArquivo(String ip_destino){
        try{    
            Socket cliente = new Socket (ip_destino, 12345);
            System.out.println("Cliene conectado ao servidor");
            ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
            FileInputStream file = new FileInputStream("C:\\Users\\mrgab\\Google Drive Pessoal\\Faculdade\\Quinto Semestre\\Redes\\Trabalho1\\P2P\\test\\texto.txt");
            byte[] buffer = new byte[4096];
            int len = file.read(buffer);
            while(len != -1){
                out.write(buffer, 0, len);
                len = file.read(buffer);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean receberArquivo(){
       try{
           ServerSocket ss = new ServerSocket(12345);
           Socket s = ss.accept();
           System.out.println("Vai criar o objectinputstream");

           ObjectInputStream in = new ObjectInputStream(s.getInputStream());
           System.out.println("Criouuuuuuuuu");

           FileOutputStream file = new FileOutputStream("C:\\Users\\mrgab\\Google Drive Pessoal\\Faculdade\\Quinto Semestre\\Redes\\Trabalho1\\P2P\\test\\texto2.txt");
           System.out.println("Cria o FileOutputStream");
           byte[] buffer = new byte[4096];
           int len = in.read(buffer);
           System.out.println("Le o primeiro byte do inputStream");
            while(len != -1){
                file.write(buffer, 0, len);
                System.out.println("Le um byte do input, retam:" + len);
                len = in.read(buffer);
            }
       }catch(Exception e){
           e.printStackTrace();
           return false;
       }
       return true;
    }
    public static void main(String[] args)throws Exception{
            criaPasta();
            ThreadServidorUDP servidor = new ThreadServidorUDP();
            servidor.start();
            ThreadClienteUDP cliente = new ThreadClienteUDP();
            cliente.start();
            new TCP.ThreadServidorTCP().start();
    }

    private static void criaPasta() {
        File diretorioCorrente = new File("Pasta Compartilhada");
        if(!diretorioCorrente.exists()) diretorioCorrente.mkdir();
        else System.out.println("Já esta craida");
    }
}
