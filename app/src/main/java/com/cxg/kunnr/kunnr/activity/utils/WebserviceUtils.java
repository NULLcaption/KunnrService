package com.cxg.kunnr.kunnr.activity.utils;

import com.cxg.kunnr.kunnr.activity.query.ResultMsgSap;
import com.cxg.kunnr.kunnr.activity.query.Zskunnr;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: webservice工具类
 * author: xg.chen
 * time: 2017/11/22
 * version: 1.0
 */

public class WebserviceUtils {
    //命名空间
    public static String NAMESPACE_001 = "urn:sap-com:document:sap:soap:functions:mc-style";
    public static String NAMESPACE_003 = "urn:sap-com:document:sap:soap:functions:mc-style";
    //请求方法名
    public static String METHOD_NAME_001 = "ZshipmentInTransitV1";//根据经销商获取其运单汇总表以及明细列表
    public static String METHOD_NAME_003 = "ZproofOfDeliveryConfirm";//SAP POD确认到货
    //请求路径
    public static String SOAP_ACTION_001 = NAMESPACE_001 + "/" + METHOD_NAME_001;
    public static String SOAP_ACTION_003 = NAMESPACE_003 + "/" + METHOD_NAME_003;
    //请求的webservice路径http://sapqas:  http://192.168.0.16:
    public static final String URL_001 = "http://192.168.0.16:8000/sap/bc/srt/rfc/sap/zshipment_in_transit_v1/700/zshipment_in_transit_v1/binding?sap-client=700&sap-user=rfc&sap-password=poiuyt";
    public static final String URL_003 = "http://192.168.0.16:8000/sap/bc/srt/rfc/sap/zproof_of_delivery_confirm/700/zproof_of_delivery_confirm/binding?sap-client=700&sap-user=rfc&sap-password=poiuyt";

    /**
     * Description: 根据运单在SAP做POD确认到货
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    public static List<Object> callWebservice003 (String url, String methodName, String Tknum, String podDate) {
        List<Object> resultList = new ArrayList<>();

        SoapObject request = new SoapObject(NAMESPACE_003, methodName);

        request.addProperty("IvPodat",podDate);//pod确认日期
        request.addProperty("IvPotim",null);//pod确认时间
        request.addProperty("IvTknum",Tknum);//运单信息

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = true;

        try {
            httpTransport.call(SOAP_ACTION_003, envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                //解析后的返回list
                resultList = parseSoapObject003(soapObject);
                return resultList;
            } else if (envelope.bodyIn instanceof SoapFault) {
                SoapFault soapFault = (SoapFault) envelope.bodyIn;
                try {
                    throw new Exception(soapFault.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * Description: 解析返回对象
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    private static List<Object> parseSoapObject003(SoapObject result) {
        List<Object> list = new ArrayList<>();

        String EvCode = result.getProperty("EvCode").toString();
        String EvMsg = result.getProperty("EvMsg").toString();
        System.out.println("EvCode:"+EvCode);
        System.out.println("EvMsg:"+EvMsg);

        ResultMsgSap resultMsgSap = new ResultMsgSap();
        resultMsgSap.setEvCode(EvCode);
        resultMsgSap.setEvMsg(EvMsg);

        list.add(resultMsgSap);

        return list;
    }

    /**
     * Description: 获取在途运单明细列表
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    public static List<Object> callWebservice002 (String url, String methodName, String Tknum) {
        List<Object> resultList = new ArrayList<>();

        SoapObject request = new SoapObject(NAMESPACE_001, methodName);

        request.addProperty("EtShipmentL",null);//运单详细
        request.addProperty("EtShipmentT",null);//运单总单
        request.addProperty("IvDateHigh",null);//开始时间
        request.addProperty("IvDateLow",null);//结束时间
        request.addProperty("IvKunnr",null);//经销商编码
        request.addProperty("IvTknum",Tknum);//运单信息

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = true;

        try {
            httpTransport.call(SOAP_ACTION_001, envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                //解析后的返回list
                resultList = parseSoapObject002(soapObject);
                return resultList;
            } else if (envelope.bodyIn instanceof SoapFault) {
                SoapFault soapFault = (SoapFault) envelope.bodyIn;
                try {
                    throw new Exception(soapFault.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Description: 解析返回对象
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    private static List<Object> parseSoapObject002(SoapObject result) {
        List<Object> list = new ArrayList<>();

        SoapObject EtShipmentL = (SoapObject) result.getProperty("EtShipmentL");
        String EvCode = result.getProperty("EvCode").toString();
        String EvMsg = result.getProperty("EvMsg").toString();
        System.out.println("EvCode:"+EvCode);
        System.out.println("EvMsg:"+EvMsg);

        if (EtShipmentL == null) {
            return null;
        }
        for (int i = 0; i < EtShipmentL.getPropertyCount(); i++) {
            SoapObject soapObject = (SoapObject) EtShipmentL.getProperty(i);
            String Maktx = soapObject.getProperty("Maktx").toString();
            String Lfimg = soapObject.getProperty("Lfimg").toString();
            String Vrkme = soapObject.getProperty("Vrkme").toString();

            ZsshipmentInTransit zshipmentL = new ZsshipmentInTransit();
            zshipmentL.setMaktx(Maktx);
            zshipmentL.setLfimg(Lfimg);
            zshipmentL.setVrkme(Vrkme);

            list.add(zshipmentL);
        }

        return list;
    }

    /**
     * Description: 获取在途运单汇总列表
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    public static List<Object> callWebservice001 (String url, String methodName, String kunnId) {
        List<Object> resultList = new ArrayList<>();

        SoapObject request = new SoapObject(NAMESPACE_001, methodName);
        request.addProperty("EtShipmentL",null);//运单详细
        request.addProperty("EtShipmentT",null);//运单总单
        request.addProperty("IvDateHigh",null);//开始时间
        request.addProperty("IvDateLow",null);//结束时间
        request.addProperty("IvKunnr",kunnId);//经销商编码
        request.addProperty("IvTknum",null);//运单信息

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(url);
        httpTransport.debug = true;

        try {
            httpTransport.call(SOAP_ACTION_001, envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                //解析后的返回list
                resultList = parseSoapObject001(soapObject);
                return resultList;
            } else if (envelope.bodyIn instanceof SoapFault) {
                SoapFault soapFault = (SoapFault) envelope.bodyIn;
                try {
                    throw new Exception(soapFault.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Description: 解析返回值
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    private static List<Object> parseSoapObject001(SoapObject result) {
        List<Object> list = new ArrayList<>();

        SoapObject EtShipmentT = (SoapObject) result.getProperty("EtShipmentT");
        String EvCode = result.getProperty("EvCode").toString();
        String EvMsg = result.getProperty("EvMsg").toString();
        System.out.println("EvCode:"+EvCode);
        System.out.println("EvMsg:"+EvMsg);
        if (EtShipmentT == null) {
            return null;
        }
        for (int i = 0; i < EtShipmentT.getPropertyCount(); i++) {
            SoapObject soapObject = (SoapObject) EtShipmentT.getProperty(i);
            String Tknum = soapObject.getProperty("Tknum").toString();
            String Name1 = soapObject.getProperty("Name1").toString();
            String Lfimg1 = soapObject.getProperty("Lfimg1").toString();
            String Lfimg2 = soapObject.getProperty("Lfimg2").toString();
            String Ydhrq = soapObject.getProperty("Ydhrq").toString();

            ZsshipmentInTransitH zshipment = new ZsshipmentInTransitH();
            zshipment.setTknum(Tknum);
            zshipment.setName1(Name1);
            zshipment.setLfimg1(Lfimg1);
            zshipment.setLfimg2(Lfimg2);
            zshipment.setYdhrq(Ydhrq);

            list.add(zshipment);
        }
        return list;
    }

}
