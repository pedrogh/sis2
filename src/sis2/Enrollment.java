/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author pedro
 */
public class Enrollment implements IEnrollment {

    private SortedMap<Integer, Course> _courses = new TreeMap<Integer, Course>();

    /**
     * Constructor
     */
    public Enrollment() {

    }

    /**
     * Add a course.
     *
     * @param course
     */
    @Override
    public void addCourse(Course aCourse) {
        // If the value does not exist then add it.
        Course course = _courses.get(aCourse.getCourseId());
        if (course == null) {
            _courses.put(aCourse.getCourseId(), aCourse);
        } else { // Retrieve it and update it
            // Assuming the only field that can change is active.
            course.setState(aCourse.getState());
        }
    }

    /**
     * Add a student to a course.
     * As per the requirement 'a Student can only be enrolled in one course'
     * so:
     * We look up the student in the courses
     * If we don't find the student we enroll in the course.
     * If we find the student we:
     *   Check if the student is enrolled in the current course, if so,
     *   we check the state of the new data.  We don't check because it will either
     *   be 'active' or 'deleted'.  Checking to change the state would make more
     *   sense if we had more than two states.
     * 
     *   If the student
     * 
     * change the state for the current course to
     *  'deleted'
     * 
     * @param student
     */
    @Override
    public void addStudentToCourse(Student student) {
        //_courses.put(courseId, course);
        Course course = _courses.get(student.getCourseId()); 
        
        if ( course == null || course.getState().equalsIgnoreCase("deleted") ){
            System.out.println("Student " + student.getUserName() + " cannot be enrolled in course with id " + student.getCourseId());
        } else {
            // The course exists and it is not deleted.
            course.addStudent(student);
        }
    }

    @Override
    public void printEnrollment() {
        System.out.println("Enrollment");
        for (SortedMap.Entry<Integer, Course> entry : _courses.entrySet()) {
            Integer key = entry.getKey();
            Course  value = entry.getValue();
            if (value.getState().equalsIgnoreCase("active")) {
                System.out.println(entry.getValue().toString());
                
                // Print the students in this course whose state is not deleted.
                value.printStudents();
            }
        }
    }
    
    @Override
    public int getNumberOfCourses() {
        return _courses.size();
    }
    
    @Override
    public int getNumberOfStudents(Integer courseId)
    {
        Course course = _courses.get(courseId);
        if (course != null) {
            return course.getStudents().size();
        } else {
            return 0;
        }
    }
}
