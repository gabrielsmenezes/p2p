package Model;




import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrgab
 */
public class ThreadServidorUDP extends Thread{
    private LinkedList listaDeArquivos;
    public void run(){
        DatagramSocket socket;
        DatagramPacket pacote;
        byte[] buffer;
        try{
            
            while(true){
                buffer = new byte[4096];
                pacote = new DatagramPacket(buffer, buffer.length);
                socket = new DatagramSocket(12345);
                socket.setBroadcast(true);
                socket.receive(pacote);
                socket.close();
                new ThreadTratarRequisicao(pacote).start();
            }
            
//            System.out.println("Criei o pacote servidor");
//            System.out.println("Esperando pacote...");
//            System.out.println("Recebi o pacote do servidor");
//            System.out.println("Criei  o bIn");            
//            System.out.println("Fechei o socket do servidor");            
//            System.out.println("Criei o oIn");            




//            } if(codigoRequisicao == 2){
//                carregaArquivos();

//            } else if (codigoRequisicao == 3){
//                
//            }
            
        }catch (Exception e){ e.printStackTrace();}
        
}// fim run

    
}// fim class