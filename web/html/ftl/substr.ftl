<#-- 自定义的图片指令。用于跨域加载图片
属性说明：
   str    需要处理字符串
   len	  截取长度
 使用方式：
    <#import "../ftl/substr.ftl" as s>
    <@s.substr str="xxx" len=xx endWith="xx"/>
 -->
 
 <#macro substr str len endWith>
 	<#if str??>
		<#assign slen = str?length>
	</#if>
	<#if len < slen>
		<#assign subStr = str?substring(0, len) + endWith>	
	<#else>
		<#assign subStr = str>
	</#if>
	${subStr}</#macro>