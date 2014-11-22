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
        _userName = userName;
        _courseId = courseId;
        _state = state;
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

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        if (getState().equalsIgnoreCase("active")) {
            String NEW_LINE = System.getProperty("line.separator");
            result.append(" Student Name: " + getUserName() + NEW_LINE);
        }

        return result.toString();
    }
}
