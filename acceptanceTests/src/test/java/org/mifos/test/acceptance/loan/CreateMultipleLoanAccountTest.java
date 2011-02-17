/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
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

import org.mifos.framework.util.DbUnitUtilities;
import org.mifos.test.acceptance.center.CenterStatus;
import org.mifos.test.acceptance.framework.MifosPage;
import org.mifos.test.acceptance.framework.UiTestCaseBase;
import org.mifos.test.acceptance.framework.center.CreateCenterEnterDataPage;
import org.mifos.test.acceptance.framework.center.MeetingParameters;
import org.mifos.test.acceptance.framework.group.EditCustomerStatusParameters;
import org.mifos.test.acceptance.framework.loan.CreateLoanAccountsSearchPage;
import org.mifos.test.acceptance.framework.office.OfficeEditInformationPage;
import org.mifos.test.acceptance.framework.testhelpers.CenterTestHelper;
import org.mifos.test.acceptance.framework.testhelpers.NavigationHelper;
import org.mifos.test.acceptance.framework.testhelpers.OfficeHelper;
import org.mifos.test.acceptance.framework.testhelpers.UserHelper;
import org.mifos.test.acceptance.framework.user.EditUserDataPage;
import org.mifos.test.acceptance.remote.InitializeApplicationRemoteTestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("PMD")
@ContextConfiguration(locations={"classpath:ui-test-context.xml"})
@Test(sequential=true, groups={"loan","acceptance","ui"})
public class CreateMultipleLoanAccountTest extends UiTestCaseBase {
    @Autowired
    private DriverManagerDataSource dataSource;
    @Autowired
    private DbUnitUtilities dbUnitUtilities;
    @Autowired
    private InitializeApplicationRemoteTestingService initRemote;

    private OfficeHelper officeHelper;
    private UserHelper userHelper;
    private CenterTestHelper centerTestHelper;
    private NavigationHelper navigationHelper;

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        dbUnitUtilities.loadDataFromFile("acceptance_small_004_dbunit.xml", dataSource);
        officeHelper = new OfficeHelper(selenium);
        userHelper = new UserHelper(selenium);
        centerTestHelper = new CenterTestHelper(selenium);
        navigationHelper = new NavigationHelper(selenium);
    }

    @AfterMethod
    public void logOut() {
        (new MifosPage(selenium)).logout();
    }

    /**
     * Verify that only "Active" branches, centers and loan officers are available
     * on the Search field and "Loan Instances" field populates with the names
     * of loan instances matching the frequency of the center selected.
     * http://mifosforge.jira.com/browse/MIFOSTEST-59
     * @throws Exception
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void verifyInactiveBranchesOfficersCentersDoesntAppearOnSearch() throws Exception {
        initRemote.dataLoadAndCacheRefresh(dbUnitUtilities, "acceptance_small_001_dbunit.xml", dataSource, selenium);
        EditCustomerStatusParameters customerStatusParams = new EditCustomerStatusParameters();
        customerStatusParams.setCenterStatus(CenterStatus.INACTIVE);
        customerStatusParams.setNote("note");
        MeetingParameters meetingParams = new MeetingParameters();
        meetingParams.setMeetingPlace("Burkat");
        meetingParams.setWeekFrequency("1");
        meetingParams.setWeekDay(MeetingParameters.THURSDAY);
        CreateCenterEnterDataPage.SubmitFormParameters centerParams = new CreateCenterEnterDataPage.SubmitFormParameters();
        centerParams.setCenterName("newCenterro");
        centerParams.setLoanOfficer("Bagonza Wilson");
        centerParams.setMeeting(meetingParams);

        centerTestHelper.createCenter(centerParams, "Office1");
        CreateLoanAccountsSearchPage multipleAccPage = navigateToCreateMultipleLoanAccountsSearchPage();

        multipleAccPage.selectBranchOfficerAndCenter("Office1", "Bagonza Wilson", "Center1");
        multipleAccPage.selectBranchOfficerAndCenter("Office1", "Bagonza Wilson", "newCenterro");
        multipleAccPage.selectBranchOfficerAndCenter("Office2", "John Okoth", "Center2");
        multipleAccPage.selectBranchOfficerAndCenter("Office3", "Jenna Barth", "Center3");

        centerTestHelper.changeCenterStatus("newCenterro", customerStatusParams);
        officeHelper.changeOfficeStatus("Office2", OfficeEditInformationPage.STATUS_INACTIVE);
        userHelper.changeUserStatus("Jenna Barth", EditUserDataPage.STATUS_INACTIVE);
        multipleAccPage = navigateToCreateMultipleLoanAccountsSearchPage();

        multipleAccPage.selectBranchOfficerAndCenter("Office1", "Bagonza Wilson", "Center1");
        multipleAccPage.verifyCenterIsNotInSelectOptions("Office1", "Bagonza Wilson", "newCenterro");
        multipleAccPage.verifyBranchNotInSelectOptions("Office2");
        multipleAccPage.verifyOfficerNotInSelectOptions("Office3", "Jenna Barth");
    }

    private CreateLoanAccountsSearchPage navigateToCreateMultipleLoanAccountsSearchPage() {
        return navigationHelper
                .navigateToClientsAndAccountsPage()
                .navigateToCreateMultipleLoanAccountsUsingLeftMenu();
    }
}
