package com.mes.dto;

import java.sql.Date;

public class ProcessDTO {
    private String processNo;           // 공정번호 (PC0001)
    private String standardCode;        // 제품코드 (FK)
    private String prDescription;       // 공정 설명
    private String prType;              // 공정 유형 (혼합, 성형, 건조, 포장)
    private int prOrder;                // 공정 순서
    private String prImage;             // 공정 이미지
    private Date createDate;            // 생성일
    private Date updateDate;            // 수정일
    
    // Getter & Setter
    public String getProcessNo() { return processNo; }
    public void setProcessNo(String processNo) { this.processNo = processNo; }
    
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getPrDescription() { return prDescription; }
    public void setPrDescription(String prDescription) { this.prDescription = prDescription; }
    
    public String getPrType() { return prType; }
    public void setPrType(String prType) { this.prType = prType; }
    
    public int getPrOrder() { return prOrder; }
    public void setPrOrder(int prOrder) { this.prOrder = prOrder; }
    
    public String getPrImage() { return prImage; }
    public void setPrImage(String prImage) { this.prImage = prImage; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    @Override
    public String toString() {
        return "ProcessDTO{" +
                "processNo='" + processNo + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", prDescription='" + prDescription + '\'' +
                ", prType='" + prType + '\'' +
                ", prOrder=" + prOrder +
                ", prImage='" + prImage + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
