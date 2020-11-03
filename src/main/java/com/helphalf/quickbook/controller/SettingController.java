package com.helphalf.quickbook.controller;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import com.helphalf.quickbook.controller.helper.QBXML;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.json.JSONObject;
import org.json.XML;


@RestController
public class SettingController {

    public static int PRETTY_PRINT_INDENT_FACTOR = 4;

    @GetMapping(value = "/setting", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String getSettingInfo() {
        String AccountXMLRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<?qbxml version=\"13.0\"?>\n" +
                "<QBXML>\n" +
                "  <QBXMLMsgsRq onError=\"stopOnError\">\n" +
                "    <AccountQueryRq  requestID=\"1\">\n" +
                "    </AccountQueryRq >  \n" +
                "  </QBXMLMsgsRq>\n" +
                "</QBXML>";

        String VendorXMLRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<?qbxml version=\"13.0\"?>\n" +
                "<QBXML>\n" +
                "  <QBXMLMsgsRq onError=\"stopOnError\">\n" +
                "    <VendorQueryRq  requestID=\"1\">\n" +
                "    </VendorQueryRq >  \n" +
                "  </QBXMLMsgsRq>\n" +
                "</QBXML>";

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
        Variant accountApiResponse = Dispatch.call(MySessionManager, "ProcessRequest", ticket, AccountXMLRequest);
        Variant vendorApiResponse = Dispatch.call(MySessionManager, "ProcessRequest", ticket, VendorXMLRequest);

        Variant itemApiResponse = Dispatch.call(MySessionManager, "ProcessRequest", ticket, QBXML.itemQueryXml);


//        System.out.println(accountApiResponse.toString());
//        System.out.println(accountApiResponse.toString());

        Dispatch.call(MySessionManager, "EndSession", ticket);
        Dispatch.call(MySessionManager, "CloseConnection");

        JSONObject accountXmlJSONObj = XML.toJSONObject(accountApiResponse.toString());
        JSONObject vendorXmlJSONObj = XML.toJSONObject(vendorApiResponse.toString());

//                String jsonPrettyPrintString = xmlJSONObj.toString(TEST_XML_STRING);
//        System.out.println(accountXmlJSONObj);
//        System.out.println(vendorXmlJSONObj);


        JSONObject itemXmlJSONObj = XML.toJSONObject(itemApiResponse.toString());

        System.out.println(itemXmlJSONObj);


        JSONObject xmlJSONObj = new JSONObject();
        xmlJSONObj.put("account",accountXmlJSONObj);
        xmlJSONObj.put("tax-agencies",vendorXmlJSONObj);
        return xmlJSONObj.toString();
    }

}
