package rxjava.account.service;

import java.util.concurrent.ExecutorService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import rx.Observable;
import rxjava.account.model.AccountInfo;
import rxjava.account.model.AccountInfoCollectionResponse;

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
	
	public Observable<AccountInfo> retrieveAccountInfo(String customerNumber, String accountNumber, ExecutorService executor){
		if(null == accountInfoServiceTarget){
			System.out.println("Reinitalizing clients");
			initializeRestClients();
		}
		
		System.out.println("called getAccountInfo....");
		return Observable
			.create((Observable.OnSubscribe<AccountInfo>) subscriber -> {
				Runnable r = () -> {
					subscriber.onNext(accountInfoServiceTarget.path("/customers/"+customerNumber+"/accounts/"+accountNumber).request().get(AccountInfo.class));
					subscriber.onCompleted();
				};
				executor.execute(r);
			});
	}
	
	
	
	public Observable<AccountInfo> retrieveAccounts(final String customerNumber, ExecutorService executor) {
		System.out.println("called getAccounts....");
		return Observable
			.create((Observable.OnSubscribe<AccountInfo>) subscriber -> {
				Runnable r = () -> {
					AccountInfoCollectionResponse response = accountInfoServiceTarget.path("/customers/"+customerNumber+"/accounts").request().get(AccountInfoCollectionResponse.class);
					for(AccountInfo accountInfo: response.getAccounts()){
						subscriber.onNext(accountInfo);
						subscriber.onCompleted();
					}
				};
				executor.execute(r);
			});
	}
}
