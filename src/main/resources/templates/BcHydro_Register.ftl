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
         </tr><br>
         <td style="padding: 0in 10pt;" xml="lang" class="">
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 10pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Hello </span><span style="font-size: 15pt; font-family: Arial, sans-serif;" class="">${fullName}<span style="color: black;" class="">, </span></span></p>
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Thank you for creating your ${whiteLabelName} EV account. 
 </span></p>
             <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">With your account, you can:

   </span></p>
    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">*  Add funds to your wallet, making charging simpler
   </span></p>
  
    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">   *  Order a ${whiteLabelName} RFID card
   </span></p>

    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">*  Review your charging activity

   </span></p>

    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">*  View your Account History
   </span></p>
 
    <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">* Visit the FAQs tab to know more about the Web portal.
   </span></p>
 <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Download the ${whiteLabelName} EV app to manage your account and find EV chargers on the go.
      
     </span></p><br><br>
 <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 12pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""> Questions? Reach us at: <br> <a href="${portalUrl}" target="_blank" class="" style=""> <span style="text-decoration: none; color: blue(82);" class="">${supportEmail} </span></a></p>
             
     <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 12pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class="">This email was sent to <br>
${Email}
      
     </span></p>        
 <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""><b>Terms of use </b> </span></p> 
 
 <p class="" style="text-align: centre; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 12pt;"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""><b>${whiteLabelName}  <br></b> ${Address}</span></p> 
  </td>
   </table>

</body>

</html>