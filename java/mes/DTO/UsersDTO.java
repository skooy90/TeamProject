package mes.DTO;

import java.sql.Date;
	//사용자관리 (로그인 만들 때도 참고)
public class UsersDTO {
    private String employeeNo;      // 사원번호 (K0001)
    private String usName;          // 사용자 이름
    private String usPosition;      // 사용자 직급
    private String usPassword;      // 사용자 비밀번호
    private String usAuthority;     // 사용자 권한 (관리자/사용자)
    private int usPsUpStatus;       // 비밀번호 수정 상태
    private Date createDate;        // 생성일
    private Date updateDate;        // 수정일
    
    // Getter & Setter
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public String getUsName() { return usName; }
    public void setUsName(String usName) { this.usName = usName; }
    
    public String getUsPosition() { return usPosition; }
    public void setUsPosition(String usPosition) { this.usPosition = usPosition; }
    
    
    public String getUsPassword() { return usPassword; }
    public void setUsPassword(String usPassword) { this.usPassword = usPassword; }
    
    public String getUsAuthority() { return usAuthority; }
    public void setUsAuthority(String usAuthority) { this.usAuthority = usAuthority; }
    
    public int getUsPsUpStatus() { return usPsUpStatus; }
    public void setUsPsUpStatus(int usPsUpStatus) { this.usPsUpStatus = usPsUpStatus; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    @Override
    public String toString() {
        return "UsersDTO{" +
                "employeeNo='" + employeeNo + '\'' +
                ", usName='" + usName + '\'' +
                ", usPosition='" + usPosition + '\'' +
                ", usPassword='" + usPassword + '\'' +
                ", usAuthority='" + usAuthority + '\'' +
                ", usPsUpStatus=" + usPsUpStatus +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
