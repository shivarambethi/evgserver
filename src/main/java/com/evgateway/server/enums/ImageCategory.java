package com.evgateway.server.enums;

public enum ImageCategory {
	
	CHARGER, // Photo of the physical device that contains one or more EVSEs.
    ENTRANCE, // Location entrance photo. Should show the car entrance to the location from street side.
    LOCATION, // Location overview photo.
    NETWORK, // logo of an associated roaming network to be displayed with the EVSE for example in lists, maps and detailed information view
    OPERATOR, // logo of the charge points operator, for example a municipality, to be displayed with the
    EVSEs, // detailed information view or in lists and maps, if no networkLogo is present
    OTHER, // Other
    OWNER, // logo of the charge points owner, for example a local store, to be displayed with the EVSEs detailed information view

}
