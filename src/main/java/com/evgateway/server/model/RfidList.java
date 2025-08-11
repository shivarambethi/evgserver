package com.evgateway.server.model;

import java.util.Objects;

public class RfidList {

	String idTag;

	public String getIdTag() {
		return idTag;
	}

	public void setIdTag(String idTag) {
		this.idTag = idTag;
	}
	
	
	

	@Override
	public int hashCode() {
		return Objects.hash(idTag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RfidList other = (RfidList) obj;
		return Objects.equals(idTag, other.idTag);
	}

	@Override
	public String toString() {
		return "RfidList [idTag=" + idTag + "]";
	}

	

}
