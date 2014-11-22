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
    IEnrollment _enrollment = null;
    ReadAndScan _readAndScan = null;
    String _coursesFileName = "courses.csv";
    String _studentsFileName = "students.csv";
    
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
            _enrollment = new Enrollment();
            _readAndScan = new ReadAndScan(_coursesFileName, _studentsFileName, _enrollment);
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
        assertEquals(3, _enrollment.getNumberOfCourses());
    }
    
    public void testGetNumberOfStudentsForCourseId() {
        _coursesFileName = "courses_bad_column_order.csv";
        _studentsFileName = "students.csv";
        Integer courseIdWithTwoStudents = new Integer(2);
        int expectedStudents = 2;
        assertEquals(expectedStudents, _enrollment.getNumberOfStudents(courseIdWithTwoStudents));
        
        Integer courseIdWithOneStudent = new Integer(4);
        expectedStudents = 1;
        assertEquals(expectedStudents, _enrollment.getNumberOfStudents(courseIdWithOneStudent));
    }
    
    @Test
    public void testGetNumberOfCoursesUsingFilesWithColumnOutOfOrder() {
        _coursesFileName = "courses_bad_column_order.csv";
        _studentsFileName = "students.csv";
        assertEquals(3, _enrollment.getNumberOfCourses());
    }
    
    @Test
    public void testGetNumberOfStudentsUsingWithColumnOutOfOrder() {
        _coursesFileName = "courses_bad_column_order.csv";
        _studentsFileName = "students.csv";
        Integer courseIdWithNoStudents = 6;
        assertEquals(0, _enrollment.getNumberOfStudents(courseIdWithNoStudents));
    }
    
    @Test
    public void testGetCourseNameOfCourseFileWithColumnOutOfOrder() {
        _coursesFileName = "courses_bad_column_order.csv";
        _studentsFileName = "students.csv";
        String courseNameForID = "Operating Systems";
        Course course = _enrollment.getCourse(5);
        assertEquals(courseNameForID, course.getCourseName());
    }
}
