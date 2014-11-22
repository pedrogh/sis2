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
