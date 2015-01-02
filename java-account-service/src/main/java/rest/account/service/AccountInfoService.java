package rest.account.service;

import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

import rest.account.model.AccountInfo;
import rest.account.model.AccountInfoCollectionResponse;

@Service
public class AccountInfoService {
	
	private Client accountInfoServiceClient;
	private WebTarget accountInfoServiceTarget;

	public AccountInfoService(){
		initializeRestClients();
	}
	
	public void initializeRestClients() {

		accountInfoServiceClient = ClientBuilder.newClient();
		accountInfoServiceTarget = accountInfoServiceClient.target("http://localhost:8080/accountinfoservice");

		System.out.println("Initiakized clients and targets...");
	}
	
	public Future<AccountInfo> retrieveAccountInfo(String customerNumber, String accountNumber){
		if(null == accountInfoServiceTarget){
			System.out.println("Reinitalizing clients");
			initializeRestClients();
		}
		
		Future<AccountInfo> accountInfoFuture = accountInfoServiceTarget.
				path("/customers/"+customerNumber+"/accounts/"+accountNumber).
				request().async().get(AccountInfo.class);
		
		return accountInfoFuture;
	}
	
	public Future<AccountInfoCollectionResponse> retrieveAccountInfoCollection(String customerNumber){
		if(null == accountInfoServiceTarget){
			System.out.println("Reinitalizing clients");
			initializeRestClients();
		}
		
		Future<AccountInfoCollectionResponse> accountInfoCollResponse = accountInfoServiceTarget.
				path("/customers/"+customerNumber+"/accounts").
				request().async().get(AccountInfoCollectionResponse.class);
		
		return accountInfoCollResponse;
	}
}
