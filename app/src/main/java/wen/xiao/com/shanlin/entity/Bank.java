package wen.xiao.com.shanlin.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class Bank {

    /**
     * message : 操作成功
     * data : {"bankTypeList":[{"value":"中国工商银行","code":"01020000"},{"value":"中国银行","code":"01040000"},{"value":"中国建设银行","code":"01050000"},{"value":"交通银行","code":"03010000"},{"value":"中信银行","code":"03020000"},{"value":"光大银行","code":"03030000"},{"value":"华夏银行","code":"03040000"},{"value":"民生银行","code":"03050000"},{"value":"广发银行","code":"03060000"},{"value":"平安银行","code":"03070000"},{"value":"招商银行","code":"03080000"}]}
     * code : 200
     */

    private String message;
    private DataBean data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        private List<BankTypeListBean> bankTypeList;

        public List<BankTypeListBean> getBankTypeList() {
            return bankTypeList;
        }

        public void setBankTypeList(List<BankTypeListBean> bankTypeList) {
            this.bankTypeList = bankTypeList;
        }

        public static class BankTypeListBean {
            /**
             * value : 中国工商银行
             * code : 01020000
             */

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
}
