<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
xmlns:wrs="http://java.sun.com/xml/ns/jdbc"
>

<xsl:output method="text" encoding="utf-8" />
<xsl:strip-space elements="*"/>

<xsl:param name="table" select="//table-name/text()" />

<xsl:template match="/" >
table=<xsl:value-of select="$table"/>
<xsl:apply-templates select="//wrs:data" />
</xsl:template>


<xsl:template match="wrs:data" >
<xsl:apply-templates select=".//wrs:currentRow" />
</xsl:template>


<xsl:template match="wrs:currentRow" >
	<xsl:text>insert into </xsl:text><xsl:value-of select="$table"/><xsl:text> (</xsl:text>
  <xsl:for-each select='//wrs:column-name'>
		<xsl:if test="position()>1" ><xsl:text>,</xsl:text>
		</xsl:if>
    <xsl:value-of select='.'/>
  </xsl:for-each>
  <xsl:text>) values (</xsl:text>
  <xsl:for-each select='wrs:columnValue'>
		<xsl:if test="position()>1" ><xsl:text>,</xsl:text>
		</xsl:if>
    <xsl:text>'</xsl:text><xsl:value-of select='.'/><xsl:text>'</xsl:text>
  </xsl:for-each>
  <xsl:text>);</xsl:text>
</xsl:template>



</xsl:stylesheet>

