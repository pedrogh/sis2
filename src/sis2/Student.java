/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

/**
 *
 * @author pedro
 */
public class Student {

    private Integer _userId = -1;
    private String _userName = "";
    private Integer _courseId = -1;
    private String _state = "";

    public Student(Integer userId, String userName, Integer courseId, String state) {
        _userId = userId;
        if (!userName.isEmpty()) {
            _userName = userName;
        } else {
            _userName = "No name";
        }
        
        _courseId = courseId;
        
        if (!state.isEmpty()) {
            _state = state;
        } else {
            _state = "No state";
        }
    }

    public Integer getUserId() {
        return _userId;
    }

    public void setUserId(int userId) {
        this._userId = userId;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        this._userName = userName;
    }

    public Integer getCourseId() {
        return _courseId;
    }

    public void setCourseId(int courseId) {
        this._courseId = courseId;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        this._state = state;
    }

    public void print() {
        if (getState().equalsIgnoreCase("active")) {
            System.out.print("\t" + this.toString());
        }
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        String NEW_LINE = System.getProperty("line.separator");
        result.append(" Student Name: ").append(getUserName()).append(NEW_LINE);

        return result.toString();
    }
}
