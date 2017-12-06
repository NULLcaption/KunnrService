package com.cxg.kunnr.kunnr;

import android.support.v4.media.MediaMetadataCompat;

import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.ResultMsgSap;
import com.cxg.kunnr.kunnr.activity.query.Zskunnr;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;
import com.cxg.kunnr.kunnr.activity.utils.BitmapSampleUtil;
import com.cxg.kunnr.kunnr.activity.utils.HttpUtil;
import com.cxg.kunnr.kunnr.activity.utils.WebserviceUtils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testHttpRequest() {

    }

    @Test
    public void CheckTime () {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        Calendar cale;
        String firstday,nowTime;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        //获取当前时间
        nowTime = format.format(new Date());
        //判断时间
        String checkTime = "2017-12-02";

        System.out.println(nowTime);
        System.out.println(firstday);

        System.out.println(checkTime.compareTo(firstday));
        System.out.println(checkTime.compareTo(nowTime));

        if (checkTime.compareTo(firstday)>0 && checkTime.compareTo(nowTime)<=0 || checkTime.compareTo(firstday)==0) {
            System.out.println("true");
        }

    }

    @Test
    public void PodOk () {
        String Tknum = "0000117072";
        String podDate = "2017-11-29";
        ResultMsgSap resultMsgSap = new ResultMsgSap();
        List<Object> list = WebserviceUtils.callWebservice003(WebserviceUtils.URL_003,WebserviceUtils.METHOD_NAME_003,Tknum,podDate);
        if (list.size() != 0) {
            resultMsgSap = (ResultMsgSap) list.get(0);
        }
        System.out.println(resultMsgSap);
    }

    @Test
    public void PodInfo () {
        String kunnId = "08150002";
        List<ZsshipmentInTransitH> zsshipmentInTransitHList = new ArrayList<>();
        ZsshipmentInTransitH zsshipmentInTransitH;
        List<Object> list = WebserviceUtils.callWebservice001(WebserviceUtils.URL_001,WebserviceUtils.METHOD_NAME_001,kunnId);
        if (list.size() != 0) {
            for (int i = 1; i < list.size(); i++) {
                zsshipmentInTransitH = (ZsshipmentInTransitH) list.get(i);
                zsshipmentInTransitHList.add(zsshipmentInTransitH);
            }
        }
        System.out.println(zsshipmentInTransitHList);
    }

    @Test
    public void PodDetailInfo() {
        String Tknum ="0000117328";
        List<ZsshipmentInTransit> zsshipmentInTransitList = new ArrayList<>();
        ZsshipmentInTransit zsshipmentInTransit;
        List<Object> list = WebserviceUtils.callWebservice002(WebserviceUtils.URL_001,WebserviceUtils.METHOD_NAME_001,Tknum);
        if (list.size() != 0) {
            for (int i = 1; i < list.size(); i++) {
                zsshipmentInTransit = (ZsshipmentInTransit) list.get(i);
                zsshipmentInTransitList.add(zsshipmentInTransit);
            }
        }
        System.out.println(zsshipmentInTransitList);
    }


}