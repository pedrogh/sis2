/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author pedro
 */
public class Course {

    private Vector _students = new Vector();
    private Integer _courseId = -1;
    private String _courseName = "";
    private String _state = "";

    /**
     * Constructor.
     *
     * @param courseId
     * @param courseName
     * @param State
     */
    public Course(Integer courseId, String courseName, String State) {
        _courseId = courseId;
        _courseName = courseName;
        _state = State;
    }

    /**
     * Add a student to this course.
     *
     * @param student
     */
    public void addStudent(Student student) {
        // Find the student
        Student studentInCourse = getStudentWithId(student.getUserId());
        // The student wasn't there so add it
        if (studentInCourse == null) {
            _students.add(student);
        } else {
            // The student was there.  I assume we can only update the
            // state.  Maybe the course should be checked.
            studentInCourse.setState(student.getState());
        }
    }

    /**
     * Get the student with the given id.
     *
     * @param studentId
     * @return
     */
    protected Student getStudentWithId(Integer studentId) {
        Student studentWithId = null;
        for (int i = 0; i < _students.size(); i++) {
            Student student = (Student) _students.get(i);
            if (student.getUserId() == studentId) {
                studentWithId = student;
            }
        }

        return studentWithId;
    }

    public Vector getStudents() {
        return _students;
    }

    public void setStudents(Vector _students) {
        this._students = _students;
    }

    public Integer getCourseId() {
        return _courseId;
    }

    public void setCourseId(int _courseId) {
        this._courseId = _courseId;
    }

    public String getCourseName() {
        return _courseName;
    }

    public void setCourseName(String _courseName) {
        this._courseName = _courseName;
    }

    public String getState() {
        return _state;
    }

    public void setState(String _state) {
        this._state = _state;
    }

    /**
     * Print all the students enrolled in the given course if the course is
     * active.
     */
    public void printStudents() {
        System.out.println("\tStudents in class:");
        if (this.getState().equalsIgnoreCase("active")) {
            for (int i = 0; i < _students.size(); i++) {
                Student student = (Student) _students.get(i);
                if (student.getState().equalsIgnoreCase("active")) {
                    System.out.println("\t\tStudent: " + student);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(" Couse ID: " + getCourseId() + NEW_LINE);
        result.append(" Course Name: " + getCourseName() + NEW_LINE);
        result.append(" Course State: " + getState() + NEW_LINE);

        return result.toString();
    }
}
