package com.mes.dto;

import java.sql.Date;

public class MaterialDTO {
    private String materialCode;        // 재고코드 (MA0001)
    private String standardCode;        // 제품코드 (FK)
    private String employeeNo;          // 담당자 (FK)
    private int maQuantity;             // 재고량
    private Date maCreationDate;        // 생성일
    private Date maUpdateDate;          // 수정일
    
    // Getter & Setter
    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public int getMaQuantity() { return maQuantity; }
    public void setMaQuantity(int maQuantity) { this.maQuantity = maQuantity; }
    
    public Date getMaCreationDate() { return maCreationDate; }
    public void setMaCreationDate(Date maCreationDate) { this.maCreationDate = maCreationDate; }
    
    public Date getMaUpdateDate() { return maUpdateDate; }
    public void setMaUpdateDate(Date maUpdateDate) { this.maUpdateDate = maUpdateDate; }
    
    @Override
    public String toString() {
        return "MaterialDTO{" +
                "materialCode='" + materialCode + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", maQuantity=" + maQuantity +
                ", maCreationDate=" + maCreationDate +
                ", maUpdateDate=" + maUpdateDate +
                '}';
    }
}
