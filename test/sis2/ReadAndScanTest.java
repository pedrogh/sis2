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
    
    @Test
    public void testGetNumberOfCoursesUsingIncorrectFiles() {
        coursesFileName = "courses_bad_column_order.csv";
        studentsFileName = "students.csv";
        assertEquals(3, enrollment.getNumberOfCourses());
    }
    
    @Test
    public void testGetNumberOfStudentsUsingIncorrectFiles() {
        coursesFileName = "courses_bad_column_order.csv";
        studentsFileName = "students.csv";
        Integer courseIdWithNoStudents = new Integer(6);
        assertEquals(0, enrollment.getNumberOfStudents(courseIdWithNoStudents));
    }

    /**
     * Test of processLineByLine method, of class ReadAndScan.
     */
    @Test
    public void testProcessLineByLine() throws Exception {
        System.out.println("processLineByLine");
        String fileName = "";
        ReadAndScan instance = null;
        instance.processLineByLine(fileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of DetermineFileType method, of class ReadAndScan.
     */
    @Test
    public void testDetermineFileType() {
        System.out.println("DetermineFileType");
        String aLine = "";
        ReadAndScan instance = null;
        InputFileType expResult = null;
        InputFileType result = instance.DetermineFileType(aLine);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processCourseLine method, of class ReadAndScan.
     */
    @Test
    public void testProcessCourseLine() throws Exception {
        System.out.println("processCourseLine");
        String aLine = "";
        ReadAndScan instance = null;
        instance.processCourseLine(aLine);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processStudentLine method, of class ReadAndScan.
     */
    @Test
    public void testProcessStudentLine() throws Exception {
        System.out.println("processStudentLine");
        String aLine = "";
        ReadAndScan instance = null;
        instance.processStudentLine(aLine);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
