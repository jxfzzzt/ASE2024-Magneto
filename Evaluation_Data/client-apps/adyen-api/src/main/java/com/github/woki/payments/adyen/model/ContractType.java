package com.github.woki.payments.adyen.model;

import com.github.woki.payments.adyen.PublicApi;

@PublicApi
public enum ContractType {
    @PublicApi ONECLICK,
    @PublicApi RECURRING,
    @PublicApi PAYOUT
}
