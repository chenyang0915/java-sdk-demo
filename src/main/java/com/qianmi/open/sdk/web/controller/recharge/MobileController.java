package com.qianmi.open.sdk.web.controller.recharge;

import com.qianmi.open.api.ApiException;
import com.qianmi.open.api.DefaultOpenClient;
import com.qianmi.open.api.OpenClient;
import com.qianmi.open.api.request.RechargeMobileCreateBillRequest;
import com.qianmi.open.api.request.RechargeMobileGetItemInfoRequest;
import com.qianmi.open.api.request.RechargeMobileGetItemListRequest;
import com.qianmi.open.api.response.RechargeMobileCreateBillResponse;
import com.qianmi.open.api.response.RechargeMobileGetItemInfoResponse;
import com.qianmi.open.api.response.RechargeMobileGetItemListResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 话费充值
 * Created by qmopen on 16/1/9.
 */
@Controller
@RequestMapping("/mobile")
public class MobileController {

    private static final String url = "url";
    private static final String appKey = "{你的APPKEY}";
    private static final String appSecret = "{你的appSecret}";
    private static final String accessToken = "{你的accessToken}";


    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "mobile-recharge";
    }


    /**
     * 第一步,根据手机号码加上面值取得支持该号码的商品
     * 1.返回指定面值，手机号所在区域下优先级最高商品，优先级："市>省>全国，固定面值>任意充"
     * 2.在同样充值金额下，满足客户选择不同商品需求，可选择与 "查询话费直充商品列表"接口分场景使用
     * 3.可放弃此步骤，直接根据手机号码、充值金额直接生成订单(知道商品编号的前提下)。
     *
     * @throws ApiException
     */
    @RequestMapping(value = "/itemInfo")
    public Object mobileGetItemInfo(String mobileNo, String rechargeAmount) throws ApiException {

        OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
        RechargeMobileGetItemInfoRequest req = new RechargeMobileGetItemInfoRequest();
        req.setMobileNo(mobileNo);
        req.setRechargeAmount(rechargeAmount);
        RechargeMobileGetItemInfoResponse response = client.execute(req, accessToken);
        return response;
    }

//    /**
//     * 第二步,根据选择的面值获取商品详情
//     *
//     * @throws ApiException
//     */
//
//    public void mobileGetItemInfo() throws ApiException {
//        OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
//        RechargeMobileGetItemInfoRequest req = new RechargeMobileGetItemInfoRequest();
//        req.setMobileNo("13333333333");
//        req.setRechargeAmount("100");
//        RechargeMobileGetItemInfoResponse response = client.execute(req, accessToken);
//    }

    /**
     * 第二步,调用下单接口
     * 调用此接口，生成话费充值订单，并且返回订单详情
     *
     * @throws ApiException
     */
    @RequestMapping(value = "/createBill")
    public Object mobileCreateBill(String itemId , String mobileNo,String rechargeAmount) throws ApiException {
        OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
        RechargeMobileCreateBillRequest req = new RechargeMobileCreateBillRequest();
        req.setItemId(itemId);
        req.setMobileNo(mobileNo);
        req.setRechargeAmount(rechargeAmount);
        RechargeMobileCreateBillResponse response = client.execute(req, accessToken);
        return  response;
    }

    public static void main(String[] args) {
        try {
            String mobileNo = "13770786502";
            String rechargeAmout = "50";
            new MobileController().mobileGetItemInfo(mobileNo,rechargeAmout);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}