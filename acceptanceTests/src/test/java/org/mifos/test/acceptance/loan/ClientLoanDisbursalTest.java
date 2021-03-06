/*
 * Copyright (c) 2005-2011 Grameen Foundation USA
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

import org.mifos.test.acceptance.framework.MifosPage;
import org.mifos.test.acceptance.framework.UiTestCaseBase;
import org.mifos.test.acceptance.framework.admin.AdminPage;
import org.mifos.test.acceptance.framework.admin.DefineAcceptedPaymentTypesPage;
import org.mifos.test.acceptance.framework.loan.CreateLoanAccountSearchParameters;
import org.mifos.test.acceptance.framework.loan.DisburseLoanPage;
import org.mifos.test.acceptance.framework.loan.LoanAccountPage;
import org.mifos.test.acceptance.framework.testhelpers.LoanTestHelper;
import org.mifos.test.acceptance.framework.testhelpers.NavigationHelper;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("PMD")
@ContextConfiguration(locations = {"classpath:ui-test-context.xml"})
@Test(singleThreaded = true, groups = {"acceptance", "ui", "loan", "no_db_unit"})
public class ClientLoanDisbursalTest extends UiTestCaseBase {
    private LoanTestHelper loanTestHelper;

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    // one of the dependent methods throws Exception
    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        super.setUp();

        loanTestHelper = new LoanTestHelper(selenium);
    }

    @AfterMethod(alwaysRun = true)
    public void logOut() {
        (new MifosPage(selenium)).logout();
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    // http://mifosforge.jira.com/browse/MIFOSTEST-249
    public void verifyAcceptedPaymentTypesForDisbursementsOfLoan() throws Exception {
        // When
        NavigationHelper navigationHelper = new NavigationHelper(selenium);
        AdminPage adminPage = navigationHelper.navigateToAdminPage();
        DefineAcceptedPaymentTypesPage defineAcceptedPaymentTypesPage = adminPage.navigateToDefineAcceptedPaymentType();
        defineAcceptedPaymentTypesPage.addLoanDisbursementsPaymentType(defineAcceptedPaymentTypesPage.CHEQUE);

        adminPage = navigationHelper.navigateToAdminPage();
        defineAcceptedPaymentTypesPage = adminPage.navigateToDefineAcceptedPaymentType();
        defineAcceptedPaymentTypesPage.addLoanDisbursementsPaymentType(defineAcceptedPaymentTypesPage.VOUCHER);

        LoanTestHelper loanTestHelper = new LoanTestHelper(selenium);
        CreateLoanAccountSearchParameters searchParams = new CreateLoanAccountSearchParameters();
        searchParams.setLoanProduct("WeeklyFlatLoanWithOneTimeFees");
        searchParams.setSearchString("Stu1233266063395 Client1233266063395");
        LoanAccountPage loanAccountPage = loanTestHelper.createAndActivateDefaultLoanAccount(searchParams);
        DisburseLoanPage disburseLoanPage = loanAccountPage.navigateToDisburseLoan();
        //Then
        disburseLoanPage.verifyModeOfPayments();
        //When
        disburseLoanPage = navigationHelper.navigateToLoanAccountPage("000100000000020").navigateToDisburseLoan();
        //Then
        disburseLoanPage.verifyModeOfPayments();
        disburseLoanPage.verifyPaymentModesOfPaymentAreEmpty();
        disburseLoanPage.verifyPaymentModeOfPaymentIsEditable(
                "payment mode of payment must be editable when a disbursal fee exists.");

    }
}
