/*
 * Copyright 2015 Willian Oki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.github.woki.payments.adyen.error;

/**
 * Handy class for wrapping runtime {@code Exceptions} with a root cause.
 * <p>
 *     This class is {@code abstract} to force the programmer to extend the class. {@code getMessage} will include nested exception
 * information; {@code printStackTrace} and other like methods will delegate to the wrapped exception, if any.
 * </p>
 * <p>
 *     The similarity between this class and the NestedCheckedException class is unavoidable, as Java forces these two classes to have different superclasses
 * (ah, the inflexibility of concrete inheritance!).
 * </p>
 * <p>
 *     Note: 'direct inspiration' from Spring/Core. Used here to avoid dependency on spring-core.
 * </p>
 *
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@SuppressWarnings("serial")
public abstract class NestedRuntimeException extends RuntimeException {
    /**
     * Construct a {@code NestedRuntimeException} with the specified detail message.
     *
     * @param msg the detail message
     */
    public NestedRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Construct a {@code NestedRuntimeException} with the specified detail message and nested exception.
     *
     * @param msg the detail message
     * @param cause the nested exception
     */
    public NestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Return the detail message, including the message from the nested exception if there is one.
     */
    @Override
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }
}
