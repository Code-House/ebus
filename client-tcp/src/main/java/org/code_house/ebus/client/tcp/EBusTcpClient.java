/*
 * (C) Copyright 2017 Code-House, Łukasz Dywicki.
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
package org.code_house.ebus.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.client.api.BusListener;
import org.code_house.ebus.client.api.EBusClient;
import org.code_house.ebus.client.api.EBusClientException;
import org.code_house.ebus.client.api.EBusConfiguration;
import org.code_house.ebus.client.api.PropertyName;
import org.code_house.ebus.client.api.device.EBusDevice;
import org.code_house.ebus.client.api.device.EBusDeviceConfiguration;
import org.code_house.ebus.client.api.participant.Master;
import org.code_house.ebus.client.api.participant.Slave;
import org.code_house.ebus.client.common.CompositeBusListener;
import org.code_house.ebus.client.common.device.SoftDevice;
import org.code_house.ebus.client.common.participant.SimpleMaster;
import org.code_house.ebus.client.common.participant.SimpleSlave;
import org.code_house.ebus.netty.codec.AckTelegramDecoder;
import org.code_house.ebus.netty.codec.InboundEBusHandler;
import org.code_house.ebus.netty.codec.MasterTelegramDecoder;
import org.code_house.ebus.netty.codec.OutboundEBusHandler;
import org.code_house.ebus.netty.codec.SlaveTelegramDecoder;
import org.code_house.ebus.netty.codec.frame.FrameDelimiterDecoder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

public class EBusTcpClient implements EBusClient {

    private final String host;
    private final int port;
    private final Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private List<BusListener> listeners = new CopyOnWriteArrayList<>();

    public EBusTcpClient(EBusConfiguration configuration, String host, int port) {
        this.host = host;
        this.port = port;

        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        final EBusDeviceConfiguration deviceConfiguration = configuration.getDeviceConfiguration();

        bootstrap.group(clientGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            public void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                    new FrameDelimiterDecoder(),
                    new MasterTelegramDecoder(),
                    new SlaveTelegramDecoder(),
                    new AckTelegramDecoder()
                );

                ch.pipeline().addLast(new InboundEBusHandler(configuration.getCommandCodecRegistry(), configuration.getPropertyMap(), new CompositeBusListener(listeners)));
                if (deviceConfiguration != null) {
                    ch.pipeline().addLast(new OutboundEBusHandler(configuration.getCommandCodecRegistry(), createDevice(deviceConfiguration)));
                }
            }
        });
        bootstrap.remoteAddress(host, port);
    }

    private EBusDevice createDevice(EBusDeviceConfiguration deviceConfiguration) {
        SimpleSlave slave = null;
        Master master = new SimpleMaster(deviceConfiguration.getMaster(), (slave = new SimpleSlave(deviceConfiguration.getSlave())));
        slave.setMaster(master);

        return new SoftDevice(master, slave);
    }

    @Override
    public void connect() throws EBusClientException {
        if (channelFuture == null) {
            try {
                channelFuture = bootstrap.connect().sync();
            } catch (InterruptedException e) {
                throw new EBusClientException("Could not open TCP connection to configured host", e);
            }
        }
    }

    @Override
    public void disconnect() throws EBusClientException {
        if (channelFuture != null) {
            try {
                channelFuture.channel().closeFuture().sync();
                channelFuture = null;
            } catch (InterruptedException e) {
                throw new EBusClientException("Connection shutdown reported errors", e);
            }
        }
    }

    @Override
    public void registerBusListener(BusListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public <T, R> Future<PropertyValue<R>> get(PropertyName<T, R> property, PropertyValue<T> value) {
        return null;
    }

    @Override
    public <T, R> Future<PropertyValue<R>> get(PropertyName<T, R> property) {
        return null;
    }

    @Override
    public <T> Future<Boolean> push(PropertyName<T, Boolean> name, PropertyValue<T> value) {
        return null;
    }

    @Override
    public <T> Future<Void> broadcast(PropertyName<T, Void> name, PropertyValue<T> value) {
        return null;
    }
}
