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
     * Add a student to a course. As per the requirement 'a Student can only be
     * enrolled in one course'. so:
     *
     * We look up the student in the courses If we don't find the student we
     * enroll in the course regardless of the state. Students with 'delete'
     * state won't print anyway. Aware that this will make the program to use
     * more memory. Plus the requirement reads 'The user_id and course_id are
     * globally unique. As a result, if the id does not exist, create a new
     * record, otherwise, update the existing record'. That also implies there
     * is no need to remove a student from a course when the course changes but
     * we can change the state in the current course.
     *
     * If we find the student we: Check if the student is enrolled in the
     * current course, if so, we change the state of the new data. We don't
     * check if the state is different because it will either be 'active' or
     * 'deleted'. Checking to change the state would make sense if we had more
     * than two states. The logic for changing states might become more complex
     * the more states there are.
     *
     * If the student is enrolled but the course is different then we also
     * change course and state.
     *
     * @param student
     */
    @Override
    public void addStudentToCourse(Student student) {
        //_courses.put(courseId, course);
        Course courseToEnroll = _courses.get(student.getCourseId());

        if (courseToEnroll == null) {
            System.out.println("Student " + student.getUserName() + " with id "
                    + student.getUserId() + " is using an invalid course id: "
                    + student.getCourseId());
        } else {
            // The course exists.  Add the student to the course.
            if (courseToEnroll.addStudent(student)) {
                // The student was added so now check that it was not
                // enrolled in any other courses and if so change the state
                // in that other course.
                for (SortedMap.Entry<Integer, Course> entry : _courses.entrySet()) {
                    Course courseToCheck = entry.getValue();
                    // Don't check if this course ID was the same as the
                    // course id to which the student was just recenly added
                    // because the student was added because it was not in
                    // that course.
                    if (courseToEnroll.getCourseId() != courseToCheck.getCourseId()) {
                        Student studentInOtherCourse = courseToCheck.getStudent(student);
                        
                        // If the student existed in another course change its
                        // state
                        if (studentInOtherCourse != null) {
                            studentInOtherCourse.setState("inactive");
                        }
                    }
                }

            }
        }
    }

    /**
     * Output a list of active courses, and for each course, a list of active
     * students enrolled in that course.
     */
    @Override
    public void printEnrollment() {
        System.out.println("Enrollment");
        for (SortedMap.Entry<Integer, Course> entry : _courses.entrySet()) {
            Course course = entry.getValue();
            // Print the students in this course whose state is not deleted.
            // Logic of whether the course prints and whether it prints 
            course.printStudents();
        }
    }

    /**
     * Get the number of courses.
     *
     * @return
     */
    @Override
    public int getNumberOfCourses() {
        return _courses.size();
    }

    /**
     * Get the number of students for a given course. No student should show in
     * more than one course as per the requirement.
     *
     * @param courseId
     * @return
     */
    @Override
    public int getNumberOfStudents(Integer courseId) {
        Course course = _courses.get(courseId);
        if (course != null) {
            return course.getStudents().size();
        } else {
            return 0;
        }
    }
    
    /**
     * Get the course given the course id.
     * @param courseId
     * @return 
     */
    @Override
    public Course getCourse(Integer courseId)
    {
        Course course = _courses.get(courseId);
        return course;
    }
}
