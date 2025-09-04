package com.mes.dto;

import java.sql.Date;

public class BOMDTO {
    private String bomNo;               // BOM번호 (B0001)
    private String standardCode;        // 제품코드 (FK)
    private String bomDescription;      // BOM 설명
    private String bomType;             // BOM 유형
    private int bomOrder;               // BOM 순서
    private String bomImage;            // BOM 이미지
    private Date createDate;            // 생성일
    private Date updateDate;            // 수정일
    
    // Getter & Setter
    public String getBomNo() { return bomNo; }
    public void setBomNo(String bomNo) { this.bomNo = bomNo; }
    
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getBomDescription() { return bomDescription; }
    public void setBomDescription(String bomDescription) { this.bomDescription = bomDescription; }
    
    public String getBomType() { return bomType; }
    public void setBomType(String bomType) { this.bomType = bomType; }
    
    public int getBomOrder() { return bomOrder; }
    public void setBomOrder(int bomOrder) { this.bomOrder = bomOrder; }
    
    public String getBomImage() { return bomImage; }
    public void setBomImage(String bomImage) { this.bomImage = bomImage; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    @Override
    public String toString() {
        return "BOMDTO{" +
                "bomNo='" + bomNo + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", bomDescription='" + bomDescription + '\'' +
                ", bomType='" + bomType + '\'' +
                ", bomOrder=" + bomOrder +
                ", bomImage='" + bomImage + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
