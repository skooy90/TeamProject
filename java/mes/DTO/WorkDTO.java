package mes.DTO;

import java.sql.Date;
import java.sql.Timestamp;
	// 작업관리
public class WorkDTO {
    private String workNo;          // 작업번호 (W0001)
    private String productionNo;    // 생산번호 (FK)
    private String standardCode;    // 제품코드 (FK)
    private String employeeNo;      // 담당자 (FK)
    private Date woSchedule;        // 작업 일정
    private int woQuantity;         // 작업량
    private String woStatus;        // 작업 상태
    private int woCompleted;        // 작업 완료량
    private Timestamp woStart;      // 작업 시작일
    private Timestamp woEnd;        // 작업 종료일
    private Date createDate;        // 생성일
    private Date updateDate;        // 수정일
    private String stName; 
    // Getter & Setter
    public String getWorkNo() { return workNo; }
    public void setWorkNo(String workNo) { this.workNo = workNo; }
    
    public String getProductionNo() { return productionNo; }
    public void setProductionNo(String productionNo) { this.productionNo = productionNo; }
    
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public Date getWoSchedule() { return woSchedule; }
    public void setWoSchedule(Date woSchedule) { this.woSchedule = woSchedule; }
    
    public int getWoQuantity() { return woQuantity; }
    public void setWoQuantity(int woQuantity) { this.woQuantity = woQuantity; }
    
    public String getWoStatus() { return woStatus; }
    public void setWoStatus(String woStatus) { this.woStatus = woStatus; }
    
    public int getWoCompleted() { return woCompleted; }
    public void setWoCompleted(int woCompleted) { this.woCompleted = woCompleted; }
    
    public Timestamp getWoStart() { return woStart; }
    public void setWoStart(Timestamp woStart) { this.woStart = woStart; }
    
    public Timestamp getWoEnd() { return woEnd; }
    public void setWoEnd(Timestamp woEnd) { this.woEnd = woEnd; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    public String getStName() { return stName; } // 쪼인
    public void setStName(String stName) { this.stName = stName; }

    
    @Override
    public String toString() {
        return "WorkDTO{" +
                "workNo='" + workNo + '\'' +
                ", productionNo='" + productionNo + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", woSchedule=" + woSchedule +
                ", woQuantity=" + woQuantity +
                ", woStatus='" + woStatus + '\'' +
                ", woCompleted=" + woCompleted +
                ", woStart=" + woStart +
                ", woEnd=" + woEnd +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
