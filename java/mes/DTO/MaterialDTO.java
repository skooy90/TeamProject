package mes.DTO;

import java.sql.Date;

public class MaterialDTO {
    private String materialCode;        // 재고코드 (MA0001)
    private String standardCode;        // 제품코드 (FK)
    private String employeeNo;          // 담당자 (FK)
    private int maQuantity;             // 재고량
    private Date maCreationDate;        // 생성일
    private Date maUpdateDate;          // 수정일
    
    // JOIN 데이터 필드 (DB 테이블과 일치)
    private String stName;              // STANDARD.ST_NAME
    private String stType;              // STANDARD.ST_TYPE
    private String stUnit;              // STANDARD.ST_UNIT
    private String usName;              // USERS.US_NAME
    private String usPosition;          // USERS.US_POSITION
    
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
    
    // JOIN 데이터 Getter & Setter
    public String getStName() { return stName; }
    public void setStName(String stName) { this.stName = stName; }
    
    public String getStType() { return stType; }
    public void setStType(String stType) { this.stType = stType; }
    
    public String getStUnit() { return stUnit; }
    public void setStUnit(String stUnit) { this.stUnit = stUnit; }
    
    public String getUsName() { return usName; }
    public void setUsName(String usName) { this.usName = usName; }
    
    public String getUsPosition() { return usPosition; }
    public void setUsPosition(String usPosition) { this.usPosition = usPosition; }
    
    @Override
    public String toString() {
        return "MaterialDTO{" +
                "materialCode='" + materialCode + '\'' +
                ", standardCode='" + standardCode + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", maQuantity=" + maQuantity +
                ", maCreationDate=" + maCreationDate +
                ", maUpdateDate=" + maUpdateDate +
                ", stName='" + stName + '\'' +
                ", stType='" + stType + '\'' +
                ", stUnit='" + stUnit + '\'' +
                ", usName='" + usName + '\'' +
                ", usPosition='" + usPosition + '\'' +
                '}';
    }
}
