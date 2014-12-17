package rest.accountinfo.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//@Path("accounts")
@Path("customers")
public class AccountInfoResource {

    @GET
	@Path("/{customerNumber}/accounts/{accountNumber}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccountInfo getAccountInfo(@PathParam("accountNumber") String accountNumber) {
    	AccountInfo info = new AccountInfo();
    	info.setAccountNumber(accountNumber);
    	info.setNickName("My Account");
        return info;
    }
    
    @GET
    @Path("/{customerNumber}/accounts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccountInfoCollectionResponse getAccounts(@PathParam("customerNumber") String customerNumber) {
    	AccountInfoCollectionResponse response = new AccountInfoCollectionResponse();
    	response.setAccounts(this.getAccountInfos(customerNumber));
        return response;
    }
    
    private List<AccountInfo> getAccountInfos(String customerNumber){
    	List<AccountInfo> accounts = new ArrayList<AccountInfo>();
    	
    	for(int i=0; i<3; i++){
    		AccountInfo acc1 = new AccountInfo();
        	acc1.setAccountNumber(customerNumber+": Acc "+i);
        	acc1.setNickName("My Account:"+acc1.getAccountNumber());
        	accounts.add(acc1);
    	}
    	
    	return accounts;
    }
}
