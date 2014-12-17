package rxjava.account.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rxjava.account.model.Account;
import rxjava.account.model.AccountCollectionResponse;
import rxjava.account.model.AccountInfo;
import rxjava.account.model.AccountInfoCollectionResponse;
import rxjava.account.model.Transaction;
import rxjava.account.model.TransactionCollectionResponse;


@Path("customers")
public class AccountResource {

	//private final int THREADS = Runtime.getRuntime().availableProcessors();
	private ExecutorService executor = Executors.newFixedThreadPool(20);
	
    //private TaskExecutor executor = this.getExecutor();

	Client accountInfoServiceClient;
	WebTarget accountInfoServiceTarget;

	Client transactionsServiceClient;
	WebTarget transactionsServiceTarget;

	@PostConstruct
	public void initializeRestClients() {

		accountInfoServiceClient = ClientBuilder.newClient();
		accountInfoServiceTarget = accountInfoServiceClient.target("http://localhost:8080/accountinfoservice");

		transactionsServiceClient = ClientBuilder.newClient();
		transactionsServiceTarget = transactionsServiceClient.target("http://localhost:8080/transactionservice");
		
		System.out.println("Initiakized clients and targets...");
	}

	@GET
	@Path("/{customerNumber}/accounts/{accountNumber}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void retrieveAccount(
			@Suspended final AsyncResponse asyncResponse,
			@PathParam("customerNumber") String customerNumber,
			@PathParam("accountNumber") String accountNumber) {
		
		System.out.println("in retrieveAccount returing string");
		
		Observable<AccountInfo> accountInfo = getAccountInfo(customerNumber, accountNumber);
	    Observable<List<Transaction>> transcations = getTransactions(accountNumber);
	    
	    Observable.zip(
			accountInfo,
			transcations, new Func2<AccountInfo, List<Transaction>, Account>(){
		    	@Override
		        public Account call(AccountInfo accInfo, List<Transaction> xtns) {
		    		Account accountDetail = new Account();
		    		accountDetail.setAccountNumber(accInfo.getAccountNumber());
		    		accountDetail.setNickName(accInfo.getNickName());
		    		accountDetail.setTransactions(xtns);
		    		return accountDetail;
		    	}
		    }).subscribe(new Subscriber<Account>() {
		          @Override
		          public void onCompleted() {
		          }
		  
		          @Override
		          public void onError(Throwable e) {
		              asyncResponse.resume(e);
		          }
	
		          @Override
		          public void onNext(Account account) {
		              asyncResponse.resume(account);
		          }
		});
	}
	
	@GET
	@Path("/{customerNumber}/accounts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void retrieveAccounts(
			@Suspended final AsyncResponse asyncResponse,
			@PathParam("customerNumber") String customerNumber) {
		
		System.out.println("in operation retrieveAccounts ...");
		this.getAllAccounts(customerNumber).toList().subscribe(new Action1<List<Account>>(){

			@Override
			public void call(List<Account> accounts) {
				AccountCollectionResponse response = new AccountCollectionResponse();
				response.setAccounts(accounts);
				asyncResponse.resume(response);
			}});
		
	}
	
	private Observable<Account> getAllAccounts(String customerNumber){
		Observable<AccountInfo> accountInfoObs =this.getAccounts(customerNumber);
		
		return accountInfoObs.flatMap(new Func1<AccountInfo, Observable<Account>>(){

			@Override
			public Observable<Account> call(AccountInfo accountInfo) {
				Observable<List<Transaction>> transObs = getTransactions(accountInfo.getAccountNumber());
				Observable<AccountInfo> accountInfoObs = Observable.just(accountInfo);
				return Observable.zip(accountInfoObs, transObs, new Func2<AccountInfo, List<Transaction>, Account>(){

					@Override
					public Account call(AccountInfo accInfo, List<Transaction> transactions) {
						Account account = new Account();
						account.setAccountNumber(accInfo.getAccountNumber());
						account.setNickName(accInfo.getNickName());
						account.setTransactions(transactions);
						return account;
					}
				});
			}
		});
	}
	
	
	private Observable<AccountInfo> getAccounts(final String customerNumber) {
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
	
	private Observable<AccountInfo> getAccountInfo(final String customerNumber, final String accountNumber) {
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

	private Observable<List<Transaction>> getTransactions(final String accountNumber) {
		return Observable
			.create( (Observable.OnSubscribe<List<Transaction>>) subscriber -> {

				Runnable r = () -> {
					TransactionCollectionResponse response = transactionsServiceTarget.path("/accounts/"+accountNumber+"/transactions").request().get(TransactionCollectionResponse.class);
					subscriber.onNext(response.getTransactions());
					subscriber.onCompleted();
				};
				executor.execute(r);
			});
	}
	
}
