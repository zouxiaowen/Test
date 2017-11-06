package wen.xiao.com.shanlin.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class GG {

    private List<KK> bankTypeList;

    public List<KK> getBankTypeList() {
        return bankTypeList;
    }

    public void setBankTypeList(List<KK> bankTypeList) {
        this.bankTypeList = bankTypeList;
    }
    public class KK {
        private String value;
        private String code;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

