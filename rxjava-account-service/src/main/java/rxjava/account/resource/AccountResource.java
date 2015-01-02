package rxjava.account.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rxjava.account.model.Account;
import rxjava.account.model.AccountCollectionResponse;
import rxjava.account.service.AccountService;


@Path("customers")
public class AccountResource {
	
	private AccountService accountService = new AccountService();

	@GET
	@Path("/{customerNumber}/accounts/{accountNumber}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void retrieveAccount(
			@Suspended final AsyncResponse asyncResponse,
			@PathParam("customerNumber") String customerNumber,
			@PathParam("accountNumber") String accountNumber) {
		
		System.out.println("in retrieveAccount returing string");
		
		Observable<Account> account = accountService.retrieveAccount(customerNumber, accountNumber);
		account.subscribe(new Subscriber<Account>() {
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
		
		accountService.retrieveAccounts(customerNumber).toList().subscribe(new Action1<List<Account>>(){

			@Override
			public void call(List<Account> accounts) {
				AccountCollectionResponse response = new AccountCollectionResponse();
				response.setAccounts(accounts);
				asyncResponse.resume(response);
			}});
		
	}
	
}
