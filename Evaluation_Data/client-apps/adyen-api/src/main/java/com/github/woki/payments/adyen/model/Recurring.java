package com.github.woki.payments.adyen.model;

import com.github.woki.payments.adyen.PublicApi;
import com.github.woki.payments.adyen.ToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("serial")
@PublicApi
public class Recurring implements Serializable {
    private ContractType contract;
    private String recurringDetailName;

    @PublicApi
    public Recurring() {
    }

    public Recurring(ContractType contract, String recurringDetailName) {
        this.contract = contract;
        this.recurringDetailName = recurringDetailName;
    }

    @PublicApi
    public ContractType getContract() {
        return contract;
    }

    @PublicApi
    public void setContract(ContractType contract) {
        this.contract = contract;
    }

    @PublicApi
    public String getRecurringDetailName() {
        return recurringDetailName;
    }

    @PublicApi
    public void setRecurringDetailName(String recurringDetailName) {
        this.recurringDetailName = recurringDetailName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("contract", contract).append("recurringDetailName", recurringDetailName).toString();
    }
}
