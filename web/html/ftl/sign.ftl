 <#-- 单位处理 -->
<#macro unitSign codeValue lan><#assign codeName = "">
<@portal_dictionary codeValue="${codeValue}">
<#if codeInfos??>
	<#list codeInfos as code>
	<#if code_index == 0>
		<#assign codeName=code.codeName>
	</#if>
	</#list>
</#if></@portal_dictionary>
<#if lan?? && lan == "zn"><#if codeName == "美元基金" || codeName == "美元" >美元<#elseif codeName == "人民币基金" || codeName == "人民币">人民币<#else>${codeName}</#if><#else><#if codeName == "美元基金" || codeName == "美元" >＄<#else>￥</#if></#if>
</#macro>