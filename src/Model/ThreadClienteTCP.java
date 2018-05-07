package Model;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
public class ThreadClienteTCP extends Thread{
    private String nomeDoArquivo;
    private InetAddress endereco;

    public ThreadClienteTCP(String nomeDoArquivo, InetAddress endereco) {
        this.nomeDoArquivo = nomeDoArquivo;
        this.endereco = endereco;
    }
    
    public void run(){
        Socket socketTCP;
        ObjectInputStream in;
        ObjectOutputStream out;
        long tamanhoDoArquivo;
        byte[] bufferIn;
        try{
            String[] divisao = nomeDoArquivo.split("\\.");
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
            File a = new File("Pasta Compartilhada//novoarquivo." + extensao);
            if(a.length() > 0) System.out.println("arquivo recebido");
            else {
                System.out.println("Nos desculpe o arquivo foi dar um passeio na rede e nao chegou, por favor tente novamente");
                a.delete();
            }
            a = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
