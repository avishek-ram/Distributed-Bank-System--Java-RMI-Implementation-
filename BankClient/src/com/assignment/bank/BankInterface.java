
package com.assignment.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;
 
public interface BankInterface extends Remote 
{
    public boolean checkAccNum(int acnt) throws RemoteException;
    public boolean checkAccStatus(int acnt) throws RemoteException;
    public boolean checkAccPin(int acnt, int pin) throws RemoteException;
    public void lockAcc(int acnt) throws RemoteException;
    
    public void deposit(int acnt, int amt) throws RemoteException;
    public void withdraw(int acnt, int amt) throws RemoteException;
    public int checkBalance(int acnt) throws RemoteException;
    public boolean checkOverdraft(int acnt, int amt) throws RemoteException;
    public void getTransactions(int acnt, String startDate, String endDate, String emailAddr) throws RemoteException;
    public String makePayment(int acnt, int amt, int payAccNumber, String payVendor) throws RemoteException;
}
