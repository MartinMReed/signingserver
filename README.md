Usage Notes
=====
This is a simple JAVA application for tracking the RIM signing server status. 

To run the application you can execute using `java -jar signingserver.jar config.xml`

Build
-----------------
To build this you need to use Maven.

The NARST (Not A Real SignatureTool, but close enough) library is a required dependency, however it is not publicly available.
The NARST library is responsible for the actual signing attempts made to the RIM server.

A config file is required to run this application. A sample is provided below and uses values from the CSK and DB files used with the SignatureTool.jar.

	<?xml version="1.0" encoding="UTF-8"?>
	<config xmlns="http://hardisonbrewing.org/schemas/model" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://hardisonbrewing.org/schemas/model config.xsd">
		<!-- URL of the tracker website that will accept the signing results -->
		<tracker>${tracker}</tracker>
		<!-- Salt from CSK file -->
		<salt>${salt}</salt>
		<!-- PrivateKey from CSK file -->
		<privateKey>${privateKey}</privateKey>
		<authorities>
			<!-- Signing authorities from DB file -->
			<authority>
				<signerId>RRT</signerId>
				<url>http://www.rim.net/Websigner/servlet/Runtime</url>
				<clientId>123456789</clientId>
				<password>password</password>
			</authority>
			<authority>
				<signerId>RBB</signerId>
				<url>http://www.rim.net/Websigner/servlet/BBApps</url>
				<clientId>123456789</clientId>
				<password>password</password>
			</authority>
			<authority>
				<signerId>RCR</signerId>
				<url>http://www.rim.net/Websigner/servlet/CryptoRIM</url>
				<clientId>123456789</clientId>
				<password>password</password>
			</authority>
		</authorities>
		<cods>
			<cod>sigtool-0.cod</cod>
			<cod>sigtool-1.cod</cod>
			<cod>sigtool-2.cod</cod>
			<cod>sigtool-3.cod</cod>
			<cod>sigtool-4.cod</cod>
			<cod>sigtool-5.cod</cod>
			<cod>sigtool-6.cod</cod>
			<cod>sigtool-7.cod</cod>
			<cod>sigtool-8.cod</cod>
			<cod>sigtool-9.cod</cod>
		</cods>
	</config>
