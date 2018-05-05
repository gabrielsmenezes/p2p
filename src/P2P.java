


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
   
    public static void main(String[] args)throws Exception{
            criaPasta();
            ThreadServidorUDP servidor = new ThreadServidorUDP();
            servidor.start();
            ThreadClienteUDP cliente = new ThreadClienteUDP();
            cliente.start();
            new ThreadServidorTCP().start();
    }

    private static void criaPasta() {
        File diretorioCorrente = new File("Pasta Compartilhada");
        if(!diretorioCorrente.exists()) diretorioCorrente.mkdir();
        else System.out.println("Já esta criada");
    }
}