package com.tia.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

//Service Endpoint Interface
@WebService
//@SOAPBinding(style = Style.RPC)
public interface IService{
	
	@WebMethod String getHelloWorldAsString();
	
}