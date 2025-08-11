package com.evgateway.server.enums;

public enum TariffDimensionType {
	
	ENERGY, // defined in kWh, step_size multiplier: 1 Wh
    FLAT, // flat fee, no unit
    PARKING_TIME, // time not charging: defined in hours, step_size multiplier: 1 second
    TIME // time charging: defined in hours, step_size multiplier: 1 second

}
