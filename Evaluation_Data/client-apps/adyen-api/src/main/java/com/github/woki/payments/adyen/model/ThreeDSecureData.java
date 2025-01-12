package com.github.woki.payments.adyen.model;


import com.github.woki.payments.adyen.PublicApi;
import com.github.woki.payments.adyen.ToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("serial")
@PublicApi
public class ThreeDSecureData implements Serializable {
    private String authenticationResponse;
    private String cavv;
    private String cavvAlgorithm;
    private String directoryResponse;
    private String eci;
    private String xid;

    @PublicApi
    public ThreeDSecureData() {
    }

    @PublicApi
    public String getAuthenticationResponse() {
        return authenticationResponse;
    }

    @PublicApi
    public void setAuthenticationResponse(String authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
    }

    @PublicApi
    public String getCavv() {
        return cavv;
    }

    @PublicApi
    public void setCavv(String cavv) {
        this.cavv = cavv;
    }

    @PublicApi
    public String getCavvAlgorithm() {
        return cavvAlgorithm;
    }

    @PublicApi
    public void setCavvAlgorithm(String cavvAlgorithm) {
        this.cavvAlgorithm = cavvAlgorithm;
    }

    @PublicApi
    public String getDirectoryResponse() {
        return directoryResponse;
    }

    @PublicApi
    public void setDirectoryResponse(String directoryResponse) {
        this.directoryResponse = directoryResponse;
    }

    @PublicApi
    public String getEci() {
        return eci;
    }

    @PublicApi
    public void setEci(String eci) {
        this.eci = eci;
    }

    @PublicApi
    public String getXid() {
        return xid;
    }

    @PublicApi
    public void setXid(String xid) {
        this.xid = xid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("authenticationResponse", authenticationResponse).append("cavv", cavv)
                .append("cavvAlgorithm", cavvAlgorithm).append("directoryResponse", directoryResponse).append("eci", eci).append("xid", xid).toString();
    }
}
