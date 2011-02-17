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

package org.mifos.test.acceptance.admin;

import org.mifos.framework.util.DbUnitUtilities;
import org.mifos.test.acceptance.framework.MifosPage;
import org.mifos.test.acceptance.framework.UiTestCaseBase;
import org.mifos.test.acceptance.framework.admin.AdminPage;
import org.mifos.test.acceptance.framework.admin.DefineHiddenMandatoryFieldsPage;
import org.mifos.test.acceptance.framework.testhelpers.NavigationHelper;
import org.mifos.test.acceptance.remote.InitializeApplicationRemoteTestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:ui-test-context.xml"})
@Test(sequential=true, groups={"acceptance","ui","admin"})
public class DefineHiddenMandatoryFieldsTest  extends UiTestCaseBase{
    private NavigationHelper navigationHelper;

    @Autowired
    private DriverManagerDataSource dataSource;
    @Autowired
    private DbUnitUtilities dbUnitUtilities;
    @Autowired
    private InitializeApplicationRemoteTestingService initRemote;

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        navigationHelper = new NavigationHelper(selenium);
        initRemote.dataLoadAndCacheRefresh(dbUnitUtilities, "acceptance_small_008_dbunit.xml", dataSource, selenium);
    }

    @AfterMethod
    public void logOut() {
        (new MifosPage(selenium)).logout();
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    //http://mifosforge.jira.com/browse/MIFOSTEST-219
    public void verifyChangesMadeOnDefineMandatoryHiddenFileds(){
        //When
        AdminPage adminPage = navigationHelper.navigateToAdminPage().navigateToViewRolesPage().navigateToManageRolePage("Admin").disablePermission("9_1").
        verifyPermissionText("9_1", "Can define hidden/mandatory fields").submitAndGotoViewRolesPage().navigateToAdminPage();

        DefineHiddenMandatoryFieldsPage defineHiddenMandatoryFieldsPage = adminPage.navigateToDefineHiddenMandatoryFields();
        //Then
        defineHiddenMandatoryFieldsPage.verifyAccessDenied();
        //When
        adminPage = navigationHelper.navigateToAdminPage().navigateToViewRolesPage().navigateToManageRolePage("Admin").enablePermission("9_1").
        verifyPermissionText("9_1", "Can define hidden/mandatory fields").submitAndGotoViewRolesPage().navigateToAdminPage();
        defineHiddenMandatoryFieldsPage = adminPage.navigateToDefineHiddenMandatoryFields();
        defineHiddenMandatoryFieldsPage.checkHideRelativeSecondLastName();
        defineHiddenMandatoryFieldsPage.checkMandatoryEthnicity();
        adminPage = defineHiddenMandatoryFieldsPage.submit();
        adminPage.navigateToClientsAndAccountsPageUsingHeaderTab().navigateToCreateNewClientPage()
            .navigateToCreateClientWithoutGroupPage().chooseOffice("MyOffice1232993831593");
        //Then
        Assert.assertTrue(selenium.isTextPresent("*Ethnicity:"));
        Assert.assertFalse(selenium.isElementPresent("create_ClientPersonalInfo.input.spouseSecondLastName"));

        // restore original configuration
        defineHiddenMandatoryFieldsPage = navigationHelper.navigateToAdminPage().navigateToDefineHiddenMandatoryFields();
        defineHiddenMandatoryFieldsPage.uncheckHideRelativeSecondLastName();
        defineHiddenMandatoryFieldsPage.uncheckMandatoryEthnicity();
        defineHiddenMandatoryFieldsPage.submit();
    }
}
