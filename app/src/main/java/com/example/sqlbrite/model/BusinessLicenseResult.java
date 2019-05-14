package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class BusinessLicenseResult {

    public BusinessLicenseResultWordsResult words_result;

    public class BusinessLicenseResultWordsResult {

        @SerializedName("注册资本")
        public RegisteredCapital registeredCapital;

        @SerializedName("社会信用代码")
        public SocialCreditCode socialCreditCode;

        @SerializedName("单位名称")
        public CompanyName companyName;

        @SerializedName("法人")
        public LegalPerson legalPerson;

        @SerializedName("证件编号")
        public DocumentNumber documentNumber;

        @SerializedName("组成形式")
        public Formation formation;

        @SerializedName("成立日期")
        public DateOfEstablishment dateOfEstablishment;

        @SerializedName("地址")
        public CompanyAddress companyAddress;

        @SerializedName("经营范围")
        public BusinessScope businessScope;

        @SerializedName("类型")
        public TypeOfCompany typeOfCompany;

        @SerializedName("有效期")
        public BusinessLicenseValidityPeriod businessLicenseValidityPeriod;

        public class RegisteredCapital {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class SocialCreditCode {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class CompanyName {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class LegalPerson {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class DocumentNumber {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class Formation {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class DateOfEstablishment {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class CompanyAddress {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class TypeOfCompany {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class BusinessScope {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }

        public class BusinessLicenseValidityPeriod {
            private String words;

            public void setWords(String words) {
                this.words = words;
            }

            public String getWords() {
                return words;
            }
        }
    }
}
