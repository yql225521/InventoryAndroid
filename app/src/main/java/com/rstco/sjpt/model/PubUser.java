package com.rstco.sjpt.model;

import java.io.Serializable;

 
public class PubUser   implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String userId;

	private String accounts;

	private String isAlloc;

	private String isLogin;

	private String isValid;

	private String isSys; 

	private String password;

	private String remark;

	private String username;

	private Integer securityLevel;

	private String departmentId;  //部门号       

	private String employeeId;

	private String employeeName;

	private String departmentName;
	
	private String clearPassword;

	public PubUser() {
	}

	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsAlloc() {
		return this.isAlloc;
	}
	public void setIsAlloc(String isAlloc) {
		this.isAlloc = isAlloc;
	}

	public String getIsLogin() {
		return this.isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}

	public String getIsValid() {
		return this.isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "PubUser [userId=" + userId + ", accounts=" + accounts
				+ ", isAlloc=" + isAlloc + ", isLogin=" + isLogin
				+ ", isValid=" + isValid + ", isSys=" + isSys + ", password="
				+ password + ", remark=" + remark + ", username=" + username
				+ ", securityLevel=" + securityLevel + ", departmentId="
				+ departmentId + ", employeeId=" + employeeId
				+ ", departmentName=" + departmentName + ", clearPassword="
				+ clearPassword + "]";
	}

	public Integer getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(Integer securityLevel) {
		this.securityLevel = securityLevel;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getIsSys() {
		return isSys;
	}

	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	public String getAccounts() {
		return accounts;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getClearPassword() {
		return clearPassword;
	}

	public void setClearPassword(String clearPassword) {
		this.clearPassword = clearPassword;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	
}