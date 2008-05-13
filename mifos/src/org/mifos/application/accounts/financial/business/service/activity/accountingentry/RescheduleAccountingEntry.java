package org.mifos.application.accounts.financial.business.service.activity.accountingentry;

import org.mifos.application.accounts.financial.business.FinancialActionBO;
import org.mifos.application.accounts.financial.business.GLCodeEntity;
import org.mifos.application.accounts.financial.exceptions.FinancialException;
import org.mifos.application.accounts.financial.util.helpers.FinancialActionCache;
import org.mifos.application.accounts.financial.util.helpers.FinancialActionConstants;
import org.mifos.application.accounts.financial.util.helpers.FinancialConstants;
import org.mifos.application.accounts.loan.business.LoanBO;
import org.mifos.application.accounts.loan.business.LoanTrxnDetailEntity;

public class RescheduleAccountingEntry extends BaseAccountingEntry {

	@Override
	protected void getSpecificAccountActionEntry() throws FinancialException {
		LoanTrxnDetailEntity loanTrxn = (LoanTrxnDetailEntity) financialActivity
				.getAccountTrxn();

		FinancialActionBO finActionReschedule = FinancialActionCache
				.getFinancialAction(FinancialActionConstants.RESCHEDULE);
		addAccountEntryDetails(loanTrxn.getPrincipalAmount(),
				finActionReschedule, getGLcode(finActionReschedule
						.getApplicableDebitCharts()), FinancialConstants.DEBIT);
		GLCodeEntity glcodeCredit = ((LoanBO) loanTrxn.getAccount())
				.getLoanOffering().getPrincipalGLcode();
		addAccountEntryDetails(loanTrxn.getPrincipalAmount(),
				finActionReschedule, glcodeCredit, FinancialConstants.CREDIT);

	}

}
