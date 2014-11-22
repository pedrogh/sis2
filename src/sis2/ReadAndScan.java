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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadAndScan {

    // Type of the file.
    private InputFileType _fileType = InputFileType.UNKNOWN;
    private IEnrollment _enrollment = null;

    public ReadAndScan(String coursesFileName, String studentsFileName, IEnrollment enrollment) throws IOException, InvalidFileTypeException, FailedToParseFileLineException {
        
        try {
            _enrollment = enrollment;
            String current = new java.io.File(".").getCanonicalPath();
            System.out.println("Current dir:" + current);
            this.processLineByLine(coursesFileName);
            this.processLineByLine(studentsFileName);
            //_enrollment.printEnrollment();
        } catch (InvalidFileTypeException ex) {
            System.out.println("Invalid file type: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Could not read file");
        }
    }

    /**
     * Process each line in the file.
     *
     * @throws IOException
     * @throws InvalidFileTypeException
     */
    public final void processLineByLine(String fileName) throws IOException, InvalidFileTypeException, FailedToParseFileLineException {
        fFilePath = Paths.get(fileName);
        try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
            // Determine the type of file.  Student or course.
            String headerLine = scanner.nextLine();
            _fileType = DetermineFileType(headerLine);
            if (_fileType == InputFileType.UNKNOWN) {
                throw new InvalidFileTypeException("File is not of type student or course.");
            } else {
                while (scanner.hasNextLine()) {
                    if (_fileType == InputFileType.STUDENT) {
                        processStudentLine(scanner.nextLine());
                    } else if (_fileType == InputFileType.COURSE) {
                        processCourseLine(scanner.nextLine());
                    }
                }
            }
        }
    }

    /**
     * Find out what type of file we have.
     *
     * @param aLine
     * @return
     */
    protected InputFileType DetermineFileType(String aLine) {
        InputFileType fileType = InputFileType.UNKNOWN;
        Scanner scanner = new Scanner(aLine);

        scanner.useDelimiter(",");
        if (scanner.hasNext()) {
            // Read first three columns.
            String firstColumn = scanner.next().trim();
            String secondColumn = scanner.next().trim();
            String thirdColumn = scanner.next().trim();
            // Don't know if we need to read a fourth column yet.
            String fourthColumn = "";

            if (firstColumn.trim().equalsIgnoreCase("course_id") && secondColumn.trim().equalsIgnoreCase("course_name")
                    && thirdColumn.trim().equalsIgnoreCase("state")) {
                fileType = InputFileType.COURSE;
            } else if (firstColumn.trim().equalsIgnoreCase("user_id") && secondColumn.trim().equalsIgnoreCase("user_name")
                    && thirdColumn.trim().equalsIgnoreCase("course_id")) {
                // Check to see if it might be of student file type.
                // If so then there should be a fourth column.  Check just to make sure.
                fourthColumn = scanner.next().trim();
                if (fourthColumn.trim().equalsIgnoreCase("state")) {
                    fileType = InputFileType.STUDENT;
                }
            }
        }

        return fileType;
    }

    /**
     * Get course info from the line.
     *
     * @param aLine
     */
    protected void processCourseLine(String aLine) throws FailedToParseFileLineException {
        //use a second Scanner to parse the content of each line 
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter(",");
        if (scanner.hasNext()) {
            //assumes the line has a certain structure
            // course_id, course_name, state.
            int courseID = -1;
            String courseName = "";
            String state = "";

            try {
                String strCourseId = scanner.next().trim();
                courseID = Integer.parseInt(strCourseId);
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find course_id");
                throw new FailedToParseFileLineException("Could not find course_id.");
            } catch (NumberFormatException ex) {
                log("Invalid course_id");
                throw new FailedToParseFileLineException("Failed to parse course_id.");
            }

            try {
                courseName = scanner.next().trim().replace("\"", "");;
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find course_name.");
                throw new FailedToParseFileLineException("Could not find course_name.");
            }

            try {
                state = scanner.next().trim().replace("\"", "");
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find state.");
                throw new FailedToParseFileLineException("Could not find state.");
            }

            LogCourseLine(courseID, courseName, state);
            Course course = new Course(new Integer(courseID), courseName, state);
            _enrollment.addCourse(course);

        } else {
            log("Empty or invalid line. Unable to process.");
        }
    }

    /**
     * Get student info from the line.
     *
     * @param aLine
     */
    protected void processStudentLine(String aLine) throws FailedToParseFileLineException {
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter(",");
        if (scanner.hasNext()) {
            //assumes the line has a certain structure
            // user_id, user_name, course_id, state
            int userID = -1;
            String userName = "";
            int courseID = -1;
            String state = "";

            try {
                String strStudentId = scanner.next().trim();
                userID = Integer.parseInt(strStudentId);
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find user_id");
                throw new FailedToParseFileLineException("Could not find user_id.");
            } catch (NumberFormatException ex) {
                log("Invalid user_id");
                throw new FailedToParseFileLineException("Failed to parse student_id.");
            }

            try {
                userName = scanner.next().trim().replace("\"", "");
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find user_name.");
                throw new FailedToParseFileLineException("Could not find user_name.");
            }

            try {
                String strCourseId = scanner.next().trim();
                courseID = Integer.parseInt(strCourseId);
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find course_id");
                throw new FailedToParseFileLineException("Could not find course_id.");
            } catch (NumberFormatException ex) {
                log("Invalid course_id");
                throw new FailedToParseFileLineException("Failed to parse course_id.");
            }

            try {
                state = scanner.next().trim().replace("\"", "");
            } catch (java.util.NoSuchElementException ex) {
                log("Could not find state.");
                throw new FailedToParseFileLineException("Could not find state.");
            }

            LogStudentLine(userID, userName, courseID, state);
            
            Student student = new Student(new Integer(userID), userName, new Integer(courseID), state);
            _enrollment.addStudentToCourse(student);

        } else {
            log("Empty or invalid line. Unable to process.");
        }
    }

    private void LogStudentLine(int userId, String userName, int courseId,
            String state) {
        log("Student ID: " + quote(Integer.toString(userId)) + ", Name : " + quote(userName)
                + ", courseID : " + quote(Integer.toString(courseId))
                + ", state : " + quote(state));
    }

    private void LogCourseLine(int courseId, String courseName, String state) {
        log("course ID: " + quote(Integer.toString(courseId)) + ", Name : " + quote(courseName)
                + ", state : " + quote(state));
    }

    // PRIVATE 
    private Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private static void log(Object aObject) {
        System.out.println("log: " + String.valueOf(aObject));
    }

    private String quote(String aText) {
        String QUOTE = "'";
        return QUOTE + aText + QUOTE;
    }
}
