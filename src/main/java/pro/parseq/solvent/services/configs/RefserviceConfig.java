/*******************************************************************************
 *     Copyright 2016-2017 the original author or authors.
 *
 *     This file is part of CONC.
 *
 *     CONC. is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CONC. is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with CONC. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package pro.parseq.solvent.services.configs;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefserviceConfig implements Serializable {

	private static final long serialVersionUID = -2140482836436882208L;

	// TODO: consider defensive copying on deserialization (see Item 76 from Effective Java)
	@Value("${refservice.remote.connection.scheme}")
	private String connectionScheme;

	@Value("${refservice.remote.connection.host}")
	private String connectionHost;

	@Value("${refservice.remote.connection.port}")
	private int connectionPort;

	@Value("${refservice.remote.api.root}")
	private String apiRoot;

	@Value("${refservice.remote.api.version}")
	private String apiVersion;

	@Value("${refservice.remote.api.endpoints.references}")
	private String referencesEndpoint;

	@Value("${refservice.local.path}")
	private String referencesPath;

	protected RefserviceConfig() {}

	public String getConnectionScheme() {
		return connectionScheme;
	}

	public void setConnectionScheme(String connectionScheme) {
		this.connectionScheme = connectionScheme;
	}

	public String getConnectionHost() {
		return connectionHost;
	}

	public void setConnectionHost(String connectionHost) {
		this.connectionHost = connectionHost;
	}

	public int getConnectionPort() {
		return connectionPort;
	}

	public void setConnectionPort(int connectionPort) {
		this.connectionPort = connectionPort;
	}

	public String getApiRoot() {
		return apiRoot;
	}

	public void setApiRoot(String apiRoot) {
		this.apiRoot = apiRoot;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getReferencesEndpoint() {
		return referencesEndpoint;
	}

	public void setReferencesEndpoint(String referencesEndpoint) {
		this.referencesEndpoint = referencesEndpoint;
	}

	public String getReferencesPath() {
		return referencesPath;
	}

	public void setReferencesPath(String referencesPath) {
		this.referencesPath = referencesPath;
	}
}
