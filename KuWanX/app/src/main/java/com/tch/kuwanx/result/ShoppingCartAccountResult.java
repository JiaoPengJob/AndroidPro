package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/3/20.
 * 购物车结算
 */

public class ShoppingCartAccountResult {

    private String msg;
    private PagingBean paging;
    private ResultBean result;
    private String ret;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PagingBean getPaging() {
        return paging;
    }

    public void setPaging(PagingBean paging) {
        this.paging = paging;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public static class PagingBean {

        private String firstResult;
        private String pageCount;
        private String pageNow;
        private String pageSize;
        private String pagecode;
        private PageindexBean pageindex;
        private String rowCount;
        private String startPage;

        public String getFirstResult() {
            return firstResult;
        }

        public void setFirstResult(String firstResult) {
            this.firstResult = firstResult;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

        public String getPageNow() {
            return pageNow;
        }

        public void setPageNow(String pageNow) {
            this.pageNow = pageNow;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getPagecode() {
            return pagecode;
        }

        public void setPagecode(String pagecode) {
            this.pagecode = pagecode;
        }

        public PageindexBean getPageindex() {
            return pageindex;
        }

        public void setPageindex(PageindexBean pageindex) {
            this.pageindex = pageindex;
        }

        public String getRowCount() {
            return rowCount;
        }

        public void setRowCount(String rowCount) {
            this.rowCount = rowCount;
        }

        public String getStartPage() {
            return startPage;
        }

        public void setStartPage(String startPage) {
            this.startPage = startPage;
        }

        public static class PageindexBean {

            private String endindex;
            private String startindex;

            public String getEndindex() {
                return endindex;
            }

            public void setEndindex(String endindex) {
                this.endindex = endindex;
            }

            public String getStartindex() {
                return startindex;
            }

            public void setStartindex(String startindex) {
                this.startindex = startindex;
            }
        }
    }

    public static class ResultBean {

        private String amt;
        private List<AddressList> addressList;
        private List<GoodListBean> goodList;

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public List<AddressList> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<AddressList> addressList) {
            this.addressList = addressList;
        }

        public List<GoodListBean> getGoodList() {
            return goodList;
        }

        public void setGoodList(List<GoodListBean> goodList) {
            this.goodList = goodList;
        }

        public static class AddressList {

            private String createdate;
            private String detail;
            private String id;
            private String iddefault;
            private String name;
            private String phone;
            private String region;
            private String sex;
            private String userid;

            public String getCreatedate() {
                return createdate;
            }

            public void setCreatedate(String createdate) {
                this.createdate = createdate;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIddefault() {
                return iddefault;
            }

            public void setIddefault(String iddefault) {
                this.iddefault = iddefault;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }
        }

        public static class GoodListBean {

            private String brand_id;
            private String cost_price;
            private String create_id;
            private String create_time;
            private String current_price;
            private String good_cover;
            private String good_desc;
            private String good_intr;
            private String good_name;
            private String good_no;
            private String good_status;
            private String good_type_id;
            private String good_unit;
            private String id;
            private String is_sell_hot;
            private String is_shelves;
            private String sale_num;
            private String spec;
            private String stock_num;

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }

            public String getCreate_id() {
                return create_id;
            }

            public void setCreate_id(String create_id) {
                this.create_id = create_id;
            }

            public String getGood_cover() {
                return good_cover;
            }

            public void setGood_cover(String good_cover) {
                this.good_cover = good_cover;
            }

            public String getGood_desc() {
                return good_desc;
            }

            public void setGood_desc(String good_desc) {
                this.good_desc = good_desc;
            }

            public String getGood_intr() {
                return good_intr;
            }

            public void setGood_intr(String good_intr) {
                this.good_intr = good_intr;
            }

            public String getGood_name() {
                return good_name;
            }

            public void setGood_name(String good_name) {
                this.good_name = good_name;
            }

            public String getGood_no() {
                return good_no;
            }

            public void setGood_no(String good_no) {
                this.good_no = good_no;
            }

            public String getGood_status() {
                return good_status;
            }

            public void setGood_status(String good_status) {
                this.good_status = good_status;
            }

            public String getGood_type_id() {
                return good_type_id;
            }

            public void setGood_type_id(String good_type_id) {
                this.good_type_id = good_type_id;
            }

            public String getGood_unit() {
                return good_unit;
            }

            public void setGood_unit(String good_unit) {
                this.good_unit = good_unit;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_sell_hot() {
                return is_sell_hot;
            }

            public void setIs_sell_hot(String is_sell_hot) {
                this.is_sell_hot = is_sell_hot;
            }

            public String getIs_shelves() {
                return is_shelves;
            }

            public void setIs_shelves(String is_shelves) {
                this.is_shelves = is_shelves;
            }

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }

            public String getCost_price() {
                return cost_price;
            }

            public void setCost_price(String cost_price) {
                this.cost_price = cost_price;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(String current_price) {
                this.current_price = current_price;
            }

            public String getSale_num() {
                return sale_num;
            }

            public void setSale_num(String sale_num) {
                this.sale_num = sale_num;
            }

            public String getStock_num() {
                return stock_num;
            }

            public void setStock_num(String stock_num) {
                this.stock_num = stock_num;
            }
        }
    }
}
