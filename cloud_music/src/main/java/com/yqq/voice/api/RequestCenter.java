package com.yqq.voice.api;


import com.yqq.lib_network.okhttp.CommonOkHttpClient;
import com.yqq.lib_network.okhttp.listener.DisposeDataHandle;
import com.yqq.lib_network.okhttp.listener.DisposeDataListener;
import com.yqq.lib_network.okhttp.request.CommonRequest;
import com.yqq.lib_network.okhttp.request.RequestParams;
import com.yqq.voice.model.user.User;

/**
 * 请求中心
 */
public class RequestCenter {

    static class HttpConstants {
        // private static final String ROOT_URL = "http://imooc.com/api";
        private static final String ROOT_URL = "http://39.97.122.129";

        /**
         * 首页请求接口
         */
        private static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";

        private static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

        /**
         * 登陆接口
         */
        public static String LOGIN = ROOT_URL + "/module_voice/login_phone";
    }

    //根据参数发送所有post请求
    public static void getRequest(String url, RequestParams params, DisposeDataListener listener,
                                  Class<?> clazz) {

        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void postRequest (String url, RequestParams params, DisposeDataListener listener,
                                    Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }


    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", "18734924592");
        params.put("pwd", "999999q");
        RequestCenter.getRequest(HttpConstants.LOGIN, params, listener, User.class);
    }
}
