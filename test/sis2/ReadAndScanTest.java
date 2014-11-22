/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pedro
 */
public class ReadAndScanTest {
    IEnrollment enrollment = null;
    ReadAndScan readAndScan = null;
    String coursesFileName = "courses.csv";
    String studentsFileName = "students.csv";
    
    public ReadAndScanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {            
            enrollment = new Enrollment();
            readAndScan = new ReadAndScan(coursesFileName, studentsFileName, enrollment);
        } catch (IOException | InvalidFileTypeException | FailedToParseFileLineException ex) {
            System.out.println(ex.getMessage());
        }     
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetNumberOfCourses() {
        // There should be three courses
        assertEquals(3, enrollment.getNumberOfCourses());
    }
    
    public void testGetNumberOfStudentsForCourseId() {
        coursesFileName = "courses_bad_column_order.csv";
        studentsFileName = "students.csv";
        Integer courseIdWithTwoStudents = new Integer(2);
        int expectedStudents = 2;
        assertEquals(expectedStudents, enrollment.getNumberOfStudents(courseIdWithTwoStudents));
        
        Integer courseIdWithOneStudent = new Integer(4);
        expectedStudents = 1;
        assertEquals(expectedStudents, enrollment.getNumberOfStudents(courseIdWithOneStudent));
    }
    
    @Test
    public void testGetNumberOfCoursesUsingFilesWithColumnOutOfOrder() {
        coursesFileName = "courses_bad_column_order.csv";
        studentsFileName = "students.csv";
        assertEquals(3, enrollment.getNumberOfCourses());
    }
    
    @Test
    public void testGetNumberOfStudentsUsingWithColumnOutOfOrder() {
        coursesFileName = "courses_bad_column_order.csv";
        studentsFileName = "students.csv";
        Integer courseIdWithNoStudents = new Integer(6);
        assertEquals(0, enrollment.getNumberOfStudents(courseIdWithNoStudents));
    }
    
    @Test
    public void testGetCourseNameOfCourseFileWithColumnOutOfOrder() {
        coursesFileName = "courses_bad_column_order.csv";
        studentsFileName = "students.csv";
        String courseNameForID = "Operating Systems";
        Course course = enrollment.getCourse(5);
        assertEquals(courseNameForID, course.getCourseName());
    }
}
