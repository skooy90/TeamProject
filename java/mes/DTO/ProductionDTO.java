package mes.DTO;

import java.sql.Date;
	// 생산관리 
public class ProductionDTO {
    private String productionNo;    // 생산번호 (P0001)
    private String standardCode;    // 제품코드 (FK)
    private String employeeNo;      // 담당자 (FK)
    private Date prStart;           // 생산 시작일
    private Date prEnd;             // 생산 종료일
    private int prTarget;           // 생산 목표량
    private int prCompleted;        // 생산 완료량
    private Date createDate;        // 생성일
    private Date updateDate;        // 수정일
    
    // Getter & Setter
    public String getProductionNo() { return productionNo; }
    public void setProductionNo(String productionNo) { this.productionNo = productionNo; }
    
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public Date getPrStart() { return prStart; }
    public void setPrStart(Date prStart) { this.prStart = prStart; }
    
    public Date getPrEnd() { return prEnd; }
    public void setPrEnd(Date prEnd) { this.prEnd = prEnd; }
    
    public int getPrTarget() { return prTarget; }
    public void setPrTarget(int prTarget) { this.prTarget = prTarget; }
    
    public int getPrCompleted() { return prCompleted; }
    public void setPrCompleted(int prCompleted) { this.prCompleted = prCompleted; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    @Override
    public String toString() {
        return "ProductionDTO{" +
                "productionNo='" + productionNo + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", prStart=" + prStart +
                ", prEnd=" + prEnd +
                ", prTarget=" + prTarget +
                ", prCompleted=" + prCompleted +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
