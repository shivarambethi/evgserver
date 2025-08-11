

package com.evgateway.server.pojo;

import javax.persistence.Table;

@Table(name = "permission_in_revenue")
@javax.persistence.Entity
public class PermissionInRevenue extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    private long userId;
    private boolean showRevenue;
    private boolean changePrice;
    
    public long getUserId() {
        return this.userId;
    }
    
    public void setUserId(final long userId) {
        this.userId = userId;
    }
    
    public boolean isShowRevenue() {
        return this.showRevenue;
    }
    
    public void setShowRevenue(final boolean showRevenue) {
        this.showRevenue = showRevenue;
    }
    
    public boolean isChangePrice() {
        return this.changePrice;
    }
    
    public void setChangePrice(final boolean changePrice) {
        this.changePrice = changePrice;
    }
    
    @Override
    public String toString() {
        return "PermissionInRevenue [userId=" + this.userId + ", showRevenue=" + this.showRevenue + ", changePrice=" + this.changePrice + "]";
    }
}
