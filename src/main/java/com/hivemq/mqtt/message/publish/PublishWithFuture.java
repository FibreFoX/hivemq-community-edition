/*
 * Copyright 2019-present HiveMQ GmbH
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
 */
package com.hivemq.mqtt.message.publish;

import com.google.common.util.concurrent.SettableFuture;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.mqtt.handler.publish.PublishStatus;
import com.hivemq.persistence.payload.PublishPayloadPersistence;

/**
 * @author Christoph Schäbel
 */
public class PublishWithFuture extends PUBLISH {

    @NotNull
    private final SettableFuture<PublishStatus> future;
    private final boolean shared;
    private final boolean flush;

    public PublishWithFuture(@NotNull final PUBLISH publish,
                             @NotNull final SettableFuture<PublishStatus> future,
                             final boolean shared) {
        this(publish, future, shared, null);
    }

    public PublishWithFuture(@NotNull final PUBLISH publish,
                             @NotNull final SettableFuture<PublishStatus> future,
                             final boolean shared,
                             @Nullable final PublishPayloadPersistence persistence) {
        this(publish, future, shared, persistence, true);
    }

    public PublishWithFuture(@NotNull final PUBLISH publish,
                             @NotNull final SettableFuture<PublishStatus> future,
                             final boolean shared,
                             @Nullable final PublishPayloadPersistence persistence,
                             final boolean flush) {
        super(publish, persistence);
        this.future = future;
        this.shared = shared;
        this.flush = flush;
    }

    @NotNull
    public SettableFuture<PublishStatus> getFuture() {
        return future;
    }

    public boolean isShared() {
        return shared;
    }

    public boolean isFlush() {
        return flush;
    }
}
