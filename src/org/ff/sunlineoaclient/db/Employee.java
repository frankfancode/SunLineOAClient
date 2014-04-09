package org.ff.sunlineoaclient.db;

public class Employee {
    private String employeeName;
    private String employeePhoneNo;

    public Employee() {}
    public Employee(String _employeeName, String _employeePhoneNo) {
        employeeName = _employeeName;
        employeePhoneNo = _employeePhoneNo;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the employeePhoneNo
     */
    public String getEmployeePhoneNo() {
        return employeePhoneNo;
    }

    /**
     * @param employeePhoneNo the employeePhoneNo to set
     */
    public void setEmployeePhoneNo(String employeePhoneNo) {
        this.employeePhoneNo = employeePhoneNo;
    }

}
