package com.helphalf.quickbook.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.helphalf.quickbook.controller.helper.Invoice;

@RestController
public class InvoiceController {
    //    @Autowired
    @GetMapping(value = "/", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String home() {
        return "hello from ellen";

    }

    @PostMapping(value = "/invoice/create", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String createInvoice(@RequestBody String jsonString) {

        //handle json file
        if(jsonString.indexOf("'") != -1) {
            jsonString = jsonString.replaceAll("'", "\\'");
        }
        if(jsonString.indexOf("\"") != -1) {
            jsonString = jsonString.replaceAll("\"", "\\\"");
        }
//            if(jsonString.indexOf("\r\n")!=-1){
//                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
//                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
//            }
//        if(jsonString.indexOf("\n")!=-1){
//            //将换行转换一下，因为JSON串中字符串不能出现显式的换行
//            jsonString = jsonString.replaceAll("\n", "\\u000a");
//        }

        jsonString = formatJson(jsonString);

//        System.out.println(jsonString);

        JSONObject json = JSONObject.parseObject(jsonString);

//        System.out.println(json);

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

        System.out.println("sent data to qb desktop");

        return apiResponse.toString();
    }

    public static String json2Xml(JSONObject json) {
//        LOG.info("---------------json转换成xml:---------------");

        Map<String, String> map = JSON.parseObject(JSONObject.toJSONString(json),Map.class);
//        System.out.println(map);
        Map<String, Object> settingObj = JSONObject.parseObject(JSON.toJSONString(map.get("settings")));


        Invoice invoiceWithItemsRecord = new Invoice();
        Invoice.ShipAddress shipAddress = new Invoice.ShipAddress();
        Invoice.InvoiceLineAdd invoiceLine = new Invoice.InvoiceLineAdd();

        JSONObject customer = JSONObject.parseObject(JSON.toJSONString(map.get("customer")));
//        Map<String, Object> customerMap = JSON.parseObject(JSONObject.toJSONString(map.get("customer")),Map.class);
        invoiceWithItemsRecord.fullCustomerName =  (String)customer.get("name");
        shipAddress.addr1 = (String)customer.get("street1");
        shipAddress.city = (String)customer.get("city");
        shipAddress.postalCode = (String)customer.get("zip_code");
        shipAddress.country = (String)customer.get("country");
        shipAddress.state = (String)customer.get("state");

        String payload = "";
        payload+="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        payload+="<?qbxml version=\"10.0\"?>";
        payload+="<QBXML>";
        payload+="<QBXMLMsgsRq onError=\"stopOnError\">";
        payload+="<InvoiceAddRq requestID=\"2\">";
        payload+="	<InvoiceAdd>";
        payload+="		<CustomerRef>";
        payload+="			<FullName>"+invoiceWithItemsRecord.fullCustomerName+"</FullName>";
        payload+="		</CustomerRef>";
//        payload+="		<TxnDate>"+invoiceWithItemsRecord.txnDate+"</TxnDate>";
        payload+="		<RefNumber>"+invoiceWithItemsRecord.refNumber+"</RefNumber>";
        payload+="		<BillAddress>";
        payload+="			<Addr1>"+shipAddress.addr1+"</Addr1>";
        payload+="			<City>"+shipAddress.city+"</City>";
        payload+="			<State>"+shipAddress.state+"</State>";
        payload+="			<PostalCode>"+shipAddress.postalCode+"</PostalCode>";
        payload+="			<Country>"+shipAddress.country+"</Country>";
        payload+="		</BillAddress>";
        payload+="		<PONumber>"+invoiceWithItemsRecord.pONumber+"</PONumber>";
        payload+="		<Memo>"+invoiceWithItemsRecord.memo+"</Memo> ";


        JSONArray lineArray = (JSONArray)json.get("items");
        for (int i = 0; i < lineArray.size(); i++) {
            String lineDetail = JSON.toJSONString(lineArray.get(i));
            Map<String, Object> lineMap = JSON.parseObject(lineDetail, Map.class);
            invoiceLine.itemFullName = (String) lineMap.get("name");
            invoiceLine.description = (String) lineMap.get("description");
            invoiceLine.rate = Double.parseDouble(lineMap.get("unit_price").toString());
            invoiceLine.quantity =(Integer) lineMap.get("quantity");

            payload+="		<InvoiceLineAdd>";
            payload+="			<ItemRef>";
            payload+="				<FullName>"+invoiceLine.itemFullName+"</FullName>";//04 Concrete
            payload+="			</ItemRef>";
            payload+="			<Desc>"+invoiceLine.description+"</Desc>";
            payload+="			<Quantity>"+invoiceLine.quantity+"</Quantity>";
            payload+="			<Rate>"+invoiceLine.rate+"</Rate>";
            payload+="		</InvoiceLineAdd> ";
        }

        payload+="	</InvoiceAdd>";
        payload+="</InvoiceAddRq>";
        payload+="</QBXMLMsgsRq>";
        payload+="</QBXML>";

        System.out.println("payload" + payload);

        return payload;
    }


    private static String SPACE = " ";


    public static String formatJson(String json) {
        StringBuffer result = new StringBuffer();
        int length = json.length();
        int number = 0;
        char key = 0;
        for (int i = 0; i < length; i++) {
            key = json.charAt(i);
            if ((key == '[') || (key == '{')) {
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }
                result.append(key);
                result.append('\n');
                number++;
                result.append(indent(number));
                continue;
            }
            if ((key == ']') || (key == '}')) {
                result.append('\n');
                number--;
                result.append(indent(number));
                result.append(key);
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }
                continue;
            }
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }
            result.append(key);
        }
        return result.toString();
    }

    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }
}

