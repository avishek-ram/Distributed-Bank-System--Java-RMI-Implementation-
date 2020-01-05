//Getter and setter mapping  for hybernate to map the class to the database table
package com.assignment.bank;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Payments"
)
public class Payments  implements Serializable {


     private String payId;
     private String payVendor;
     private int payAccnum;
     private double payAmount;
     private String payDatetime;
     private int accNum;

    public Payments() {
    }

    public Payments(String payVendor, int payMeternum, double payAmount, String payDatetime, int accNum) {
       this.payVendor = payVendor;
       this.payAccnum = payMeternum;
       this.payAmount = payAmount;
       this.payDatetime = payDatetime;
       this.accNum = accNum;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="pay_id", unique=true, nullable=false, length=2000000000)
    public String getPayId() {
        return this.payId;
    }
    
    public void setPayId(String payId) {
        this.payId = payId;
    }

    
    @Column(name="pay_vendor", nullable=false, length=2000000000)
    public String getPayVendor() {
        return this.payVendor;
    }
    
    public void setPayVendor(String payVendor) {
        this.payVendor = payVendor;
    }

    
    @Column(name="pay_accnum", nullable=false)
    public int getPayAccnum() {
        return this.payAccnum;
    }
    
    public void setPayAccnum(int payAccnum) {
        this.payAccnum = payAccnum;
    }

    
    @Column(name="pay_amount", nullable=false, precision=2000000000, scale=10)
    public double getPayAmount() {
        return this.payAmount;
    }
    
    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    
    @Column(name="pay_datetime", nullable=false, length=2000000000)
    public String getPayDatetime() {
        return this.payDatetime;
    }
    
    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    
    @Column(name="acc_num", nullable=false)
    public int getAccNum() {
        return this.accNum;
    }
    
    public void setAccNum(int accNum) {
        this.accNum = accNum;
    }

}


