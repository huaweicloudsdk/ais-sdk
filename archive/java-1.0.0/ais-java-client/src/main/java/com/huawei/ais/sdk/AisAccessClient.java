package com.huawei.ais.sdk;

import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.common.ProxyHostInfo;

public class AisAccessClient {

	/**
	 * 单例对象
	 */
	private static AisAccessClient instance = new AisAccessClient();

	/**
	 * 是否通过proxy访问
	 */
	private boolean accessWithProxy = false;

	/**
	 * 代理的主机信息
	 */
	private ProxyHostInfo proxyHostInfo = null;

	/**
	 * 基本的认证信息
	 */
	private AuthInfo authInfo = null;

	/**
	 * 获得单例对象
	 * 
	 * @return AisAccessClient instance
	 */
	public static AisAccessClient getInstance() {
		return instance;
	}

	public void init(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	public AisAccess getAccessService(AuthInfo inAuthInfo) {
		return accessWithProxy ? new AisAccessWithProxy(inAuthInfo, proxyHostInfo) : new AisAccess(inAuthInfo);
	}
	
	public AisAccess getAccessService() {
		return accessWithProxy ? new AisAccessWithProxy(authInfo, proxyHostInfo) : new AisAccess(authInfo);
	}

	public AisAccessClient setAccessWithProxy(boolean accessWithProxy) {
		this.accessWithProxy = accessWithProxy;
		return this;
	}

	public AisAccessClient setProxyHostInfo(ProxyHostInfo proxyHostInfo) {
		this.proxyHostInfo = proxyHostInfo;
		return this;
	}
}
