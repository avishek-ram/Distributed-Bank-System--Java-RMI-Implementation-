
package com.assignment.bank;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
 
public class Main extends Policy{
     
    private void startServer(){ 
//        this method is responsible to register the remote 
//        objects with the registry so that they can be looked up by the clients.
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Bank", new BankImpl());
            System.out.println("system is ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    public static void main(String[] args) throws RemoteException {
        Main main = new Main();
        System.setProperty("java.rmi.server.hostname","127.0.0.1");// sets the RMI service to start on local host
        main.startServer();
    }
}
