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
package com.hivemq.bootstrap;

import com.hivemq.common.shutdown.HiveMQShutdownHook;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Dominik Obermaier
 */
public class NettyShutdownHook implements HiveMQShutdownHook {

    private static final Logger log = LoggerFactory.getLogger(NettyShutdownHook.class);

    private final @NotNull EventLoopGroup workerGroup;
    private final @NotNull EventLoopGroup bossGroup;
    private final int timeout;

    public NettyShutdownHook(
            final @NotNull EventLoopGroup workerGroup,
            final @NotNull EventLoopGroup bossGroup,
            final int timeout) {
        this.workerGroup = workerGroup;
        this.bossGroup = bossGroup;
        this.timeout = timeout;
    }

    @Override
    public @NotNull String name() {
        return "Netty Shutdown";
    }

    @Override
    public @NotNull Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public void run() {
        log.debug("Shutting down worker and boss threads");
        final Future<?> workerFinished = workerGroup.shutdownGracefully(2, timeout, TimeUnit.SECONDS); //TimeUnit effects both parameters!
        final Future<?> bossFinished = bossGroup.shutdownGracefully(2, timeout, TimeUnit.SECONDS);

        log.trace("Waiting for Worker threads to finish");
        workerFinished.syncUninterruptibly();
        log.trace("Waiting for Boss threads to finish");
        bossFinished.syncUninterruptibly();
    }
}
