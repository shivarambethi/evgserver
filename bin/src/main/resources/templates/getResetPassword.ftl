<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
      <title>Sending Email with Freemarker HTML Template Example</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
      <!-- use the font -->
      <style>
         body {
         font-family: 'Roboto', sans-serif;
         font-size: 48px;
         }
         .card-div {
    width: 600px;
    height:fit-content;
    position: absolute;
    top: 0;
    bottom: 50px;
    left: 0;
    right: 0;
    margin: auto;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
    margin-top: 50px;    
    margin-bottom: 50px;
}
         table,tr,td,th {
         text-align: center;
         font-family: 'Times New Roman', Times, serif;
         font-size: medium;
         border-collapse: collapse;
         adding: 6px;
         width:30% ;
         background-color:#ffffff;
         table-layout: auto;        
         padding: 10px 5px 10px 5px;
         }
      </style>
   </head>
   <body style="margin: 0; padding: 0;">
   
      <table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse; width: 600px;">
         <tr>
            <td align="center"  style="padding: 40px 200px 30px 200px; width: 300px;" >
               <img src="https://evgimages.blob.core.windows.net/evg3imagestesting/IMG_image%201.jpg_8a269a0b-5625-4988-ad24-38b525129d46.jpg" style="display: block; width: 300px;" alt="BcHydro"/>
            </td>
         </tr>
         <td style="padding: 0in 10pt;" xml="lang" class="">
              <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 10pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Dear </span><span style="font-size: 15pt; font-family: Arial, sans-serif;" class="">${fullName}<span style="color: black;" class="">, </span></span></p> <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> We received a request to reset the password for your account at ${orgName}.
			 To ensure the security of your account,
				we are providing you with a temporary password. Please follow the
			instructions below to reset your password: </span></p> <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;">
			    <br>
           <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Temporary Password: <b>${password} </b> </span></p> <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> </span></p>
             <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">  Please use the temporary password to log in to your account. Once logged in,
				 you will be prompted to create a new, personalized password. </span></p> 
				 
				<br> 
				 <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Instructions: </span></p> 
			 <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> 1.Visit our login page at <a href="${Url}" target="_blank" class="" style=""> <span style="text-decoration: none; color: blue(82);" class="">${Url} 
  </span></a></p> 
  <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">2.Enter your email address ${Email} and the temporary password provided above. </span></p> 
  <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">3.Follow the on-screen instructions to create a new password for your account.</span></p> 
  <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">4.Ensure your new password meets our security requirements. </span></p> 
  
  <br>
  
  
   <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Contact us: </span></p> 
  <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">The   ${orgName} support team is available 24/7 to help you with your questions and make sure you have the best experience. To get in touch, please visit email:${supportMail} or call: ${phone} . </span></p> 

    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Best, </span></p>  
    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">The ${orgName} Team. </span></p> 
     </span></p><br><br>
 <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 12pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""> Questions? Reach us at: <br> <a href="${portalUrl}" target="_blank" class="" style=""> <span style="text-decoration: none; color: blue(82);" class="">${supportMail} </span></a></p>
             
     <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 12pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class="">This email was sent to <br>
${Email}
      
     </span></p>        
 <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""><b>Terms of use </b> </span></p> 
 
 <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 12pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""><b>${whiteLabelName}  <br></b> ${Address}</span></p> 
 