/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.io.IOException;

/**
 *
 * @author pedro
 */
public class Sis2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Needs two arguments: \"name_of_courses_file\", \"name_of_students_file\"");
        } else {
            try {
                IEnrollment enrollment = new Enrollment();
                String coursesFileName = args[0];
                String studentsFileName = args[1];
                ReadAndScan readAndScan = new ReadAndScan(coursesFileName, studentsFileName, enrollment);
                enrollment.printEnrollment();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (InvalidFileTypeException ex) {
                System.out.println(ex.getMessage());
            } catch (FailedToParseFileLineException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
}
