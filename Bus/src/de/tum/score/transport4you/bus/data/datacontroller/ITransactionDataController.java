package de.tum.score.transport4you.bus.data.datacontroller;


import java.util.List;

import de.tum.score.transport4you.bus.data.datacontroller.data.PaymentTransaction;

/**
 * Offers methods to acces the Transactions stored;
 * @author hoerning
 *
 */
public interface ITransactionDataController {
	
	public List<PaymentTransaction> getAllTransactions();

}
