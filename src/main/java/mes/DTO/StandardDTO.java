package com.mes.dto;

import java.sql.Date;

public class StandardDTO {
    private String standardCode;    // 제품코드 (RA0001, SE0001, FI0001)
    private String stType;          // 제품유형 (원자제, 반제품, 완제품)
    private String stName;          // 제품 이름
    private int stQuantity;         // 제품 갯수
    private String stUnit;          // 제품 단위
    private Date createDate;        // 생성일
    private Date updateDate;        // 수정일
    
    // Getter & Setter
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getStType() { return stType; }
    public void setStType(String stType) { this.stType = stType; }
    
    public String getStName() { return stName; }
    public void setStName(String stName) { this.stName = stName; }
    
    public int getStQuantity() { return stQuantity; }
    public void setStQuantity(int stQuantity) { this.stQuantity = stQuantity; }
    
    public String getStUnit() { return stUnit; }
    public void setStUnit(String stUnit) { this.stUnit = stUnit; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    @Override
    public String toString() {
        return "StandardDTO{" +
                "standardCode='" + standardCode + '\'' +
                ", stType='" + stType + '\'' +
                ", stName='" + stName + '\'' +
                ", stQuantity=" + stQuantity +
                ", stUnit='" + stUnit + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
