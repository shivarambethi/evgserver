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
         table,tr,td,th {
         text-align: center;
         font-family: 'Times New Roman', Times, serif;
         font-size: medium;
         border-collapse: collapse;
         adding: 6px;
         width:30% ;
         table-layout: auto;
         background-color: #eaeaea;
         padding: 10px 5px 10px 5px;
         }
      </style>
   </head>
   <body style="margin: 0; padding: 0;">
      <table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse; width: 600px;">
         <tr>
            <td align="center" bgcolor="#eaeaea" style="padding: 40px 200px 30px 200px; width: 300px;" >
               <img src="cid:logo" style="display: block; width: 200px;height: 65px;" alt="EvGateway"/>
            </td>
         </tr>
         <td style="padding: 0in 10pt;" xml="lang" class="">
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 10pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Querida </span><span style="font-size: 15pt; font-family: Arial, sans-serif;" class="">${accName}<span style="color: black;" class="">, </span></span></p>
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 7.5pt; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Gracias por cargar con ${whiteLabelName}. </span></p>
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 10pt; line-height: 22.5pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">¿Preguntas? Contáctenos las 24 horas al día en <a href="tel:${contactUsNo}" target="_blank" class="" style=""><strong class="" style=""><span style="text-decoration: none; color: rgb(1, 19, 82);" class="">${contactUsNo}</span></strong></a>. </span></p>
            <p class="" style="text-align: left; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 10pt;  line-height: 18.75pt;" align="center"><strong class="" style=""><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${curDate} </span></strong></p>
            <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
               <tbody class="" style="">
                  <tr class="" style="">
                     <td style="padding: 0in;" xml="lang" valign="top" class="">
                        <div align="center" class="" style="">
                           <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                              <tbody class="" style="">
                                 <tr class="" style="">
                                    <td style="  padding: 0in;" xml="lang"   class="">
                                       <p class="" style=" text-align: left; margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; line-height: 18.75pt; width: 100%"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${stnAddress}</span></p>
                                    </td>

                                </tr>
                                <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                                <tr>
                                    <td style="   padding: 0in;" xml="lang" valign="top"   class="">
                                       <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Identificación del cargador:</p>
                                    </td>
                                     <td style="   padding: 0in;" xml="lang" valign="top"  class="">
                                       <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${StationId}</p>
                                    </td>
                                 </tr>
                                 <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                                 <tr>
                                    <td style="  text-align: left; padding: 0in;" xml="lang" valign="top"   class="">
                                       <p class="" style="  margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> ID de sesión:  </span></p>
                                    </td>
                                     <td style="   padding: 0in;" xml="lang" valign="top"  class="">
                                       <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt; " align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class=""> ${sessionId} </span></p>
                                    </td>
                                 </tr>
                                 <tr>
                                      <td class="" style="padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                 </tr>
                              </tbody>
                           </table>
                        </div>
                     </td>
                  </tr>
               </tbody>
            </table>
            <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt; display: none;" class="">&nbsp;</span></p>
            <div align="center" class="" style="">
               <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                  <tbody class="" style="">
                     <tr class="" style="">
                        <td style="border-top: none; border-right: none; border-left: none; border-image: initial; border-bottom: 2.25pt solid rgb(1, 19, 82); padding: 0in;" xml="lang" class=""></td>
                     </tr>
                  </tbody>
               </table>
            </div>
            <div class="" style="">
             
               <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                  <tbody class="" style="">
                     <tr class="" style="">
                        <td style="padding: 0pt 0pt 15pt;" xml="lang" class="">
                          
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  line-height: 18.75pt; text-align: left;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black; " class="">Precios de carga: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${Rate} (pre-tax) </span></p>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0;; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Tiempo de carga: </span></p>
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
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Costo de carga: </span></p>
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
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0pt 0in 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">De marcha en vacío: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;    text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${connectedTimePrice} </span></p>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Tiempo de inactividad pagado: </span></p>
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
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Periodo de gracia: </span></p>
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
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0pt 0in 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Estado final de carga: </span></p>
                                                   </td>
                                                   <td style="padding: 0in;" xml="lang" class="">
                                                      <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${socEndVal} </span></p>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: center;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></p>
                           <div align="center" class="" style="">
                              <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                 <tbody class="" style="">
                                    <tr class="" style="">
                                       <td style="padding: 0in;" xml="lang" class="">
                                          <table class="" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
                                             <tbody class="" style="">
                                                <tr class="" style="">
                           <td style="text-align: left; padding: 0px;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 10pt; text-align: left; font-family: Calibri, sans-serif; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Tarifa de transacción: </span></p>
                           </td>
                           <td style="padding: 0pt 0pt 0pt 0pt;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${txnFee} </span></p>
                           </td>
                        </tr>
                    <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                            <tr class="" style="">
                           <td style="text-align: left; padding: 0px;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 10pt; text-align: left; font-family: Calibri, sans-serif; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">cuota de procesamiento: </span></p>
                           </td>
                           <td style="padding: 0pt 0pt 0pt 0pt;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${processingFee} </span></p>
                           </td>
                        </tr>
                        <tr>
                                                <td class="" style="margin:0in; padding: 0px;" align="center"><span style="font-size: 11pt;" class="">&nbsp;</span></td>
                                            </tr>
                         <tr class="" style="">
                           <td style="padding: 10px 0px; text-align: left;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 10pt; text-align: left; font-family: Calibri, sans-serif; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Impuesto sobre las ventas (0%): </span></p>
                           </td>
                           <td style="padding: 10px 0px; text-align: left;" xml="lang" valign="top" class="">
                              <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${socValue} </span></p>
                           </td>
                        </tr>
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
                  <tbody class="" style="">
                     <tr class="" style="">
                        <td style="border-top: none; border-right: none; border-left: none; border-image: initial; border-bottom: 2.25pt solid rgb(1, 19, 82); padding: 0in;" xml="lang" class=""></td>
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
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 32.25pt; text-align: left;" align="right"><strong class="" style=""><span style="font-size: 20pt; font-family: Arial, sans-serif;" class="">Total pagada:  </span></strong></p>
                        </td>
                        <td style="padding: 0px; text-align: right;" xml="lang" class="">
                           <p class="" style="margin: 0in; font-size: 10pt; font-family: Calibri, sans-serif; text-align: right; line-height: 32.25pt;" align="right"><strong class="" style=""><span style="font-size: 20pt; font-family: Arial, sans-serif;" class=""> ${TotalCost} </span></strong></p>
                        </td>
                     </tr>
                     <tr class="" style="">
                       <td style="padding: 0in;" xml="lang" class="">
                          <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif; text-align: left; line-height: 18.75pt;"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">Energía total entregada: </span></p>
                       </td>
                       <td style="padding: 0in;" xml="lang" class="">
                          <p class="" style="margin: 0in; margin-left: 0in; font-size: 11pt; font-family: Calibri, sans-serif;  text-align: right; line-height: 18.75pt;" align="right"><span style="font-size: 15pt; font-family: Arial, sans-serif; color: black;" class="">${EnergyUsage} </span></p>
                       </td>
                    </tr>
                  </thead>
               </table>
            </div>    
            <p class="" style="margin-right: 18.75pt; margin-left: 18.75pt; font-size: 11pt; font-family: Calibri, sans-serif; margin-bottom: 30pt; text-align: center; line-height: 12.75pt;" align="center"><span style="font-size: 10pt; font-family: Arial, sans-serif; color: black;" class=""><br class="" style=""> © 2021 <a href="${portalUrl}" target="_blank" class="" style=""> <strong class="" style=""><span style="text-decoration: none; color: rgb(1, 19, 82);" class="">${whiteLabelName}.</span></strong></a> Reservados todos los derechos. <br class="" style="">${orgAddress}</span></p>
         </td>
      </table>
   </body>
</html>