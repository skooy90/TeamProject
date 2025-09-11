package mes.DTO;

import java.sql.Date;
import java.sql.Timestamp;
	// 품질 관리
public class QualityDTO {
    private String qualityNo;           // 검사번호 (I0001)
    private String workNo;              // 작업번호 (FK)
    private String standardCode;        // 제품코드 (FK)
    private String employeeNo;          // 검사자 (FK)
    private String quResult;            // 검사 결과 (양품/불량)
    private int quQuantity;             // 양품 수량
    private Date quManufactureDate;     // 제조일
    private int defectQuantity;         // 불량 수량
    private Timestamp inspectionDate;   // 검사일시
    private Date createDate;            // 생성일
    private Date updateDate;            // 수정일
    private String stName;

    // Getter & Setter
    public String getQualityNo() { return qualityNo; }
    public void setQualityNo(String qualityNo) { this.qualityNo = qualityNo; }
    
    public String getWorkNo() { return workNo; }
    public void setWorkNo(String workNo) { this.workNo = workNo; }
    
    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public String getQuResult() { return quResult; }
    public void setQuResult(String quResult) { this.quResult = quResult; }
    
    public int getQuQuantity() { return quQuantity; }
    public void setQuQuantity(int quQuantity) { this.quQuantity = quQuantity; }
    
    public Date getQuManufactureDate() { return quManufactureDate; }
    public void setQuManufactureDate(Date quManufactureDate) { this.quManufactureDate = quManufactureDate; }
    
    public int getDefectQuantity() { return defectQuantity; }
    public void setDefectQuantity(int defectQuantity) { this.defectQuantity = defectQuantity; }
    
    public Timestamp getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Timestamp inspectionDate) { this.inspectionDate = inspectionDate; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    public String getStName() { return stName; }
    public void setStName(String stName) { this.stName = stName; }
    
    @Override
    public String toString() {
        return "QualityDTO{" +
                "qualityNo='" + qualityNo + '\'' +
                ", workNo='" + workNo + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", quResult='" + quResult + '\'' +
                ", quQuantity=" + quQuantity +
                ", quManufactureDate=" + quManufactureDate +
                ", defectQuantity=" + defectQuantity +
                ", inspectionDate=" + inspectionDate +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
