
package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@javax.persistence.Entity
@Table(name = "user_party")
public class UserParty extends BaseEntityString
{
    private static  final long serialVersionUID = 1L;
    private String client_name;
    private String client_id;
    @Column(name = "role_name")
    private String role_name;
    private String address;
    private String country;
    private String description;
    @Column(name = "org_id")
    private long org_id;
    @Column(name = "org_type")
    private String org_type;
    private String status;
    @Column(name = "app_version")
    private String app_version;
    @Column(name = "shared_token")
    private String shared_token;
    @Column(name = "user_id")
    private long user_id;
    @Column(name = "secret_token")
    private String secret_token;
    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date", length = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date creation_date;
    @Temporal(TemporalType.DATE)
    @Column(name = "modified_date", length = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date modified_date;
    private boolean generated;
    @Column(name = "created_by")
    private String created_by;
    
    public String getClient_name() {
        return this.client_name;
    }
    
    public void setClient_name( String client_name) {
        this.client_name = client_name;
    }
    
    public String getClient_id() {
        return this.client_id;
    }
    
    public void setClient_id( String client_id) {
        this.client_id = client_id;
    }
    
    public String getRole_name() {
        return this.role_name;
    }
    
    public void setRole_name( String role_name) {
        this.role_name = role_name;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress( String address) {
        this.address = address;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public void setCountry( String country) {
        this.country = country;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription( String description) {
        this.description = description;
    }
    
    public long getOrg_id() {
        return this.org_id;
    }
    
    public void setOrg_id( long org_id) {
        this.org_id = org_id;
    }
    
    public String getOrg_type() {
        return this.org_type;
    }
    
    public void setOrg_type( String org_type) {
        this.org_type = org_type;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus( String status) {
        this.status = status;
    }
    
    public String getApp_version() {
        return this.app_version;
    }
    
    public void setApp_version( String app_version) {
        this.app_version = app_version;
    }
    
    public String getShared_token() {
        return this.shared_token;
    }
    
    public void setShared_token( String shared_token) {
        this.shared_token = shared_token;
    }
    
    public long getUser_id() {
        return this.user_id;
    }
    
    public void setUser_id( long user_id) {
        this.user_id = user_id;
    }
    
    public String getSecret_token() {
        return this.secret_token;
    }
    
    public void setSecret_token( String secret_token) {
        this.secret_token = secret_token;
    }
    
    public Date getCreation_date() {
        return this.creation_date;
    }
    
    public void setCreation_date( Date creation_date) {
        this.creation_date = creation_date;
    }
    
    public Date getModified_date() {
        return this.modified_date;
    }
    
    public void setModified_date( Date modified_date) {
        this.modified_date = modified_date;
    }
    
    public boolean isGenerated() {
        return this.generated;
    }
    
    public void setGenerated( boolean generated) {
        this.generated = generated;
    }
    
    public String getCreated_by() {
        return this.created_by;
    }
    
    public void setCreated_by( String created_by) {
        this.created_by = created_by;
    }
    
    @Override
    public String toString() {
        return "UserParty [client_name=" + client_name + ", client_id=" + client_id + ", role_name=" + role_name + ", address=" + address + ", country=" +country + ", description=" +description + ", org_id=" + org_id + ", org_type=" + org_type + ", status=" + status + ", app_version=" + app_version + ", shared_token=" + shared_token + ", user_id=" + user_id + ", secret_token=" +secret_token + ", creation_date=" + creation_date + ", modified_date=" + modified_date + ", generated=" + generated + ", created_by=" + created_by + "]";
    }
}
