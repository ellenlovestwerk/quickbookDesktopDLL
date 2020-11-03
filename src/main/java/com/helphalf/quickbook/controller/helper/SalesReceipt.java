package com.helphalf.quickbook.controller.helper;

import java.util.List;

public class SalesReceipt {
    public String fullCustomerName;
    public String txnDate;
    public BillAddress billAddress;
    public String refNumber;
    public String pONumber;
    public String memo;
    public String IsPaid;
    public List<SalesReceiptLineAdd> salesReceiptLineAddArray;

    public class BillAddress{
        public String addr1;
        public String city;
        public String state;
        public String postalCode;
        public String country;
    }

    public static class ShipAddress{
        public String addr1;
        public String city;
        public String state;
        public String postalCode;
        public String country;
    }

    public static class SalesReceiptLineAdd{
        public String itemFullName;
        public String description;
        public Integer quantity;
        public double rate;
    }
}