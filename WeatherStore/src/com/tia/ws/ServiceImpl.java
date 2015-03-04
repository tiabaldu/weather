package com.tia.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "com.tia.ws.IService")
public class ServiceImpl implements IService{

	@Override
	public String getHelloWorldAsString() {
		return "Hello World JAX-WS";
	}
}