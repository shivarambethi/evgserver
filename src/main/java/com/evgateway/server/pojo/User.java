package com.evgateway.server.pojo;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@NamedQueries({ @NamedQuery(name = "@HQL_GET_ALL_USERS", query = "from User where username!='admin' order by id"),
		@NamedQuery(name = "@HQL_GET_ASSIGN_USERS", query = "from User u,Usersinroles ur,Role r where u.id=ur.user_id and u.enabled=true and ur.role_id=r.id and r.rolename in(:rolename) order by u.id") })
@JsonIgnoreProperties(ignoreUnknown = true, value = { "passwords", "securityAnswerses", "roles",
		"hibernateLazyInitializer", "handler", "sites", "stations", "party", "driverProfileGroup" })
public class User implements UserDetails, CredentialsContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7892624231406935374L;
	protected Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserId", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String oldRefId;

	@Column(name = "FirstName", nullable = false, length = 50)
	private String firstName;

	@Column(name = "LastName", length = 50)
	private String lastName;

	@Column(name = "username", unique = true, length = 50)
	private String username;

	@Column(name = "email", unique = true, length = 50)
	private String email;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	private long orgId;

	private String uid;

	private Set<Role> roles = new HashSet<Role>(0);

	private Set<Address> address = new HashSet<Address>(0);

	private Set<Password> passwords = new HashSet<Password>(0);

	private Set<Profile> profiles = new HashSet<Profile>(0);

	private Set<Accounts> account = new HashSet<Accounts>(0);

	private Set<Organization> org = new HashSet<Organization>();

	// private Set<Station> stations = new HashSet<Station>();
	private PermissionInRevenue revenuePermission;

	@Transient
	@Column(name = "token", length = 250)
	private String token;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "Modified_Date", length = 10)
	private Date modifiedDate;

	@Column
	private String createdBy;
	@Column
	private String lastModifiedBy;

	/*
	 * @Transient
	 * 
	 * @JsonIgnore private String confirmPassword;
	 */

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getOldRefId() {
		return oldRefId;
	}

	public void setOldRefId(String oldRefId) {
		this.oldRefId = oldRefId;
	}

	@Transient
	public PermissionInRevenue getRevenuePermission() {
		return this.revenuePermission;
	}

	public void setRevenuePermission(final PermissionInRevenue revenuePermission) {
		this.revenuePermission = revenuePermission;
	}

	public User() {
	}

	public User(boolean enabled, String firstName) {
		this.enabled = enabled;
		this.firstName = firstName;
	}

	public User(boolean enabled, String firstName, String lastName, String username, Set<Profile> profiles,
			Set<Role> roles, Set<Password> passwords) {

		this.enabled = enabled;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.profiles = profiles;
		this.roles = roles;
		this.passwords = passwords;
	}

	public User(String firstName, String lastName, String username, String email, boolean enabled, Set<Role> roles,
			long orgId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;

		this.enabled = enabled;
		this.roles = roles;
		this.orgId = orgId;

	}

	public User(String firstName, String lastName, String username, String email, boolean enabled, Set<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.enabled = enabled;
		this.roles = roles;
		// this.orgId = orgId;

	}

	public User(String firstName, String username, boolean enabled) {

		this.firstName = firstName;
		this.username = username;
		this.enabled = enabled;
	}

	public User(Set<Password> passwords, String firstName, String username, boolean enabled, Set<Role> roles) {
		this.passwords = passwords;
		this.firstName = firstName;
		this.username = username;
		this.enabled = enabled;
		this.roles = roles;
	}

	public User(String firstName, String username, boolean enabled, Set<Role> roles) {

		this.firstName = firstName;
		this.username = username;
		this.enabled = enabled;
		this.roles = roles;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinTable(name = "usersinroles", uniqueConstraints = @UniqueConstraint(columnNames = { "role_id",
			"user_id" }), joinColumns = {
					@JoinColumn(name = "user_id", nullable = false, updatable = false, referencedColumnName = "UserId") }, inverseJoinColumns = {
							@JoinColumn(name = "role_id", nullable = false, updatable = false, referencedColumnName = "id") })
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	public Set<Password> getPasswords() {
		return passwords;
	}

	public void setPasswords(Set<Password> passwords) {
		this.passwords = passwords;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	public Set<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}

	@Transient
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	public Set<Accounts> getAccount() {
		return account;
	}

	public void setAccount(Set<Accounts> account) {
		this.account = account;
	}

	/*
	 * @ManyToMany(mappedBy = "stations", fetch = FetchType.LAZY, cascade =
	 * CascadeType.ALL)
	 * 
	 * @Transient public Set<Station> getStations() { return stations; }
	 * 
	 * public void setStations(Set<Station> stations) { this.stations = stations; }
	 */

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Organization.class)
	@JoinTable(name = "users_in_org", uniqueConstraints = @UniqueConstraint(columnNames = { "orgId",
			"userId" }), joinColumns = {
					@JoinColumn(name = "userId", nullable = false, updatable = false, referencedColumnName = "userId") }, inverseJoinColumns = {
							@JoinColumn(name = "orgId", nullable = false, updatable = false, referencedColumnName = "id") })

	public Set<Organization> getOrg() {
		return org;
	}

	public void setOrg(Set<Organization> org) {
		this.org = org;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "orgId", length = 10, columnDefinition = "Numeric Default 0 Not Null")
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	@Override
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		try {
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

			for (Role role : getRoles()) {
				// System.out.println("ali found role "+
				// role.getRolename());
				authorities.add(role);
			}
			return authorities;
			// authorities.addAll(getPermissions());

		} catch (Exception e) {
			// Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		// return true = account is valid / not expired
		return true;
	}

	@Transient
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		// return true = account is not locked
		return true;
	}

	@JsonIgnore
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		// return true = password is valid / not expired
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;

		if (o instanceof User) {
			final User other = (User) o;
			return BaseEntity.equal(getId(), other.getId()) && BaseEntity.equal(getUsername(), other.getUsername())
					&& BaseEntity.equal(getPassword(), other.getPassword())
					&& BaseEntity.equal(this.isEnabled(), other.isEnabled());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return BaseEntity.hashCode(getId(), getUsername(), getPassword(), isEnabled());
	}

	@JsonIgnore
	@Override
	@Transient
	public void eraseCredentials() {

	}

	@Override
	@Transient
	@JsonIgnore
	public String getPassword() {
		if (passwords == null || (passwords != null && passwords.isEmpty()))
			return null;

		return passwords.iterator().next().getPassword();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
