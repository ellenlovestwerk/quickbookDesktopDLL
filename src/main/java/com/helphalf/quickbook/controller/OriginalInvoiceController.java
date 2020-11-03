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

@RestController
public class OriginalInvoiceController {
//    @Autowired
//    @GetMapping(value = "/", produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public String home() {
//        return "hello from ellen";
//
//    }

    @PostMapping(value = "/originalInvoice/create", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void createInvoice(@RequestBody String jsonString) {

//        String jsonString = invoice.toString();
//        System.out.println("hello from ellen");
//        System.out.println("hello"+jsonString);

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

        json2Xml(json);
        System.out.println("111111hello from ellen");
    }

    public static String json2Xml(JSONObject json) {
//        LOG.info("---------------json转换成xml:---------------");

//        Map<String, String> map = json2Map(json);
        Map<String, String> map = JSON.parseObject(JSONObject.toJSONString(json),Map.class);
//        System.out.println(map);
        Map<String, Object> settingObj = JSONObject.parseObject(JSON.toJSONString(map.get("settings")));



        JSONObject customer = JSONObject.parseObject(JSON.toJSONString(map.get("customer")));
//        Map<String, Object> customerMap = JSON.parseObject(JSONObject.toJSONString(map.get("customer")),Map.class);
        String fullName = (String)customer.get("name");
        String addr1 = (String)customer.get("street1");
        String city = (String)customer.get("city");
        String postalCode = (String)customer.get("zip_code");
        String country = (String)customer.get("country");
        String state = (String)customer.get("state");

//        JSONObject item = JSONObject.parseObject(JSON.toJSONString(map.get("items")));
//        String itemName = (String)item.get("name");
//        Double unitPrice = Double.parseDouble(item.get("unit_price").toString());
//        String description = item.get("description").toString();
//        Integer quantity = (Integer)item.get("quantity");
//        Double amount = unitPrice*quantity;

//        List<JSONObject> invLine = new ArrayList<>();
//        JSONArray lineArray = (JSONArray)json.get("items");
//        for (int i = 0; i < lineArray.size(); i++) {
//            String lineDetail = JSON.toJSONString(lineArray.get(i));
//            Map<String, Object> lineMap = JSON.parseObject(lineDetail, Map.class);
//            String itemName = (String) lineMap.get("name");
//            String description = (String) lineMap.get("description");
//            Double unitPrice = Double.parseDouble(lineMap.get("unit_price").toString());
//            Integer quantity =(Integer) lineMap.get("quantity");
//        }

//
//            if (map.get("Error_Code") == null || map.get("Error_Code").equals("")) {
//                //生成头
        String sb = new String("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb+="<?qbxml version=\"13.0\"?>\n";
        sb+="<QBXML>\n";
        sb+="<QBXMLMsgsRq onError=\"stopOnError\">\n";
        sb+="<InvoiceAddRq requestID=\"0\">\n";
        sb+="	<InvoiceAdd defMacro=\"TxnID:NewInvoice\">\n";
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

        List<JSONObject> invLine = new ArrayList<>();
        JSONArray lineArray = (JSONArray)json.get("items");
        for (int i = 0; i < lineArray.size(); i++) {
            String lineDetail = JSON.toJSONString(lineArray.get(i));
            Map<String, Object> lineMap = JSON.parseObject(lineDetail, Map.class);
            String itemName = (String) lineMap.get("name");
            String description = (String) lineMap.get("description");
            Double unitPrice = Double.parseDouble(lineMap.get("unit_price").toString());
            Integer quantity =(Integer) lineMap.get("quantity");
        sb+="                <InvoiceLineAdd>\n";
        sb+="                  <ItemRef>\n";
        sb+="                     <FullName>"+itemName+"</FullName>\n";
        sb+="                  </ItemRef>\n";
        sb+="			       <Desc>"+description+"</Desc>\n";
        sb+="			       <Quantity>"+quantity+"</Quantity>\n";
        sb+="                  <Rate>"+unitPrice+"</Rate>\n";
        sb+="                </InvoiceLineAdd>\n";
        }
        sb+="	</InvoiceAdd>\n";
        sb+="</InvoiceAddRq>\n";
        sb+="</QBXMLMsgsRq>\n";
        sb+="</QBXML>\n";
        System.out.println("sb" + sb);

        //        String XMLRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                "<?qbxml version=\"13.0\"?>\n" +
//                "<QBXML>\n" +
//                "  <QBXMLMsgsRq onError=\"stopOnError\">\n" +
//                "    <InvoiceAddRq  requestID=\"0\">\n" +
//                "       <InvoiceAdd defMacro=\"TxnID:NewInvoice\">\n" +
//                "           <CustomerRef>\n" +
//                "                   <FullName>Ellen Park</FullName>\n" +
//                "                </CustomerRef>\n" +
//                "                <TemplateRef>\n" +
//                "                   <FullName>Intuit Service Invoice</FullName>\n" +
//                "                </TemplateRef>\n" +
//                "                <InvoiceLineAdd>\n" +
//                "                  <ItemRef>\n" +
//                "                     <FullName>Test</FullName>\n" +
//                "                  </ItemRef>\n" +
//                "                  <Amount>100.00</Amount>\n" +
//                "                </InvoiceLineAdd>\n" +
//                "       </InvoiceAdd>\n" +
//                "    </InvoiceAddRq >  \n" +
//                "  </QBXMLMsgsRq>\n" +
//                "</QBXML>";
//                return sb;
//		payload+="		<PONumber>"+invoiceWithItemsRecord.pONumber+"</PONumber>";

//                sb.append("<?qbxml version=\"10.0\"?>");
//                //生成三个子标签
//                System.out.println(sb);
//                d = DocumentHelper.parseText(sb.toString());
//                Element root = d.getRootElement();
//                Element body = root.addElement("<QBXML>");
//                Element lir= body.addElement("<QBXMLMsgsRq onError=\"stopOnError\">/");
//                Element lo = lir.addElement("<InvoiceAddRq requestID=\"2\">");
//                Element l = lo.addElement("<InvoiceAdd>");
//                Element CustomerRef =l.addElement("		<CustomerRef>");
//                Element BillAddress =l.addElement("		<BillAddress>");
//
//                //获取JSONObject
//                JSONObject Customer = (JSONObject)(json.get("customer"));
//                JSONObject FullName = (JSONObject)(Customer.get("name"));
//                JSONObject Addr1 = (JSONObject)(Customer.get("street1"));
//                JSONObject City = (JSONObject)(Customer.get("city"));
//                JSONObject PostalCode = (JSONObject)(Customer.get("zip_code"));
//                JSONObject Country = (JSONObject)(Customer.get("country"));
//                JSONObject State = (JSONObject)(Customer.get("state"));
//
//
//                //把获取到的JSONObject转换成map
//                Map<String, String> lopchildren = json2Map(Customer);
//                //遍历map生成标签
//                for (String key : lopchildren.keySet()) {
//                        lo.addElement(key).addCDATA(map.get(key)==null? "":map.get(key));
//                }
//            } else {
//                //生成根标签
//                StringBuffer sb = new StringBuffer(
//                        "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">");
//                sb.append("</S:Envelope>");
//                //获取根标签
//                d = DocumentHelper.parseText(sb.toString());
//                Element root = d.getRootElement();
//                //在根标签内逐级添加子标签
//                Element header = root.addElement("S:Header");
//                header.addElement("work:WorkContext","http://oracle.com/weblogic/soap/workarea/").setText(map.get("WorkContext"));
//                Element body = root.addElement("S:Body");
//                Element lir= body.addElement("ns2:PlaceOrder_LocalInputResponse","http://siebel.com/");
//                Element lo = lir.addElement("PlaceOrder_LocalOutput");
//                lo.addElement("Error_Code").setText(map.get("Error_Code")==null? "":map.get("Error_Code"));
//                lo.addElement("Error_Message").setText(map.get("Error_Message")==null? "":map.get("Error_Message"));
//                lo.addElement("ErrorCode").setText(map.get("ErrorCode")==null? "":map.get("ErrorCode"));
//                lo.addElement("ErrorMessage").setText(map.get("ErrorMessage")==null? "":map.get("ErrorMessage"));
//            }


//        String strXml = d.getRootElement().asXML();
//        return strXml;

//          return sb;
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

        return apiResponse.toString();
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

//
//    <?xml version="1.0" encoding="utf-8"?>
//<?qbxml version="5.0"?>
//<QBXML>
//<QBXMLMsgsRq onError="continueOnError">
//<InvoiceAddRq requestID="SW52b2ljZUFkZHw1NA==">
//<InvoiceAdd>
//<CustomerRef>
//<ListID>90001-1263558758</ListID>
//<FullName>Testy McTesterson</FullName>
//</CustomerRef>
//<TxnDate>2010-01-15</TxnDate>
//<RefNumber>21011</RefNumber>
//<BillAddress>
//<Addr1>ConsoliBYTE, LLC</Addr1>
//<Addr2>134 Stonemill Road</Addr2>
//<Addr3 />
//<City>Mansfield</City>
//<State>CT</State>
//<PostalCode>06268</PostalCode>
//<Country>United States</Country>
//</BillAddress>
//<ShipAddress>
//<Addr1>ConsoliBYTE, LLC</Addr1>
//<Addr2>Attn: Keith Palmer</Addr2>
//<Addr3>56 Cowles Road</Addr3>
//<City>Willington</City>
//<State>CT</State>
//<PostalCode>06279</PostalCode>
//<Country>United States</Country>
//</ShipAddress>
//<TermsRef>
//<FullName>Net 30</FullName>
//</TermsRef>
//<SalesRepRef>
//<FullName>KRP</FullName>
//</SalesRepRef>
//<Memo>Test memo goes here.</Memo>
//<InvoiceLineAdd>
//<ItemRef>
//<FullName>test</FullName>
//</ItemRef>
//<Desc>Test item description</Desc>
//<Quantity>1.00000</Quantity>
//<Rate>15.00000</Rate>
//</InvoiceLineAdd>
//</InvoiceAdd>
//</InvoiceAddRq>
//</QBXMLMsgsRq>
//</QBXML>