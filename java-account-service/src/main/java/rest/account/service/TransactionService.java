package rest.account.service;

import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

import rest.account.model.TransactionCollectionResponse;

@Service
public class TransactionService {
	
	private Client transactionsServiceClient;
	private WebTarget transactionsServiceTarget;
	
	public TransactionService(){
		initializeRestClients();
	}
	
	public void initializeRestClients() {

		transactionsServiceClient = ClientBuilder.newClient();
		transactionsServiceTarget = transactionsServiceClient.target("http://localhost:8080/transactionservice");
		
		System.out.println("Initiakized clients and targets...");
	}
	
	public Future<TransactionCollectionResponse> retrieveTransactions(String accountNumber){
		if(null == transactionsServiceTarget){
			System.out.println("Reinitalizing clients");
			this.initializeRestClients();
		}
		Future<TransactionCollectionResponse> response = transactionsServiceTarget
				.path("/accounts/"+accountNumber+"/transactions").request().async()
				.get(TransactionCollectionResponse.class);
		
		return response;
		
	}
}
