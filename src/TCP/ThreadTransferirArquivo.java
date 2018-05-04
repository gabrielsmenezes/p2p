/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author mrgab
 */
public class ThreadTransferirArquivo extends Thread{
    private Socket cliente; 
    public ThreadTransferirArquivo(Socket cliente) {
        this.cliente = cliente;
    }
    public void run(){
        try{
            System.out.println("Servidor conectado");
            ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( cliente.getInputStream() );
            String nomeDoArquivo = (String) in.readObject();
            System.out.println("Nome do arquivo recebido");
            File arquivo = new File("Pasta Compartilhada//" + nomeDoArquivo);
            out.writeObject(arquivo.getUsableSpace());
            System.out.println("Tamanho do arquivo enviado");
            FileInputStream file = new FileInputStream(arquivo);
            byte[] buffer = new byte[(int) arquivo.length()];
            file.read(buffer, 0, buffer.length);
            out.flush();
            out.writeObject(buffer);
            cliente.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
}
