package com.cay.Model;

public class BaseEntity {
	private static final String sysCode = "800";
    private String code;
    private String subCode;
    private String msg;
    private Boolean ok;



	public BaseEntity() {
        setOk();
    }

    public BaseEntity(Boolean ok, String code, String msg) {
        this.code = sysCode + code;
        this.subCode = "0";
        this.msg = msg;
        this.ok = ok;
    }

    public BaseEntity(Boolean ok, String code, String subCode, String msg) {
        this.code = sysCode + code;
        this.subCode = subCode;
        this.msg = msg;
        this.ok = ok;
    }

    public void setOk() {
        code = "0";
        subCode = "0";
        msg = "OK";
        ok = true;
    }

    public boolean success() {
        return "0".equals(code);
    }

    public void setErr(String code, String msg) {
        this.code = sysCode + code;
        this.subCode = "0";
        this.ok = false;
        this.msg = msg;
    }

    public void setErr(String code, String subCode, String msg) {
        this.code = sysCode + code;
        this.subCode = subCode;
        this.ok = false;
        this.msg = msg;
    }

    public Boolean getOk() {
		return ok;
	}

	public void setOk(Boolean ok) {
		this.ok = ok;
	}
	
    public String getCode() {
        return code;
    }    

    public void setCode(String code) {
        if (code == null) {
            this.code = null;
        } else if ("0".equals(code) || code.length() == 6) {
            this.code = code;
        } else {
            this.code = sysCode + code;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public static String getSyscode() {
        return sysCode;
    }
}
