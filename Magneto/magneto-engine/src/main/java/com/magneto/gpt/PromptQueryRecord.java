package com.magneto.gpt;

import com.magneto.staticanalysis.MethodCall;
import lombok.NonNull;

import java.util.Objects;

class PromptQueryRecord {
    private final MethodCall targetMethodCall;

    private final MethodCall triggeredMethodCall;

    public PromptQueryRecord(@NonNull MethodCall targetMethodCall, @NonNull MethodCall triggeredMethodCall) {
        this.targetMethodCall = targetMethodCall;
        this.triggeredMethodCall = triggeredMethodCall;
    }

    public MethodCall getTargetMethodCall() {
        return targetMethodCall;
    }

    public MethodCall getTriggeredMethodCall() {
        return triggeredMethodCall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PromptQueryRecord that = (PromptQueryRecord) o;

        return Objects.equals(targetMethodCall.getMethodSignature(), that.targetMethodCall.getMethodSignature()) &&
                Objects.equals(triggeredMethodCall.getMethodSignature(), that.triggeredMethodCall.getMethodSignature());
    }

    @Override
    public int hashCode() {
        int result = targetMethodCall.getMethodSignature().hashCode();
        result = 31 * result + triggeredMethodCall.getMethodSignature().hashCode();
        return result;
    }
}
