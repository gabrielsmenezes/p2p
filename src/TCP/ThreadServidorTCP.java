package TCP;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrgab
 */
// o Servidor TCP vai transferir arquivo com o cliente
public class ThreadServidorTCP extends Thread{    
    public void run(){
        try {
            ServerSocket servidor = new ServerSocket(12345);
            Socket cliente;

            while(true){
                cliente = servidor.accept();
                new ThreadTransferirArquivo(cliente).start();
            }
        }catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    
    
    }
    
}