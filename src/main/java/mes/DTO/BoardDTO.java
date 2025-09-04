package com.mes.dto;

import java.sql.Date;

public class BoardDTO {
    private String postNo;              // 게시글번호 (P0001)
    private String employeeNo;          // 작성자 (FK)
    private String boCategory;          // 게시판 카테고리
    private String boTitle;             // 게시판 제목
    private String boContent;           // 게시판 내용
    private String boWriter;            // 게시판 작성자
    private Date boCreationDate;        // 게시판 작성일
    private Date createDate;            // 생성일
    private Date updateDate;            // 수정일
    
    // Getter & Setter
    public String getPostNo() { return postNo; }
    public void setPostNo(String postNo) { this.postNo = postNo; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public String getBoCategory() { return boCategory; }
    public void setBoCategory(String boCategory) { this.boCategory = boCategory; }
    
    public String getBoTitle() { return boTitle; }
    public void setBoTitle(String boTitle) { this.boTitle = boTitle; }
    
    public String getBoContent() { return boContent; }
    public void setBoContent(String boContent) { this.boContent = boContent; }
    
    public String getBoWriter() { return boWriter; }
    public void setBoWriter(String boWriter) { this.boWriter = boWriter; }
    
    public Date getBoCreationDate() { return boCreationDate; }
    public void setBoCreationDate(Date boCreationDate) { this.boCreationDate = boCreationDate; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    
    @Override
    public String toString() {
        return "BoardDTO{" +
                "postNo='" + postNo + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", boCategory='" + boCategory + '\'' +
                ", boTitle='" + boTitle + '\'' +
                ", boContent='" + boContent + '\'' +
                ", boWriter='" + boWriter + '\'' +
                ", boCreationDate=" + boCreationDate +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
