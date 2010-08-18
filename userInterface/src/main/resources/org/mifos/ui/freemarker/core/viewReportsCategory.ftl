[#ftl]
[#--
* Copyright (c) 2005-2010 Grameen Foundation USA
*  All rights reserved.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
*  implied. See the License for the specific language governing
*  permissions and limitations under the License.
*
*  See also http://www.apache.org/licenses/LICENSE-2.0.html for an
*  explanation of the license and how it is applied.
--]
[#include "layout.ftl"]
[@adminLeftPaneLayout]
<!--  Main Content Begins-->  
  <div class=" content">
  	<form method="" action="" name="formname">
  	<p class="bluedivs paddingLeft"><a href="admin.ftl">[@spring.message "admin"/]</a>&nbsp;/&nbsp;<span class="fontBold">[@spring.message "viewreportscategory"/]</span></p>
    <p class="font15 orangeheading">[@spring.message "viewreportscategory"/]</p>
    <p><span>[@spring.message "clickonEdit/Deletetomakechangestoareportcategoryor"/] </span><a href="#">[@spring.message "addanewreportcategory"/] </a></p>
    <div>&nbsp;</div>
    <div class="span-18">
    	<div class="span-22  borderbtm paddingLeft">
        	<span class="span-17 fontBold ">[@spring.message "categoryName"/]</span>
            <span class="span-3 ">&nbsp;</span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold ">[@spring.message "clientDetail"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold">[@spring.message "performance"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold">[@spring.message "center"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold">[@spring.message "loanProductDetail"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold">[@spring.message "status"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold">[@spring.message "analysis"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        <div class="span-22  borderbtm paddingLeft ">
            <span class="span-17 fontBold">[@spring.message "miscellaneous"/]</span>
            <span class="span-2 rightAlign"><a href="#">[@spring.message "edit"/]</a>&nbsp;|&nbsp;<a href="#">[@spring.message "delete"/]</a></span>
        </div>
        
    </div>
   	</form> 
  </div><!--Main Content Ends-->
[/@adminLeftPaneLayout]