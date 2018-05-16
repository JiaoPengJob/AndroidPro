package com.tch.zx.bean;

/**
 * Created by peng on 2017/8/30.
 */

public class AliPayResult {

    /**
     * responseObject : app_id=2017090608585644&biz_content=%7B%22total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%B3%95%E5%BE%8B12%22%2C%22out_trade_no%22%3A%22D201710252211416404%22%7D&charset=UTF-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.93.191.99%3A9001%2Fzhixian_api%2Forder%2FalipayNotifyMethod.jhtml&sign_type=RSA2&timestamp=2017-10-25+22%3A11%3A41&version=1.0&sign=A71TdoTZWovKrXfvciv5B%2BLE%2BF4MCKGvGNGYfRCCqT4txZQMQSymZFeDEIwsRaXiNJCbUS13xter5WNGmmmNpFsbqfmRJQgiAnmkoCPxfRrrh3e72SWCfLASBYl7IKFTWhYNdkGagHRsjmG0Q0sWjY3%2FNHycjaR%2BkpQrZwCTm856kI8c7ytvvNtIb7%2Bg7I9Xt0EboCTTY2YEru%2BYhzIati9%2FBq18q87Fqh3JmfVDCS4ooG4%2FWUGHHZcKVl6sDySXehKqJqLo2ncWzGRX%2BLCm46XGw0uR0%2BfZnJDuUYuFZOeM%2FCLaVe6TgWUl1QDUq4rVhvgsfYjTtD9IoW7vMvt%2BnQ%3D%3D
     */

    private String responseObject;

    public String getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(String responseObject) {
        this.responseObject = responseObject;
    }
}
