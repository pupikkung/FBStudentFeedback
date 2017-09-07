package com.fbsearch.domains;

import java.util.Objects;

public class Application {

    private int id;
    private String appCd;
    private String desc;
    private String url;
    private long token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppCd() {
        return appCd;
    }

    public void setAppCd(String appCd) {
        this.appCd = appCd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.appCd);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Application other = (Application) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.appCd, other.appCd)) {
            return false;
        }
        return true;
    }

}
