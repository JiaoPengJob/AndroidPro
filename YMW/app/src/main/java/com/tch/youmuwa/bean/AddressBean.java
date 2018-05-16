package com.tch.youmuwa.bean;

import java.util.List;

/**
 * 地区
 */

public class AddressBean {


    private List<ProvinceBean> Province;

    public List<ProvinceBean> getProvince() {
        return Province;
    }

    public void setProvince(List<ProvinceBean> Province) {
        this.Province = Province;
    }

    public static class ProvinceBean {
        /**
         * Name : 北京市
         * City : [{"Name":"北京市","Area":[{"Name":"东城区"},{"Name":"西城区"},{"Name":"崇文区"},{"Name":"宣武区"},{"Name":"朝阳区"},{"Name":"丰台区"},{"Name":"石景山区"},{"Name":"海淀区"},{"Name":"门头沟区"},{"Name":"房山区"},{"Name":"通州区"},{"Name":"顺义区"},{"Name":"昌平区"},{"Name":"大兴区"},{"Name":"怀柔区"},{"Name":"平谷区"},{"Name":"密云县"},{"Name":"延庆县"},{"Name":"延庆镇"}]}]
         */

        private String Name;
        private List<CityBean> City;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public List<CityBean> getCity() {
            return City;
        }

        public void setCity(List<CityBean> City) {
            this.City = City;
        }

        public static class CityBean {
            /**
             * Name : 北京市
             * Area : [{"Name":"东城区"},{"Name":"西城区"},{"Name":"崇文区"},{"Name":"宣武区"},{"Name":"朝阳区"},{"Name":"丰台区"},{"Name":"石景山区"},{"Name":"海淀区"},{"Name":"门头沟区"},{"Name":"房山区"},{"Name":"通州区"},{"Name":"顺义区"},{"Name":"昌平区"},{"Name":"大兴区"},{"Name":"怀柔区"},{"Name":"平谷区"},{"Name":"密云县"},{"Name":"延庆县"},{"Name":"延庆镇"}]
             */

            private String Name;
            private List<AreaBean> Area;

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public List<AreaBean> getArea() {
                return Area;
            }

            public void setArea(List<AreaBean> Area) {
                this.Area = Area;
            }

            public static class AreaBean {
                /**
                 * Name : 东城区
                 */

                private String Name;

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }
            }
        }
    }
}
