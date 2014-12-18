package java.account.service;

import java.account.model.AccountInfo;
import java.account.model.AccountInfoCollectionResponse;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

@Service
public class AccountInfoService {
	
	private Client accountInfoServiceClient;
	private WebTarget accountInfoServiceTarget;

	@PostConstruct
	public void initializeRestClients() {

		accountInfoServiceClient = ClientBuilder.newClient();
		accountInfoServiceTarget = accountInfoServiceClient.target("http://localhost:8080/accountinfoservice");

		System.out.println("Initiakized clients and targets...");
	}
	
	public Future<AccountInfo> retrieveAccountInfo(String customerNumber, String accountNumber){
		Future<AccountInfo> accountInfoFuture = accountInfoServiceTarget.
				path("/customers/"+customerNumber+"/accounts/"+accountNumber).
				request().async().get(AccountInfo.class);
		
		return accountInfoFuture;
	}
	
	public Future<AccountInfoCollectionResponse> retrieveAccountInfoCollection(String customerNumber){
		Future<AccountInfoCollectionResponse> accountInfoCollResponse = accountInfoServiceTarget.
				path("/customers/"+customerNumber+"/accounts").
				request().async().get(AccountInfoCollectionResponse.class);
		
		return accountInfoCollResponse;
	}
}
