
package com.assignment.bank;

import java.rmi.registry.Registry;
 
public class Main_Driver {
    
    static final Integer IN_SML = 4;
    static final Integer IN_MED = 6;
    static final Integer IN_LRG = 8;
    static final Integer NEW_TRY = 3;
    static Registry bank_reg;
    static BankInterface bank_pros;
    static Numpad_Panel new_num;
    static Option_Panel new_opt;
    static Integer acc_num;
    static Integer pay_num;
    static Integer tries;
    static String opt;
    static String pay;
    static String date_srt;
    static String date_end;
    
    static void fun_acc_num()
    {
        opt = "acc_ck";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> ENTER ACCOUNT NUMBER <br><br>";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_acc_pin()
    {
        opt = "pin_ck";
        tries = NEW_TRY;
        Numpad_Panel.in_size = IN_SML;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> ENTER ACCOUNT PIN <br><br>TRIES REMAINING : "+tries +"<br><br>";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_depo()
    {
        opt = "depo";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ENTER DEPOSIT AMOUNT <br><br>FJD ";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_with()
    {
        opt = "with";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ENTER WITHDRAW AMOUNT <br><br>FJD ";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_pay_sel()
    {
        opt = "pay_acc";
        Option_Panel.opt_disp = "<html><p style=\"text-align:center;\"> SELECT PAYMENT VENDOR ";
        new_opt = new Option_Panel();
        new_opt.setVisible(true);
    }
    
    static void fun_pay_num()
    {
        Numpad_Panel.in_size = IN_LRG;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ENTER "+pay +"ACCOUNT NUMBER <br><br>";
        Numpad_Panel.in.clear();
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_pay_amt()
    {
        opt = "pay";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ENTER PAYMENT AMOUNT <br><br> FJD ";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_trans()
    {
        opt = "trans";
        new Record_Panel().setVisible(true);
    }
     
    public static void main(String[] args) {
        Main_Driver main = new Main_Driver();
        new Home_Panel().setVisible(true);
    }
}
