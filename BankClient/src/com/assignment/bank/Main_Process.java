/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.bank;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.assignment.bank.Main_Driver.bank_reg;

/**
 *
 * @author Vj
 */
public class Main_Process {
    
    
    static void pros_connect() throws AccessException, NotBoundException
    {
        try {
            // fire to localhost port 1099
            bank_reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
             
            Main_Driver.bank_pros = (BankInterface) bank_reg.lookup("Bank");
            
        } catch (RemoteException ex) {
            Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void pros_acc_num()
    {
        Integer multiplier = 1;
        Main_Driver.acc_num = 0;
        
        try {
            
            try 
            {
                pros_connect();
            } 
            catch (AccessException | NotBoundException ex) 
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                Main_Driver.acc_num = Main_Driver.acc_num + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            if (Main_Driver.bank_pros.checkAccNum(Main_Driver.acc_num))
            {
                if (Main_Driver.bank_pros.checkAccStatus(Main_Driver.acc_num))
                {
                    Main_Driver.new_num.setVisible(false);
                    Main_Driver.fun_acc_pin();
                }   
                else
                {
                    Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> THIS ACCOUNT IS LOCKED <br><br> PLEASE CHECK WITH OUR NEAREST BRANCH ";
                    Main_Driver.new_num.display_message();
                    Main_Driver.new_num.lock_panel();
                }
            }
            else
            {
                Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ACCOUNT DOES NOT EXIST <br><br> TRY AGAIN <br><br>";
                Numpad_Panel.in.clear();
                Main_Driver.new_num.input();
            }
                        
        }
        catch (RemoteException e)
        {
        }
    }
    
    static void pros_acc_pin()
    {
        try
        {
            Integer multiplier = 1;
            Integer acc_pin = 0;
            Integer last_atm = 1;
            
            try
            {
                pros_connect();
            }
            catch (AccessException | NotBoundException ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                acc_pin = acc_pin + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            if (Main_Driver.tries > last_atm)
            {
                if (Main_Driver.bank_pros.checkAccPin(Main_Driver.acc_num,acc_pin))
                {
                    Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\">";
                    new Menu_Panel().setVisible(true);
                    Main_Driver.new_num.setVisible(false);
                    acc_pin = 0;
                }
                else
                {
                    Main_Driver.tries--;
                    Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> INCORRECT PIN <br><br> ENTER ACCOUNT PIN <br><br>TRIES REMAINING : "+Main_Driver.tries +"<br><br>";
                    Main_Driver.new_num.display_message();
                    Numpad_Panel.in.clear();
                    Main_Driver.new_num.input();  
                }
            }
            else
            {
                Main_Driver.bank_pros.lockAcc(Main_Driver.acc_num);
                Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> TOO MANY FAILED ATTEMPS <br><br> ACCOUNT IS NOW LOCKED <br><br> PLEASE CHECK WITH OUR NEAREST BRANCH ";
                Main_Driver.new_num.display_message();
                Main_Driver.new_num.lock_panel();
            }
        }
        catch (RemoteException ex)
        {
            Logger.getLogger(Main_Process.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    static void pros_depo()
    {
        
        Integer multiplier = 1;
        Integer dept_val = 0;
        
        try
        {
            try
            {
                pros_connect();
            }
            catch (AccessException | NotBoundException ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                dept_val = dept_val + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            Main_Driver.bank_pros.deposit(Main_Driver.acc_num, dept_val);
            Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\"> DEPOSIT SUCCESSFUL <br><br> ";
            new Menu_Panel().setVisible(true);
            
        }
        catch (RemoteException ex)
        {
            Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    static void pros_with()
    {
        
        Integer multiplier = 1;
        Integer with_val = 0;
        
        try
        {
            try
            {
                pros_connect();
            }
            catch (AccessException | NotBoundException ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                with_val = with_val + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            if (Main_Driver.bank_pros.checkOverdraft(Main_Driver.acc_num, with_val))
            {
                Main_Driver.fun_with();
                Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> WITHDRAW AMOUNT EXCEEDS AMOUNT BALANCE <br><br> ENTER NEW AMOUNT <br><br>FJD <html>";
                Main_Driver.new_num.display_message();
            }
            else
            {
                Main_Driver.bank_pros.withdraw(Main_Driver.acc_num, with_val);
                Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\"> WITHDRAWAL SUCCESSFUL <br><br> ";
                new Menu_Panel().setVisible(true);
            }
        }
        catch (RemoteException ex)
        {
            Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    static void pros_pay_num()
    {
        Integer multiplier = 1;
        Main_Driver.pay_num = 0;
        
        Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
        for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
        {
            Main_Driver.pay_num = Main_Driver.pay_num + (in_val[i] * multiplier);
            multiplier = multiplier * 10;
        }
        
        Main_Driver.fun_pay_amt();
    }
    
    static void pros_pay()
    {
        Integer multiplier = 1;
        Integer pay_val = 0;
        
        try
        {
            try
            {
                pros_connect();
            }
            catch (AccessException | NotBoundException ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                pay_val = pay_val + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            String rct = Main_Driver.bank_pros.makePayment(Main_Driver.acc_num, pay_val, Main_Driver.pay_num, Main_Driver.pay);
            Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\"> PAYMENT SUCCESSFUL <br><br> RECIEPT NUMBER "+rct;
            new Menu_Panel().setVisible(true);
            
        }
        catch (RemoteException ex)
        {
            Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
