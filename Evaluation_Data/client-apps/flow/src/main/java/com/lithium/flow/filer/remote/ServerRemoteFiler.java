/*
 * Copyright 2015 Lithium Technologies, Inc.
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

package com.lithium.flow.filer.remote;

import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.io.DataIo;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.healthmarketscience.rmiio.SerializableInputStream;
import com.healthmarketscience.rmiio.SerializableOutputStream;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;

/**
 * @author Matt Ayres
 */
public class ServerRemoteFiler extends DecoratedFiler implements RemoteFiler, Serializable {
	private static final Logger log = Logs.getLogger();
	private static final long serialVersionUID = 4838720717701156008L;

	public ServerRemoteFiler(@Nonnull Filer delegate, int port, int localPort) throws RemoteException {
		super(delegate);
		bypassDelegateFind = true;

		log.info("port: {}, local port: {}", port, localPort);
		UnicastRemoteObject.exportObject(this, localPort);
		LocateRegistry.createRegistry(port).rebind(RemoteFiler.BIND, this);
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		return new SerializableInputStream(new SimpleRemoteInputStream(super.readFile(path)));
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		return new SerializableOutputStream(new SimpleRemoteOutputStream(super.writeFile(path)));
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) {
		throw new UnsupportedOperationException("openFile not implemented yet");
	}

	@Override
	public void close() {
		// ignore
	}
}
