/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author pedro
 */
public class Course {

    private ArrayList<Student> _students;
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
    public Course(Integer courseId, String courseName, String state) {
        this._students = new ArrayList<>();
        
        _courseId = courseId;
        if (!courseName.isEmpty()) {
            _courseName = courseName;
        } else {
            _courseName = "No name";
        }

        if (!state.isEmpty()) {
            _state = state;
        } else {
            _state = "No state";
        }
    }

    /**
     * Add a student to this course.
     * Returns 
     *
     * @param student
     * @return 
     */
    public boolean addStudent(Student student) {
        boolean bStudentWasAdded = false;
        // Find the student
        Student studentInCourse = getStudent(student.getUserId());
        // The student wasn't there so add it
        if (studentInCourse == null) {
            _students.add(student);
            bStudentWasAdded = true;
        } else {
            // The student was there.  I assume we can only update the
            // state.  Maybe the course should be checked.
            studentInCourse.setState(student.getState());
        }
        
        return bStudentWasAdded;
    }

    /**
     * Overloaded method so we can also pass a Student.
     * @param student
     * @return 
     */
    public Student getStudent(Student student) {
        return getStudent(student.getUserId());
    }
    
    /**
     * Get the student with the given id.
     *
     * @param studentId
     * @return
     */
    public Student getStudent(Integer studentId) {
        Student studentWithId = null;
        for (Object _student : _students) {
            Student student = (Student) _student;
            if (Objects.equals(student.getUserId(), studentId)) {
                studentWithId = student;
            }
        }

        return studentWithId;
    }

    public ArrayList<Student> getStudents() {
        return _students;
    }

    public void setStudents(ArrayList<Student> _students) {
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
     * Output if the course is active, and for each course, a list of active
     * students enrolled in that course.
     */
    public void printStudents() {
        if (this.getState().equalsIgnoreCase("active")) {
            // Print the course name first.
            System.out.print(this.toString());
            System.out.println("\tStudents in class:");
            for (Student _student : _students) {
                Student student = (Student) _student;
                student.print();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        
        System.out.println();
        result.append(" Course ID: ").append(getCourseId()).append(NEW_LINE);
        result.append(" Course Name: \"").append(getCourseName()).append("\"").append(NEW_LINE);
        result.append(" Course State: ").append(getState()).append(NEW_LINE);

        return result.toString();
    }
}
