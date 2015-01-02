package akka.account.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.account.model.Account;
import akka.account.model.AccountInfo;
import akka.account.service.AccountDto;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;

@Path("customers")
public class AccountResource {
	
	@Context 
    ActorSystem actorSystem; 
	
//	@PostConstruct
//	public void initializeAkkaSystem() {
//		actorSystem = ActorSystem.create("AccountSystem");
//		actorSystem.actorOf(
//				AccountInfoActor.mkProps().withRouter(new RoundRobinPool(5)),
//				"accountInfoRouter");
//
//		actorSystem.actorOf(
//				AccountInfoActor.mkProps().withRouter(new RoundRobinPool(5)),
//				"transactionsRouter");
//		
//
//	}
    
	@GET
    @Path("/{customerNumber}/accounts/{accountNumber}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void retrieveAccount(
			@Suspended final AsyncResponse asyncResponse,
			@PathParam("customerNumber") String customerNumber,
			@PathParam("accountNumber") String accountNumber) {
		
		AccountDto dto = new AccountDto();
		dto.setCustomerNumber(customerNumber);
		dto.setAccountNumber(accountNumber);
		
		ActorSelection accountInfoActor = actorSystem.actorSelection("/user/accountInfoRouter");
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        System.out.println("Got accountInfoActor:"+accountInfoActor);
        
        ActorSelection transactionsActor = actorSystem.actorSelection("/user/transactionsRouter");
        System.out.println("Got transactionsActor:"+transactionsActor);
        
        Future<Object> accountInfoFuture = Patterns.ask(accountInfoActor, dto, timeout);
//        final Future<Object> transactiosFuture = Patterns.ask(transactionsActor, dto, timeout);
//        
//        Future<Account> accountFuture = accountInfoFuture.zip(transactiosFuture).map(
//        		new Mapper<scala.Tuple2<Object, Object>, Account>() {
//        			public Account apply(scala.Tuple2<Object, Object> zipped) {
//            	
//        				AccountInfo accInfo = (AccountInfo)zipped._1();
//        				TransactionCollectionResponse transCollresponse = (TransactionCollectionResponse)zipped._2();
//        				Account account = new Account();
//        				account.setAccountNumber(accInfo.getAccountNumber());
//        				account.setNickName(accInfo.getNickName());
//        				account.setTransactions(transCollresponse.getTransactions());
//        				
//            	return account;
//              }
//            }, actorSystem.dispatcher()).recover(new Recover<Account>() {
//            	public Account recover(Throwable problem) throws Throwable {
//            			asyncResponse.resume(problem);
//            		    return null;
//            	}
//            }, actorSystem.dispatcher());
        
        System.out.println("calling onSuccess method on accountInfoFuture......:");
        accountInfoFuture.onSuccess(new OnSuccess<Object>() {

			@Override
			public void onSuccess(Object object) throws Throwable {
				//AccountInfo accInfo = (AccountInfo)object;
				System.out.println("onSuccess method:"+ object);
				//Response.ok().entity(object).build();
				Account account = new Account();
				account.setAccountNumber("123123");
				account.setNickName("Nick name");
				asyncResponse.resume(account);
			}
        	
        }, actorSystem.dispatcher());
        //Account account = accountFuture.value().get().get();
        //asyncResponse.resume(account);

    }
}
