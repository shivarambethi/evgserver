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
         font-size: 20px;
         }
         table,tr,td,th {
         text-align: center;
         font-family: 'Times New Roman', Times, serif;
         font-size: 15px;
         border-collapse: collapse;
         adding: 6px;
         width:30% ;
         table-layout: auto;
         background-color: #fff;
         padding: 10px 5px 10px 5px;
         }
      </style>
   </head>
   <body style="margin: 0; padding: 0;">
      <table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse; width: 600px;margin-bottom: 20px;">
         <tr style="margin-top: 20px;" style="text-align:center";>
            
            <td  colspan="3"  bgcolor="#fff" >
               <img src="${logo}" style="display: block; width: 200px;text-align:center" alt="EvGateway"/>
            </td>
            
         </tr>
         <tr><td colspan="3" style="background: linear-gradient(#5f5fbb,#565698);"></td></tr>
         <tr>
         <td colspan="3" style="padding: 0in 12px;" xml="lang" class="">
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 12px; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Dear Customer &nbsp;</span><span style="font-size: 15pt; font-family: Arial, sans-serif;" class="">${accName}<span style="color: black;" class="">, </span></span></p>
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Thank you for charging with ${whiteLabelName}. </span></p>
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 12px; line-height: 22.5pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Questions?  Contact us 24/7 at <a href="tel:${contactUsNo}" target="_blank" class="" style=""><strong class="" style=""><span style="text-decoration: none; color: rgb(1, 19, 82);" class="">${contactUsNo}</span></strong></a>. </span></p>
            <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
               <tbody class="" style="">
                  <tr class="" style="">
                     <td style="padding: 0in;" xml="lang" valign="top" class="">
                        <div align="center" class="" style="">
                           <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                              <tbody class="" style="">
                                
                                <tr>
                                    <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                    </tr>
                                <tr>
                                    <td style="   padding: 0in;" xml="lang" valign="top"   class="">
                                       <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Charger Id:</p>
                                    </td>
                                     <td style="   padding: 0in;" xml="lang" valign="top"  class="">
                                       <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${StationId}</p>
                                    </td>
                                 </tr>
                                 <tr>
                                     <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                 </tr>
                                 <tr>
                                    <td style="  text-align: left; padding: 0in;" xml="lang" valign="top"   class="">
                                       <p class="" style="  margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> Session Id:  </span></p>
                                    </td>
                                     <td style="   padding: 0in;" xml="lang" valign="top"  class="">
                                       <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> ${sessionId} </span></p>
                                    </td>
                                 </tr>
                                 <tr ><td colspan="3" style="border-bottom: 2px solid #333"></td></tr>
                              </tbody>
                           </table>
                        </div>
                     </td>
                  </tr>
               </tbody>
            </table>
            <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt; display: none;" class="">&nbsp;</span></p>
            <div align="center" class="" style="">
               <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                  <tbody class="" style="">
                     <tr class="" style="">
                        <td style="border-top: none; border-right: none; border-left: none; border-image: initial; border-bottom: 0.25pt solid rgb(1, 19, 82); padding: 0in;" xml="lang" class=""></td>
                     </tr>
                  </tbody>
               </table>
            </div>
            <div class="" style="">
             
               <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                  <tbody class="" style="">
                     <tr class="" style="">
                        <td style="padding: 0pt 0pt 15pt;" xml="lang" class="">
                          
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  line-height: 18.75pt; text-align: left;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black; " class="">Charging pricing: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${Rate} (pre-tax) </span></p>
                                                   </td>
                                                </tr>
                                                <tr>
                                     			 <td class="" style="padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                			    </tr>
                                                <tr>
                                                <td style="  text-align: left; padding: 0in;" xml="lang" valign="top"   class="">
                                       				  <p class="" style="  margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> Address:  </span></p>
                                   				   </td>
                                   				   <td style="   padding: 0in;" xml="lang" valign="top"  class="">
                                      				 <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> ${Address} </span></p>
                                    			   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0;; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Charging time: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${Duration} </span></p>
                                                   </td>
                                                   </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Charging cost: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${TxnCost} </span></p>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0pt 0in 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style=""></tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Paid idle time: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${connectedTime} </span></p>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Grace period: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${graceTime} </span></p>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0pt 0in 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                           <td style="text-align: left; padding: 0px;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 12px; text-align: left; font-family: Calibri, sans-serif; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Transaction fee: </span></p>
                           </td>
                           <td style="padding: 0pt 0pt 0pt 0pt;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${txnFee} </span></p>
                           </td>
                        </tr>
                    <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                            <tr class="" style="">
                           <td style="text-align: left; padding: 0px;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 12px; text-align: left; font-family: Calibri, sans-serif; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Processing  fee: </span></p>
                           </td>
                           <td style="padding: 0pt 0pt 0pt 0pt;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${processingFee} </span></p>
                           </td>
                        </tr>
                        <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                         <tr class="" style="">
                           <td style="padding: 10px 0px; text-align: left;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 12px; text-align: left; font-family: Calibri, sans-serif; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Sales tax(${salesTaxPerc}%): </span></p>
                           </td>
                           <td style="padding: 10px 0px; text-align: left;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${saleTaxval} </span></p>
                           </td>
                        </tr>
                          <tr ><td colspan="3" style="border-bottom: 2px solid #333"></td></tr>
                        
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>

                                 </tbody>
                              </table>
                           </div>
                        </td>
                     </tr>
                  </tbody>
               </table>
            </div>
            
           
            <div align="center" class="" style="">
               <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                  <thead class="" style="">
                    <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                     <tr class="" style="">
                        <td style="padding: 0px; text-align: left;" xml="lang" class="">
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 32.25pt; text-align: left;" align="right"><strong class="" style=""><span style="font-size: 20px; font-family: Arial, sans-serif;" class="">Total paid:  </span></strong></p>
                        </td>
                        <td style="padding: 0px; text-align: right;" xml="lang" class="">
                           <p class="" style="margin: 0in; font-size: 12px; font-family: Calibri, sans-serif; text-align: right; line-height: 32.25pt;" align="right"><strong class="" style=""><span style="font-size: 20px; font-family: Arial, sans-serif;" class=""> ${TotalCost} </span></strong></p>
                        </td>
                     </tr>
                     <tr class="" style="">
                       <td style="padding: 0in;" xml="lang" class="">
                          <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Total energy delivered: </span></p>
                       </td>
                       <td style="padding: 0in;" xml="lang" class="">
                          <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${EnergyUsage} </span></p>
                       </td>
                    </tr>
                  </thead>
               </table>
            </div>  
         </td>         
         </tr>
          <tr ><td colspan="3" style="border-bottom: 3px solid #5f5fbb"></td></tr>
         <tr style="height: 100px;font-weight: 800;">
            <!-- <td style="background: linear-gradient(#6c6b6a, rgb(167, 166, 165)); "> <span class="" style="margin-right: 18.75pt; margin-left: 18.75pt; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 30pt; text-align: center; line-height: 12.75pt;" align="center"><span style="font-size: 12px; font-family: Arial, sans-serif; color: black;" class=""><br class="" style=""> Copyright ©  <a href="${portalUrl}" target="_blank" class="" style=""> <strong class="" style=""><span style="text-decoration: none; color: rgb(1, 19, 82);" class="">${whiteLabelName}</span></strong></a> 2023. All Rights Reserved. <br class="" style="">${orgAddress}</span></span></td> -->
            <td> 
              
                  <img src="${logos}" style="display: block; width: 150px;margin-left: 20px;" alt="EvGateway"/>
              
            </td>
            <td style="margin-left: 100px;height: 80px;"> 
              <span style="border-left: #4d4c4b solid 2px;"></span>
            </td>
            <td> 
               <span style="font-size: 11pt; font-family: Calibri, sans-serif; text-align: left;">
              support@evgateway.com<br><br>
              949-945-2000
               </span>
            </td>
                  
         
         </tr>
         
      </table>
   </body>
</html>