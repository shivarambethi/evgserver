package com.evgateway.server.pojo;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zone")
// @JsonIgnoreProperties(ignoreUnknown = true, value = {"profiles"})
public class Zone implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "zone_id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "zone_name", nullable = false)
	private String name;

	@Column(name = "utc_code", length = 10, nullable = false)
	private String utcCode;

	// @JsonIgnore
	// private Set<Country> country = new HashSet<Country>(0);

	// private List<Profile> profiles = new ArrayList<Profile>(0);

	public Zone() {

	}

	public Zone(Integer id, String name, String utcCode) {
		this.id = id;
		this.name = name;
		this.utcCode = utcCode;

	}

	public Zone(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "zone") public List<Profile>
	 * getProfiles() { return this.profiles; }
	 * 
	 * public void setProfiles(List<Profile> profiles) { this.profiles = profiles; }
	 */

	/*
	 * @OneToMany(targetEntity = Country.class, mappedBy = "zone", fetch =
	 * FetchType.LAZY) public Set<Country> getCountry() { return country; }
	 * 
	 * public void setCountry(Set<Country> country) { this.country = country; }
	 */

	public String getUtcCode() {
		return utcCode;
	}

	public void setUtcCode(String utcCode) {
		this.utcCode = utcCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;

		if (o instanceof Zone) {
			final Zone other = (Zone) o;
			return BaseEntity.equal(getId(), other.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return BaseEntity.hashCode(getId());
	}

}