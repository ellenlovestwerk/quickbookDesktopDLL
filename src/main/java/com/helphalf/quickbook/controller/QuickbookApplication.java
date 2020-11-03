package com.helphalf.quickbook.controller;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuickbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickbookApplication.class, args);
    }

}


//
//public class QuickbookApplication {
//    public static void main(String[] args) {
//        String TEST_XML_STRING =
//                "    //    <?xml version=\"1.0\" ?>\n" +
//                        "    //<QBXML>\n" +
//                        "    //<QBXMLMsgsRs>\n" +
//                        "    //<InvoiceQueryRs requestID=\"1\" statusCode=\"0\" statusSeverity=\"Info\" statusMessage=\"Status OK\">\n" +
//                        "    //<InvoiceRet>\n" +
//                        "    //<TxnID>1-1602295767</TxnID>\n" +
//                        "    //<TimeCreated>2020-10-09T19:09:27-08:00</TimeCreated>\n" +
//                        "    //<TimeModified>2020-10-09T19:09:27-08:00</TimeModified>\n" +
//                        "    //<EditSequence>1602295767</EditSequence>\n" +
//                        "    //<TxnNumber>1</TxnNumber>\n" +
//                        "    //<CustomerRef>\n" +
//                        "    //<ListID>80000001-1602094833</ListID>\n" +
//                        "    //    <FullName>ellen park</FullName>\n" +
//                        "    //</CustomerRef>\n" +
//                        "    //<ARAccountRef>\n" +
//                        "    //<ListID>80000027-1602295040</ListID>\n" +
//                        "    //    <FullName>Accounts Receivable</FullName>\n" +
//                        "    //</ARAccountRef>\n" +
//                        "    //<TemplateRef>\n" +
//                        "    //<ListID>80000001-1601274059</ListID>\n" +
//                        "    //    <FullName>Intuit Product Invoice</FullName>\n" +
//                        "    //</TemplateRef>\n" +
//                        "    //<TxnDate>2020-10-09</TxnDate>\n" +
//                        "    //<RefNumber>1</RefNumber>\n" +
//                        "    //<BillAddress>\n" +
//                        "    //    <Addr1>ship saving</Addr1>\n" +
//                        "    //</BillAddress>\n" +
//                        "    //<BillAddressBlock>\n" +
//                        "    //    <Addr1>ship saving</Addr1>\n" +
//                        "    //</BillAddressBlock>\n" +
//                        "    //<IsPending>false</IsPending>\n" +
//                        "    //<IsFinanceCharge>false</IsFinanceCharge>\n" +
//                        "    //<DueDate>2020-10-09</DueDate>\n" +
//                        "    //<ShipDate>2020-10-09</ShipDate>\n" +
//                        "    //<Subtotal>100.00</Subtotal>\n" +
//                        "    //<SalesTaxPercentage>0.00</SalesTaxPercentage>\n" +
//                        "    //<SalesTaxTotal>0.00</SalesTaxTotal>\n" +
//                        "    //<AppliedAmount>0.00</AppliedAmount>\n" +
//                        "    //<BalanceRemaining>100.00</BalanceRemaining>\n" +
//                        "    //<IsPaid>false</IsPaid>\n" +
//                        "    //<IsToBePrinted>true</IsToBePrinted>\n" +
//                        "    //<IsToBeEmailed>false</IsToBeEmailed>\n" +
//                        "    //</InvoiceRet>\n" +
//                        "    //</InvoiceQueryRs>\n" +
//                        "    //</QBXMLMsgsRs>\n" +
//                        "    //</QBXML>";
//        JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
////                String jsonPrettyPrintString = xmlJSONObj.toString(TEST_XML_STRING);
//        System.out.println(xmlJSONObj);
//    }
//}
//
