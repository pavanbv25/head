/*
 * Copyright (c) 2005-2009 Grameen Foundation USA
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.test.acceptance.loan;

import org.dbunit.dataset.IDataSet;
import org.mifos.test.acceptance.framework.AppLauncher;
import org.mifos.test.acceptance.framework.DbUnitUtilities;
import org.mifos.test.acceptance.framework.MifosPage;
import org.mifos.test.acceptance.framework.UiTestCaseBase;
import org.mifos.test.acceptance.framework.admin.AdminPage;
import org.mifos.test.acceptance.framework.loan.UndoLoanDisbursalEntryPage;
import org.mifos.test.acceptance.framework.loan.UndoLoanDisbursalPreviewPage;
import org.mifos.test.acceptance.framework.loan.UndoLoanDisbursalSearchPage;
import org.mifos.test.acceptance.remote.InitializeApplicationRemoteTestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:ui-test-context.xml"})
@Test(sequential=true, groups={"smoke","holiday","acceptance"})
public class UndoLoanDisbursalTest extends UiTestCaseBase {
    private static final String LOAN_TRXN_DETAIL = "LOAN_TRXN_DETAIL";
    private static final String LOAN_ACTIVITY_DETAILS = "LOAN_ACTIVITY_DETAILS";
    private static final String CLIENT_PERF_HISTORY = "CLIENT_PERF_HISTORY";
    private static final String ACCOUNT_TRXN = "ACCOUNT_TRXN";
    private static final String ACCOUNT_STATUS_CHANGE_HISTORY = "ACCOUNT_STATUS_CHANGE_HISTORY";
    private static final String ACCOUNT_PAYMENT = "ACCOUNT_PAYMENT";
    private static final String ACCOUNT_NOTES = "ACCOUNT_NOTES";
    private static final String ACCOUNT_FLAG_DETAIL = "ACCOUNT_FLAG_DETAIL";
    private static final String ACCOUNT = "ACCOUNT";
    @Autowired
    private DriverManagerDataSource dataSource;
    @Autowired
    private DbUnitUtilities dbUnitUtilities;
    @Autowired
    private InitializeApplicationRemoteTestingService initRemote;

    private AppLauncher appLauncher;
    
    private static final String startDataSet = "acceptance_small_003_dbunit.xml.zip";
    private static final String clientResultDataSet = "UndoLoanDisbursal_001_result_dbunit.xml.zip";
    private static final String groupResultDataSet = "UndoLoanDisbursal_002_result_dbunit.xml.zip";
    
    private static final String clientLoanId = "000100000000121";
    private static final String groupLoanId = "000100000000206 ";
    
    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        appLauncher = new AppLauncher(selenium);
    }

    @AfterMethod
    public void logOut() {
        (new MifosPage(selenium)).logout();
    }
    
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void undoClientLoanDisbursal() throws Exception {
        initRemote.dataLoadAndCacheRefresh(dbUnitUtilities, startDataSet, dataSource, selenium);
        
        undoLoanDisbursal(clientLoanId);
        
        verifyLoanData(clientResultDataSet);
    }
    
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void undoGroupLoanDisbursal() throws Exception {
        initRemote.dataLoadAndCacheRefresh(dbUnitUtilities, startDataSet, dataSource, selenium);
        
        undoLoanDisbursal(groupLoanId);
        
        verifyLoanData(groupResultDataSet);
    }

    
    @SuppressWarnings({ "PMD.SignatureDeclareThrowsException", "unused" })
    private void verifyLoanData(String resultDataSet) throws Exception {
        IDataSet expectedDataSet = dbUnitUtilities.getDataSetFromFile(resultDataSet);
        IDataSet databaseDataSet = dbUnitUtilities.getDataSetForTables(dataSource, new String[] { ACCOUNT, 
                                                                                                  ACCOUNT_FLAG_DETAIL, 
                                                                                                  ACCOUNT_NOTES, 
                                                                                                  ACCOUNT_PAYMENT, 
                                                                                                  ACCOUNT_STATUS_CHANGE_HISTORY, 
                                                                                                  ACCOUNT_TRXN, 
                                                                                                  CLIENT_PERF_HISTORY, 
                                                                                                  LOAN_ACTIVITY_DETAILS, 
                                                                                                  LOAN_TRXN_DETAIL});
        
        
        dbUnitUtilities.verifyTables(new String[] { ACCOUNT, 
                ACCOUNT_FLAG_DETAIL, 
                ACCOUNT_NOTES, 
                ACCOUNT_PAYMENT, 
                ACCOUNT_STATUS_CHANGE_HISTORY, 
                ACCOUNT_TRXN, 
                CLIENT_PERF_HISTORY, 
                LOAN_ACTIVITY_DETAILS, 
                LOAN_TRXN_DETAIL}, databaseDataSet, expectedDataSet);
    }

    private void undoLoanDisbursal(String loanId) {
        AdminPage adminPage = loginAndNavigateToAdminPage();
        adminPage.verifyPage();
        
        UndoLoanDisbursalSearchPage searchPage = adminPage.navigateToUndoLoanDisbursal();
        searchPage.verifyPage();
        
        UndoLoanDisbursalEntryPage entryPage =  searchPage.searchAndNavigateToUndoLoanDisbursalPage(loanId);
        entryPage.verifyPage();
        
        UndoLoanDisbursalPreviewPage confirmationPage = entryPage.submitAndNavigateToUndoLoanDisbursalConfirmationPage("test undo loan disembursal note");
        confirmationPage.verifyPage();
        confirmationPage.submitAndNavigateToAdminPage();
        
        logOut();
    }
    
    private AdminPage loginAndNavigateToAdminPage() {
        return appLauncher
         .launchMifos()
         .loginSuccessfullyUsingDefaultCredentials()
         .navigateToAdminPage();
     }
}
