package com.helphalf.quickbook.controller;

import com.helphalf.quickbook.controller.helper.FormatJson;

//import org.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OriginalSalesReceiptController {

    @PostMapping(value = "/salesReceipt/create", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void createSalesReceipt(@RequestBody String jsonString) {


        if(jsonString.indexOf("'") != -1) {
            jsonString = jsonString.replaceAll("'", "\\'");
        }

        if(jsonString.indexOf("\"") != -1) {
            jsonString = jsonString.replaceAll("\"", "\\\"");
        }


        jsonString = FormatJson.formatJson(jsonString);

        System.out.println("jsonString" + jsonString);

        JSONObject json = JSONObject.parseObject(jsonString);

        System.out.println("json" + json);

        String sb = json2Xml(json);

        Variant QBPermissionMode = new Variant(1);
        //Mode for Multi user/Single User or both, this setting is both
        Variant QBaccessMode = new Variant(2);
        //Leave Empty to use the currently opened QB File
        String fileLocation = "";  //not needed unless opening QB file which is currently not opened

        String appID = "";//not needed unless you want to set AppID
        String applicationName = "QB Sync Test";
        Dispatch MySessionManager = new Dispatch("QBXMLRP2.RequestProcessor");
        Dispatch.call(MySessionManager, "OpenConnection2", appID, applicationName, QBPermissionMode);
        Variant ticket = Dispatch.call(MySessionManager, "BeginSession",fileLocation, QBaccessMode);
        Variant apiResponse = Dispatch.call(MySessionManager, "ProcessRequest", ticket, sb);
        System.out.println(apiResponse.toString());
        Dispatch.call(MySessionManager, "EndSession", ticket);
        Dispatch.call(MySessionManager, "CloseConnection");

    }

    public static String json2Xml(JSONObject json) {
        Map<String, String> map = JSON.parseObject(JSONObject.toJSONString(json),Map.class);
//        System.out.println(map);


        JSONObject customer = JSONObject.parseObject(JSON.toJSONString(map.get("customer")));
//        Map<String, Object> customerMap = JSON.parseObject(JSONObject.toJSONString(map.get("customer")),Map.class);
        String fullName = (String)customer.get("name");
        String addr1 = (String)customer.get("street1");
        String city = (String)customer.get("city");
        String postalCode = (String)customer.get("zip_code");
        String country = (String)customer.get("country");
        String state = (String)customer.get("state");

        JSONObject item = JSONObject.parseObject(JSON.toJSONString(map.get("items")));
        String itemName = (String)item.get("name");
        Double unitPrice = Double.parseDouble(item.get("unit_price").toString());
        String description = item.get("description").toString();
        Integer quantity = (Integer)item.get("quantity");
        Double amount = unitPrice*quantity;
//
//            if (map.get("Error_Code") == null || map.get("Error_Code").equals("")) {
//                //生成头
        String sb = new String("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb+="<?qbxml version=\"13.0\"?>\n";
        sb+="<QBXML>\n";
        sb+="<QBXMLMsgsRq onError=\"stopOnError\">\n";
        sb+="<SalesReceiptAddRq requestID=\"0\">\n";
        sb+="	<SalesReceiptAdd defMacro=\"TxnID:NewSalesReceipt\">\n";
        sb+="		<CustomerRef>\n";
        sb+="			<FullName>"+fullName+"</FullName>\n";
        sb+="		</CustomerRef>\n";
//        sb+="       <TxnDate>"+2020/10/29+"</TxnDate>\n";
        sb+="		<BillAddress>\n";
        sb+="			<Addr1>"+addr1+"</Addr1>\n";
        sb+="			<City>"+city+"</City>\n";
        sb+="			<State>"+state+"</State>\n";
        sb+="			<PostalCode>"+postalCode+"</PostalCode>\n";
        sb+="			<Country>"+country+"</Country>\n";
        sb+="		</BillAddress>\n";
        sb+="                <SalesReceiptLineAdd>\n";
        sb+="                  <ItemRef>\n";
        sb+="                     <FullName>testItem2</FullName>\n";
        sb+="                  </ItemRef>\n";
        sb+="			       <Desc>"+description+"</Desc>\n";
        sb+="			       <Quantity>"+quantity+"</Quantity>\n";
        sb+="                  <Rate>"+unitPrice+"</Rate>\n";
        sb+="                </SalesReceiptLineAdd>\n";
        sb+="	</SalesReceiptAdd>\n";
        sb+="</SalesReceiptAddRq>\n";
        sb+="</QBXMLMsgsRq>\n";
        sb+="</QBXML>\n";

        return sb;
    }


}
