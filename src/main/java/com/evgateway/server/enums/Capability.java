package com.evgateway.server.enums;

public enum Capability {
	
	CHARGING_PROFILE_CAPABLE, // The EVSE supports charging profiles. Sending Charging Profiles is not yet supported by OCPI.
    CREDIT_CARD_PAYABLE, // Charging at this EVSE can be payed with credit card.
    REMOTE_START_STOP_CAPABLE, // The EVSE can remotely be started/stopped.
    RESERVABLE, // The EVSE can be reserved.
    RFID_READER, // Charging at this EVSE can be authorized with a RFID token
    UNLOCK_CAPABLE, // Connectors have mechanical lock that can be requested by the eMSP to be unlocked.

}
