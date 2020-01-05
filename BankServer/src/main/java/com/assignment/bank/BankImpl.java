
package com.assignment.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.isNull;
import org.hibernate.*;
import static java.time.LocalDateTime.parse;
import java.util.*;  
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;  
 
public class BankImpl extends UnicastRemoteObject implements BankInterface {
 
    public BankImpl() throws RemoteException {}
    
    org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();

    @Override
    /**
        * @desc checks if the account number given by the user exists in the database
        * @param int acnt - the account number to be checked
        * @return bool - account number exists or does not exist
    */
    public boolean checkAccNum(int acnt) {
        Accounts account = (Accounts) session.get( Accounts.class, acnt);
        boolean bool = false;
        if (isNull(account))
            bool = false;
        else
            bool = true;
        return bool;
    }

    @Override
    /**
        * @desc checks the status of the account
        * @param int acnt - the account number to be checked
        * @return bool - active or locked
    */
    public boolean checkAccStatus(int acnt) {
        Accounts account = (Accounts) session.get( Accounts.class, acnt);
        boolean bool = false;
        int accStatus = 1;
        if (account.getAcc_status() == accStatus)
            bool = true;
        else
            bool = false;
        return bool;
    }

    @Override
    /**
        * @desc verifies the pin number entered by the user for the account number
        * @param int acnt - the account number
        * @param int pin - the pin number
        * @return bool - success or failure
    */
    public boolean checkAccPin(int acnt, int pin) {
        Accounts account = (Accounts) session.get( Accounts.class, acnt);
        boolean bool = false;
        if (account.getAcc_pin() != pin)
            bool = false;
        else
            bool = true;
        return bool;
    }

    @Override
    /**
        * @desc sets the account to locked status
        * @param int acnt - the account number to be locked
        * @return void
    */
    public void lockAcc(int acnt) {
        Transaction t = session.beginTransaction();
        Accounts account = (Accounts) session.get( Accounts.class, acnt);
        int accStatus = 0;
        account.setAcc_status(accStatus);
        session.save(account);
        t.commit();
    }
    
    @Override
    /**
        * @desc deposits the amount entered by the user into the account number and saves the transaction details
        * @param int acnt - the user account number
        * @param int amt - the amount to be deposited
        * @return void
    */
    public void deposit(int acnt, int amt) throws RemoteException {
        Transaction t1 = session.beginTransaction();
        try {
            Accounts account = (Accounts) session.get(Accounts.class,acnt);
            int newBal = account.getAcc_bal() + amt;
            account.setAcc_bal(newBal);
            session.save(account);
            t1.commit();
            
        } catch (HibernateException e) {
            e.printStackTrace();
            t1.rollback();
        } 
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime = now.format(formatter);

        Transaction t2 = session.beginTransaction();
        try {
            Transactions trans = new Transactions();
            trans.setAccNum(acnt);
            trans.setTransDatetime(datetime);
            trans.setTransType("Deposit");
            trans.setTransDetails("Deposited $" + amt);
            session.save(trans);
            t2.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t2.rollback();
        }
    }

    @Override
    /**
        * @desc withdraws the amount entered by the user from the bank account and saves the transaction details
        * @param int acnt - the user account number
        * @param int amt - the amount to be withdrawn
        * @return void
    */
    public void withdraw(int acnt, int amt) throws RemoteException {
        Transaction t1 = session.beginTransaction();
        try {
            Accounts account = (Accounts) session.get(Accounts.class,acnt);
            int newBal = account.getAcc_bal() - amt;
            account.setAcc_bal(newBal);
            session.save(account);
            t1.commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                t1.rollback();
            } 
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime = now.format(formatter);

        Transaction t2 = session.beginTransaction();
        try {
            Transactions trans = new Transactions();
            trans.setAccNum(acnt);
            trans.setTransDatetime(datetime);
            trans.setTransType("Withdraw");
            trans.setTransDetails("Withdrawn $" + amt);
            session.save(trans);
            t2.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t2.rollback();
        }
    }

    @Override
    /**
        * @desc checks the account balance
        * @param int acnt - the account number
        * @return Account_Balance - the account balance
    */
    public int checkBalance(int acnt) throws RemoteException {	
        Accounts account = (Accounts) session.get( Accounts.class, acnt);
        int Account_Balance = account.getAcc_bal();
        return Account_Balance;
    }

    @Override
    /**
        * @desc checks if the account is in overdraft
        * @param int acnt - the account number
        * @param int amt - the amount
        * @return bool - success of failure
    */
    public boolean checkOverdraft(int acnt, int amt) throws RemoteException {
        boolean bool = false;
        if((checkBalance(acnt) - amt) < 0){
            bool = true;
        }
        else 
            bool = false;
        return bool;
    }

    @Override
    /**
        * @desc gets transactions from a start date to end date from the database and send an email with the list of transactions
        * @param int acnt - the account number
        * @param int String startDate - the transaction start date
        * @param int String endDate - the transaction end date
        * @param int String emailAddr - the email address for sending the transactions to
        * @return void
    */
    public void getTransactions(int acnt, String startDate, String endDate, String emailAddr)throws RemoteException {
        String strAccount = Integer.toString(acnt);
        LocalDateTime s_date = parse(startDate.replace(" ", "T"));
        LocalDateTime e_date = parse(endDate.replace(" ", "T"));
        String Results = new String();
        Results += "<table border=\"1\"><tr><th>Transaction ID</th><th>Transaction Time</th><th>Transaction Details</th></tr>";
        List results = session.createQuery("from Transactions where acc_num=" + strAccount).list();
        for (Iterator iter = results.iterator(); iter.hasNext();) {
            Transactions trans = (Transactions) iter.next();
            LocalDateTime t_date = parse(trans.getTransDatetime().replace(" ", "T"));
            if (t_date.isAfter(s_date) && t_date.isBefore(e_date)){
                Results += "<tr><td>" + trans.getTransId()+"</td><td>"+trans.getTransDatetime()+"</td><td>"+ trans.getTransDetails() + "</td></tr>";
            } 
        }
        Results += "</table>";
        
        Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
        
        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        
        String[] toEmails = { emailAddr }; // Add your email here
        String emailSubject = "Transaction History";

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);
        
        try {
            for (int i = 0; i < toEmails.length; i++) {
                emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            emailMessage.setSubject(emailSubject);
            emailMessage.setContent(Results, "text/html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String emailHost = "smtp.gmail.com";
        String fromUser = "bankapp1234@gmail.com";
        String fromUserEmailPassword = "//bankapp1234";
        try {
            Transport transport = mailSession.getTransport("smtp");

            transport.connect(emailHost, fromUser, fromUserEmailPassword);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    /**
        * @desc makes payment to a vendor and saves the transaction details to the database
        * @param int acnt - the user account number
        * @param int amt - the amount for payment
        * @param int payAccNumber - the payment account number for the vendor
        * @param String payVendor - the vendor name
        * @return void
    */
    public String makePayment(int acnt, int amt, int payAccNumber, String payVendor) throws RemoteException{
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime = now.format(formatter);
        String receiptNum = "";
        
        Transaction t1 = session.beginTransaction();
        try {
            Payments pay = new Payments();
            pay.setPayVendor(payVendor);
            pay.setPayAccnum(payAccNumber);
            pay.setPayAmount(amt);
            pay.setPayDatetime(datetime);
            pay.setAccNum(acnt);
            session.save(pay);
            receiptNum = pay.getPayId();
            t1.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t1.rollback();
        }
        
        Transaction t2 = session.beginTransaction();
        try {
            Accounts account = (Accounts) session.get(Accounts.class,acnt);
            int newBal = account.getAcc_bal() - amt;
            account.setAcc_bal(newBal);
            session.save(account);
            t2.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t2.rollback();
        }
        
        Transaction t3 = session.beginTransaction();
        try {
            Transactions trans = new Transactions();
            trans.setAccNum(acnt);
            trans.setTransDatetime(datetime);
            trans.setTransType("Payment");
            trans.setTransDetails("Payment of $" + amt + " made to " + payVendor + " for account number " + payAccNumber);
            session.save(trans);
            t3.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t3.rollback();
        }
        return receiptNum;
    }
}
