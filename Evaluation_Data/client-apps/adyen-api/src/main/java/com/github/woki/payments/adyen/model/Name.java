package com.github.woki.payments.adyen.model;

import com.github.woki.payments.adyen.PublicApi;
import com.github.woki.payments.adyen.ToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("serial")
@PublicApi
public class Name implements Serializable {
    private String firstName;
    private String lastName;
    private GenderType gender = GenderType.U;
    private String infix;

    @PublicApi
    public Name() {
    }

    @PublicApi
    public String getFirstName() {
        return firstName;
    }

    @PublicApi
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PublicApi
    public String getLastName() {
        return lastName;
    }

    @PublicApi
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @PublicApi
    public GenderType getGender() {
        return gender;
    }

    @PublicApi
    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    @PublicApi
    public String getInfix() {
        return infix;
    }

    @PublicApi
    public void setInfix(String infix) {
        this.infix = infix;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("firstName", firstName).append("lastName", lastName).append("gender", gender)
                .append("infix", infix).toString();
    }
}
