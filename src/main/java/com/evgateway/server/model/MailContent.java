package com.evgateway.server.model;

import java.math.BigDecimal;
import java.util.Date;

import com.evgateway.server.pojo.ConfigurationSettings;
import com.evgateway.server.pojo.User;

public class MailContent {

	public String getPMChangedMsg(String orgName) {

		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return " es su Contraseña de un solo uso para verificar y  actualizar el correo electrónico o el cambio de número de teléfono móvil.\n \n Gracias";
		} else {
			return " is your OTP for the Verification for Updating  Mail or MobileNumber Change .\n \n Thank You";
		}

	}
	
	public String getResetMsg(String message, String url, String orgName, String password, User user, String email, String phone) {
		 
		return "Dear " + user.getFirstName() + " " + user.getLastName() + "," + " \r\n\r\n "
				+ " We received a request to reset the password for your account at " + orgName + "."
				+ " To ensure the security of your account,"
				+ "we are providing you with a temporary password. Please follow the "
				+ "instructions below to reset your password:" + " \r\n\r\n Temporary Password: " + password + ""
				+ "\r\n\r\n Please use the temporary password to log in to your account. Once logged in,"
				+ " you will be prompted to create a new, personalized password. \r\n\r\n Instructions:"
				+ "\r\n\r\n 1.Visit our login page at " + url + ".\r\n" + "2.Enter your email address ("
				+ user.getEmail() + ") and the temporary password provided above.\r\n"
				+ "3.Follow the on-screen instructions to create a new password for your account.\r\n"
				+ "4.Ensure your new password meets our security requirements."
				+ "\r\n Thankyou " + "\r\n\r\n" + "Contact us:" + "\r\n The  " + orgName
				+ " support team is available 24/7 to help you with your questions and \r\n"
				+ "make sure you have the best experience. To get in touch, please visit email:"+email
				+ "	 or call: " + phone + ".\r\n\r\n\r\n Best,\r\n The  " + orgName + " Team.";

 
	}
	public String getPromoMsg(BigDecimal promoAmount, String orgName, String currencyType) {
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "\r\n\r\n "+currencyType+" " + promoAmount + " monto de promoción ha sido agregado a su cuenta con éxito  \r\n\r\n";
		} else {
			return "\r\n\r\n "+currencyType+" " + promoAmount + " Added to your account successfully  \r\n\r\n";
		}
	}

	public String getPMChangedSub(String orgName) {
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "Everon - Contraseña de un solo uso para cambiar el correo o el número de teléfono móvil";
		} else {
			return "" + orgName + " - One Time Password for Changing Mail Or MobileNumber";
		}
	}

	public String getResetPassword(String url) {

		return "Reset password link \r\n\r\n" + url
				+ "\r\n\r\n Click or copy and paste above link in browser to reset your password.";
	}

	public String getRegisterMsg(String fullName, String orgName, String final_link, String result, String email,
			Long orgId, String phone) {

		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "Hola " + fullName + "," + " \r\n\r\n "
					+ "Gracias por registrarse con nosotros y convertirse parte de nuestra familia."
					+ " Estamos emocionados de poder servirle y esperamos que" + orgName
					+ " le permita tener un proceso de carga de EV mucho más suave y sencillo.  \r\n"
					+ "\r\n\r\n Haga clic en el enlace: " + final_link + " " + "o use la contraseña de un solo uso: "
					+ result + "  Para activar tu cuenta." + "\r\n\r\n Una vez que haya iniciado sesión, puede:"
					+ "\r\n    *  Editar su información de contacto" + "\r\n    *  Solicitar una  " + orgName
					+ " tarjeta RFID" + "\r\n    *  Ver su actividad de carga"
					+ "\r\n    *  Ver el historial de su cuenta"
					+ "\r\n    *  Visite la pestaña Preguntas frecuentes para saber más sobre el portal web."
					+ "\r\n\r\n Asegúrese de agregar dinero a su cuenta antes de usar las estaciones de pago."
					+ "\r\n No olvide descargar la aplicación móvil  " + orgName + "\r\n\r\n" + "Contáctenos: " + "\r\n"
					+ "El equipo de soporte de  " + orgName
					+ " está disponible las 24 horas del día, los 7 días de la semana para ayudarlo con sus preguntas y "
					+ "asegúrese de tener la mejor experiencia. Para ponerse en contacto, envíenos un mensaje: cfssoporte-evgateway@cfscr.com"
					+ "\r\n El equipo de " + orgName + ".";
		} else if (orgId == 135) {
			return "Hello " + fullName + "," + " \r\n\r\n "
					+ "Thank you for registering with us and becoming the newest addition to our family."
					+ " We are excited to serve you.  \r\n" + "\r\n\r\n Please Click on link: " + final_link + " "
					+ "or use the One Time Password: " + result + " to activate your account."
					+ "\r\n\r\n Once logged in, you can:" + "\r\n    *  Edit your contact information"
					+ "\r\n    *  Order a  " + orgName + " RFID card" + "\r\n    *  View your Charging Activity"
					+ "\r\n    *  View your Account History"
					+ "\r\n    *  Visit the FAQs tab to know more about the Web portal."
					+ "\r\n\r\n Make sure to add funds to your account before using paid stations."
					+ "\r\n Don't forget to download the Francis EV Charging mobile app." + "\r\n\r\n" + "Contact us:"
					+ "\r\n" + "The " + orgName + " support team is available 24/7 to help you with your questions and "
					+ "make sure you have the best experience." + " To get in touch, please visit:" + " email: " + email
					+ "; or call: " + phone + "." + "\r\n\r\n\r\n Best," + "\r\n The " + orgName + " Team";
		} else {
			return "Hello " + fullName + "," + " \r\n\r\n "
					+ "Thank you for registering with us and becoming the newest addition to our family."
					+ " We are excited to serve you and" + " we hope that " + orgName
					+ " allows you to have a much smoother and easier EV charging process.  \r\n"
					+ "\r\n\r\n Please Click on link: " + final_link + " " + "or use the One Time Password: " + result
					+ " to activate your account." + "\r\n\r\n Once logged in, you can:"
					+ "\r\n    *  Edit your contact information" + "\r\n    *  Order a  " + orgName + " RFID card"
					+ "\r\n    *  View your Charging Activity" + "\r\n    *  View your Account History"
					+ "\r\n    *  Visit the FAQs tab to know more about the Web portal."
					+ "\r\n\r\n Make sure to add funds to your account before using paid stations."
					+ "\r\n Don't forget to download the  " + orgName + " mobile app" + "\r\n\r\n" + "Contact us:"
					+ "\r\n" + "The " + orgName + " support team is available 24/7 to help you with your questions and "
					+ "make sure you have the best experience." + " To get in touch, please visit:" + " email: " + email
					+ "; or call: " + phone + "." + "\r\n\r\n\r\n Best," + "\r\n The " + orgName + " Team";
		}
	}

	public String getRegisterMsgForPortal(String fullName, String orgName, String final_link, String email,
			String phone) {

		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "Hola " + fullName + "," + " \r\n\r\n "
					+ "Gracias por registrarse con nosotros y convertirse parte de nuestra familia."
					+ " Estamos emocionados de poder servirle y esperamos que" + orgName
					+ " le permita tener un proceso de carga de EV mucho más suave y sencillo.  \r\n"
					+ "\r\n\r\n Haga clic en el enlace:  " + final_link + " Para activar tu cuenta."
					+ "\r\n\r\n Una vez que haya iniciado sesión, puede:"
					+ "\r\n    *  Editar su información de contacto" + "\r\n    *  Solicitar una  " + orgName
					+ " tarjeta RFID" + "\r\n    *  Ver su actividad de carga"
					+ "\r\n    *  Ver el historial de su cuenta"
					+ "\r\n    *  Visite la pestaña Preguntas frecuentes para saber más sobre el portal web."
					+ "\r\n\r\n Asegúrese de agregar dinero a su cuenta antes de usar las estaciones de pago."
					+ "\r\n No olvide descargar la aplicación móvil  " + orgName + "\r\n\r\n" + "Contáctenos: " + "\r\n"
					+ " El equipo de soporte de  " + orgName
					+ " está disponible las 24 horas del día, los 7 días de la semana para ayudarlo con sus preguntas y "
					+ "asegúrese de tener la mejor experiencia. Para ponerse en contacto, envíenos un mensaje: cfssoporte-evgateway@cfscr.com"
					+ "\r\n El equipo de " + orgName + ".";
		} else {
			return "Hello " + fullName + "," + " \r\n\r\n "
					+ "Thank you for registering with us and becoming the newest addition to our family."
					+ " We are excited to serve you and" + " we hope that " + orgName
					+ " allows you to have a much smoother and easier EV charging process.  \r\n"
					+ "\r\n\r\n Please Click on link: " + final_link + " to activate your account."
					+ "\r\n\r\n Once logged in, you can:" + "\r\n    *  Edit your contact information"
					+ "\r\n    *  Order a " + orgName + " RFID card" + "\r\n    *  View your Charging Activity"
					+ "\r\n    *  View your Account History"
					+ "\r\n    *  Visit the FAQs tab to know more about the Web portal."
					+ "\r\n\r\n Make sure to add funds to your account before using paid stations."
					+ "\r\n Don't forget to download the " + orgName + " mobile app" + "\r\n\r\n" + "Contact us:"
					+ "\r\n" + " The  " + orgName
					+ " support team is available 24/7 to help you with your questions and "
					+ "make sure you have the best experience." + " To get in touch, please visit:" + " email: " + email
					+ "; or call: " + phone + "." + "\r\n\r\n\r\n Best," + "\r\n The  " + orgName + " Team.";
		}
	}

	public String getResetMsg(String message, String url, String orgName) {
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "" + message + " \r\n\r\n" + url
					+ "\r\n\r\n  Haga clic o copie y pegue el enlace anterior en el navegador para restablecer su contraseña.";
		} else {
			return "" + message + " \r\n\r\n" + url
					+ "\r\n\r\n Click or copy and paste above link in browser to reset your password.";
		}
	}

	public String getPromoMsg(BigDecimal promoAmount, String orgName) {
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "\r\n\r\n ₡" + promoAmount + " monto de promoción ha sido agregado a su cuenta con éxito  \r\n\r\n";
		} else {
			return "\r\n\r\n $" + promoAmount + " Added to your account successfully  \r\n\r\n";
		}
	}

	public String getGridKActivateMsgrfid(String rfid, String orgName) {
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "\r\n\r\n" + " (" + rfid + ") Se ha desactivado una tarjeta RFID del usuario. \n Fecha : "
					+ new Date() + " \r\n\r\n";
		} else {
			return "\r\n\r\n" + "A Gridkey (" + rfid + ") has been deactivated from user.\n Dated : " + new Date()
					+ " \r\n\r\n";
		}
	}

	public String getPaymentMsg(double amount, String transactionId, String orgName, String currencyType) {
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "\r\n\r\n ¡Gracias por agregar fondos a su " + orgName
					+ "billetera! Su pago se ha realizado correctamente. A continuación se muestran los detalles de la transacción."
					+ " Monto: ₡ " + amount + "  ID de transacción: " + transactionId + "\r\n\r\n";
		} else {
			return "\r\n\r\n Thank You for adding funds to your " + orgName
					+ " Wallet! Your payment is successful. Below are the transaction details." + " Amount: "+currencyType+" " + amount
					+ "  Transaction ID: " + transactionId + "\r\n\r\n";
		}
	}

	public String getGridKActivateReqrfid(long amount, String orgName, String currencyType) {
		// TODO Auto-generated method stub
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {

			return "¡Gracias por solicitar la tarjeta Gridkey! " + currencyType + " " + amount
					+ " deducido de su cuenta.";
		} else {

			return "Thank you for requesting Gridkey card! " + currencyType + " " + amount
					+ " deducted from your account.";
		}
	}

	public String getAutoReloadMail(double amount, String message, String orgName) {
		// TODO Auto-generated method stub
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "₡" + amount + " + Acreditado por Worldpay (AutoReload) a tu cuenta.";
		} else {
			return "$" + amount + " Credited By Worldpay(AutoReload) to your account.";
		}
	}

	public String getAutoReloadDeclinedMail(String message, String orgName) {
		// TODO Auto-generated method stub
		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "La recarga automática en su cuenta ha fallado. Su tarjeta de crédito registrada ha sido rechazada.\r\n\r\n "
					+ " El rechazo podría deberse al vencimiento de la tarjeta, fondos insuficientes, cambio de número de tarjeta, etc."
					+ " Para evitar la interrupción del servicio de carga de vehículos eléctricos,"
					+ " verifique su información de facturación y actualice los datos de su tarjeta de crédito. \r\n\r\n"
					+ " Inicie sesión en su cuenta a través de la aplicación móvil y actualice la información de su tarjeta de crédito. \r\n\r\n";
		} else {
			return "Auto reload on your account failed. Your credit card on file has declined .\r\n\r\n "
					+ " The decline could be due to card expiration, insufficient funds, card number change, etc."
					+ " In order to prevent discontinuation of EV charging service,"
					+ " please verify your billing information and update your credit card details. \r\n\r\n"
					+ " Login to your account via the mobile app and update your credit card information. \r\n\r\n";
		}
	}

	
	public String getSetPasswordMailStr(String fullName, String orgName, String final_link, String email,
			String phone) {

		if (orgName.equalsIgnoreCase("Everon") || orgName == "Everon") {
			return "Hola " + fullName + "," + " \r\n\r\n " + "\r\n\r\n Haga clic en el enlace:  " + final_link
					+ " Para activar tu cuenta." + "Contáctenos: " + "\r\n" + " El equipo de soporte de  " + orgName
					+ " está disponible las 24 horas del día, los 7 días de la semana para ayudarlo con sus preguntas y "
					+ "asegúrese de tener la mejor experiencia. Para ponerse en contacto, envíenos un mensaje: cfssoporte-evgateway@cfscr.com"
					+ "\r\n El equipo de " + orgName + ".";
		} else {
			return "Hello " + fullName + "," + " \r\n\r\n " + "\r\n\r\n Please Click on link: " + final_link
					+ " to set password for  your account." + "\r\n Thankyou " + "\r\n\r\n" + "Contact us:" + "\r\n"
					+ " The  " + orgName + " support team is available 24/7 to help you with your questions and "
					+ "make sure you have the best experience." + " To get in touch, please visit:" + " email: " + email
					+ "; or call: " + phone + "." + "\r\n\r\n\r\n Best," + "\r\n The  " + orgName + " Team.";
		}

	}

	public String getFailedTranForAutoReloadUser(String responseStatus, String transactionId, String amount,
			String paymentAccountId) {
		// TODO Auto-generated method stub
		return "Dear Customer,\r\nYour Auto Reload payment is failed due to reason : " + responseStatus + "."
				+ "\r\n Currently we are disabling your auto reload payment.";

	}

	public String getFailedTranForAutoReloadSupport(String responseStatus, String transactionId, String amount,
			String paymentAccountId, Long accountId) {
		// TODO Auto-generated method stub
		return "For Auto Reload Transaction payment failed due to : " + responseStatus + " "
				+ "\r\n Transaction Details:" + "\r\n Transaction Id      : " + transactionId
				+ "\r\n Amount              : " + amount + "\r\n AccountId              : " + accountId
				+ "\r\n Payment Account Id  : " + paymentAccountId + "";
	}

	public String payAsYouGoSettlementAmtSupport(String settlementTranStatus, String transactionId, String finalAmount,
			String phoneNumber) {
		// TODO Auto-generated method stub
		return "For PayAsYouGo World Pay Transaction, Amount Settlement failed due to : " + settlementTranStatus + " "
				+ "\r\n Transaction Details:" + "\r\n Transaction Id  : " + transactionId + "\r\n Amount          : "
				+ finalAmount + "\r\n Customer Id     : " + phoneNumber + "";
	}

	public String payAsYouGoSettlementAmtUser(String settlementTranStatus, String transactionId, String finalAmount,
			String phoneNumber) {
		// TODO Auto-generated method stub
		return "Dear Customer,\r\nWe haven't received Charge Amount:($" + finalAmount
				+ ") due to Payment Failed, reason : " + settlementTranStatus + ".";
	}

	public String payAsYouGoReversalAmtSupport(String reversalStatus, String transactionId, String finalAmount,
			String paymentAccountId) {
		// TODO Auto-generated method stub
		return "For PayAsYouGo World Pay Transaction, Reversal Amount failed due to : " + reversalStatus + " "
				+ "\r\n Transaction Details:" + "\r\n Transaction Id        : " + transactionId
				+ "\r\n Amount                : " + finalAmount + "\r\n Payment AccountId     : " + paymentAccountId
				+ "";
	}

	public String payAsYouGoReversalAmtUser(String reversalStatus, String transactionId, String finalAmount,
			String phoneNumber) {
		// TODO Auto-generated method stub
		if (reversalStatus.equalsIgnoreCase("Declined")) {
			return "Dear Customer,\r\n Guest User Transaction Failed due to Card " + reversalStatus + ".";
		} else {
			return "Dear Customer,\r\n Guest User Transaction Failed due to reason : " + reversalStatus + ".";
		}
	}

	public String autoReloadFailedForReversalSupport(String responseStatus, String transactionId, String amount,
			String paymentAccountId) {
		// TODO Auto-generated method stub
		return "For Auto Reload Transaction payment for Reversal amount failed due to : " + responseStatus + " "
				+ "\r\n Transaction Details:" + "\r\n Transaction Id      : " + transactionId
				+ "\r\n Amount              : " + amount + "\r\n Payment Account Id  : " + paymentAccountId + "";
	}

	public String autoReloadFailedForReversalUser(String responseStatus, String transactionId, String amount,
			String paymentAccountId) {
		// TODO Auto-generated method stub
		return "Dear Customer,\r\nYour Auto Reload payment for Reversal amount failed due to reason : " + responseStatus
				+ "." + "\r\n Currently we are disabling your auto reload payment.";
	}

	public String getAccountsEmail(String name, String orgName, String delorgName, String sitename, Date prodate,
			String type) {

		return "Provisioned " + type + " Information : " + name + "\r\n " + " \r\n  Organisation        :  " + orgName
				+ " \r\n" + " \r\n  Dealer Organisation :  " + delorgName + "\r\n" + " \r\n  Site Name           :  "
				+ sitename + "\r\n" + " \r\n  Provision Date      :  " + prodate + " \r\n" + " \r\n Regards, \r\n"
				+ "\r\n Evgateway India pvt limited";

	}

	public String getMessageForDeleteUser(String message, String orgName) {
		// TODO Auto-generated method stub
		return "Dear Customer,\r\n\r\nWe have received your request to Delete/Deactivate your account with " + orgName
				+ ". "
				+ "Our support team will reach out to you shortly to complete the formalities and process your wallet refund.\n \nThank You for your support";
	}

	public String getSiemensMailStr() {
		return "eMobility |Siemens EV Charger Dashboard Onboarding Confirmation \r\n "
				+ "   \r\n Dear Valued Customer, \r\n"
				+ " \r\n This is to notify you that you have been successfully onboarded to the Siemens EV Charger Dashboard. Please see link for the video tutorial to get you acclimated to the dashboard. If you would also like to schedule a quick training session of the capabilities of the dashboard please reply to support@evgateway.com with some times you are available.\r\n"
				+ " \r\n Additional information and steps will be provided to setup billing and QR codes if required. \r\n"
				+ " \r\n Link to video tutorial : https://eur01.safelinks.protection.outlook.com/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DaH7FQVW8iWE&data=04%7C01%7Csunil.sinha%40siemens.com%7C2db4b52ff9c64647c60e08da12575ff0%7C38ae3bcd95794fd4addab42e1495d55a%7C1%7C0%7C637842463790496702%7CUnknown%7CTWFpbGZsb3d8eyJWIjoiMC4wLjAwMDAiLCJQIjoiV2luMzIiLCJBTiI6Ik1haWwiLCJXVCI6Mn0%3D%7C3000&sdata=iNAd8G02Sgs3ORtGIFejAhUKWQVKLwQyyGV8BimJgFI%3D&reserved=0 \r\n"
				+ " \r\n Link to  tutorial : https://evgintranet-my.sharepoint.com/:f:/g/personal/nishanth_evgateway_com/EuLEBUPIF2ZLp-Pdewf2D80BIzvLoT6wwLD2wkgBYZvIvw?e=lgQLTn \r\n"
				+ " \r\n Regards, \r\n" + "\r\n Siemens E-Mobility";

	}

	public String getCaptureMail(ConfigurationSettings config, Double amount, String currencyType) {
		// TODO Auto-generated method stub
		String symbol = "$";
		if(currencyType.equalsIgnoreCase("CAD")) {
			symbol = "CA$";
		}
		String message = "Dear Customer,\r\n\r\n"
				+ "We hope this email finds you well. We are writing to inform you that the pre-authorization hold on your "
				+ "account in the amount of "+symbol+""+amount+" has been settled. This charge is related to EvGateway EV charging Guest user service.\r\n\r\n"
				+ "Please note that any remaining hold amount on your account will be released shortly. Depending on your financial institution, "
				+ "it may take a few business days for the hold to be fully released.\r\n\r\n"
				+ "If you have any questions or concerns regarding this pre-authorization hold, please do not hesitate to contact us at "+config.getSupportEmail()+". "
				+ "We value your business and are committed to providing you with the best possible service.\r\n\r\n"
				+ "Thank you for your attention to this matter.\r\n\r\n"
				+ "Best,\r\n"
				+ "The "+config.getOrgName()+" Team";
		return message;
	}
	
	public String getNegBlncMail(ConfigurationSettings config, Double amount, String currencySymbol) {
		// TODO Auto-generated method stub
		String message = "Dear Customer,\r\n\r\n"
				+ "We hope this email finds you well. We are writing to inform you that the pre-authorization hold on your "
				+ "account in the amount of "+currencySymbol+""+amount+" has been settled. This charge is related to EvGateway EV charging Guest user service.\r\n\r\n"
				+ "Please note that any remaining hold amount on your account will be released shortly. Depending on your financial institution, "
				+ "it may take a few business days for the hold to be fully released.\r\n\r\n"
				+ "If you have any questions or concerns regarding this pre-authorization hold, please do not hesitate to contact us at "+config.getSupportEmail()+". "
				+ "We value your business and are committed to providing you with the best possible service.\r\n\r\n"
				+ "Thank you for your attention to this matter.\r\n\r\n"
				+ "Best,\r\n"
				+ "The "+config.getOrgName()+" Team";
		return message;
	}

	public String getPreauthorizeMail(ConfigurationSettings config, double authAmount, String currencyCode) {
		// TODO Auto-generated method stub
		String symbol = "$";
		if(currencyCode.equalsIgnoreCase("CAD")) {
			symbol = "CA$";
		}
		String message = "Dear Customer,\r\n\r\n"
				+ "We hope this email finds you well. We are writing to inform you that a pre-authorization hold has been placed on your card "
				+ "in the amount of "+symbol+""+authAmount+". This pre-authorization is related to EvGateway EV charging Guest user service.\r\n\r\n"
				+ "Please note that this is a temporary hold on your account and the funds will not be deducted from your account until the Charging has been completed. "
				+ "Once the charging session has been completed, the final charge will be processed and the pre-authorization hold will be released.\r\n\r\n"
				+ "If you have any questions or concerns regarding this pre-authorization hold, please do not hesitate to contact us at "+config.getSupportEmail()+". "
				+ "We value your business and are committed to providing you with the best possible service.\r\n\r\n"
				+ "Thank you for your attention to this matter.\r\n\r\n"
				+ "Best,\r\n"
				+ "The "+config.getOrgName()+" Team";
		
		return message;
	}
}
