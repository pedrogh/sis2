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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadAndScan {

    // Type of the file.
    private InputFileType _fileType = InputFileType.UNKNOWN;
    private IEnrollment _enrollment = null;
    private final ArrayList<String> validStudentFileColumnNames = new ArrayList<>(Arrays.asList("user_id", "user_name", "course_id", "state"));
    private final ArrayList<String> validCourseFileColumnNames = new ArrayList<>(Arrays.asList("course_id", "course_name", "state"));
    private ArrayList<String> courseFileColumnNamesOrder = new ArrayList<>();
    private ArrayList<String> studentFileColumnNamesOrder = new ArrayList<>();

    public ReadAndScan(String coursesFileName, String studentsFileName, IEnrollment enrollment) throws IOException, InvalidFileTypeException, FailedToParseFileLineException {

        try {
            _enrollment = enrollment;
            String current = new java.io.File(".").getCanonicalPath();
            System.out.println("Current dir:" + current);
            this.processLineByLine(coursesFileName);
            this.processLineByLine(studentsFileName);
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
                // Now we check input file type only once.
                if (_fileType == InputFileType.STUDENT) {
                    processStudentsFile(scanner);
                } else if (_fileType == InputFileType.COURSE) {
                    processCoursesFile(scanner);
                }
            }
        }
    }

    /**
     * Parse the rest of the students csv file.
     *
     * @param scanner
     * @throws FailedToParseFileLineException
     */
    protected void processStudentsFile(Scanner scanner) throws FailedToParseFileLineException {
        while (scanner.hasNextLine()) {
            processStudentLine(scanner.nextLine());
        }
    }

    /**
     * Parse the rest of the courses csv file.
     *
     * @param scanner
     * @throws FailedToParseFileLineException
     */
    protected void processCoursesFile(Scanner scanner) throws FailedToParseFileLineException {
        while (scanner.hasNextLine()) {
            processCourseLine(scanner.nextLine());
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

            // We don't care in what order the columns came in.
            if (validCourseFileColumnNames.contains(firstColumn.trim().toLowerCase())
                    && validCourseFileColumnNames.contains(secondColumn.trim().toLowerCase())
                    && validCourseFileColumnNames.contains(thirdColumn.trim().toLowerCase())) {

                fileType = InputFileType.COURSE;

                // Store the order in which the columns existed in the file.
                courseFileColumnNamesOrder.add(firstColumn.trim().toLowerCase());
                courseFileColumnNamesOrder.add(secondColumn.trim().toLowerCase());
                courseFileColumnNamesOrder.add(thirdColumn.trim().toLowerCase());

            } else if (validStudentFileColumnNames.contains(firstColumn.trim().toLowerCase())
                    && validStudentFileColumnNames.contains(secondColumn.trim().toLowerCase())
                    && validStudentFileColumnNames.contains(thirdColumn.trim().toLowerCase())) {

                // Check to see if it might be of student file type.
                // If so then there should be a fourth column.  Check just to make sure.
                fourthColumn = scanner.next().trim();
                if (validStudentFileColumnNames.contains(fourthColumn.trim().toLowerCase())) {

                    fileType = InputFileType.STUDENT;

                    // Store the order in which the columns existed in the file.
                    studentFileColumnNamesOrder.add(firstColumn.trim().toLowerCase());
                    studentFileColumnNamesOrder.add(secondColumn.trim().toLowerCase());
                    studentFileColumnNamesOrder.add(thirdColumn.trim().toLowerCase());
                    studentFileColumnNamesOrder.add(fourthColumn.trim().toLowerCase());
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
            Integer courseID = -1;
            String courseName = "";
            String state = "";

            // Parse the first column
            if (courseFileColumnNamesOrder.get(0).compareToIgnoreCase("course_name") == 0) {
                // Parse string for course name.
                courseName = parseString(scanner);

            } else if (courseFileColumnNamesOrder.get(0).compareToIgnoreCase("course_id") == 0) {
                // Parse Integer for course id.
                courseID = parseInteger(scanner);

            } else if (courseFileColumnNamesOrder.get(0).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            }

            // Parse the second column
            if (courseFileColumnNamesOrder.get(1).compareToIgnoreCase("course_name") == 0) {
                // Parse string for course name.
                courseName = parseString(scanner);
            } else if (courseFileColumnNamesOrder.get(1).compareToIgnoreCase("course_id") == 0) {
                // Parse Integer for course id.
                courseID = parseInteger(scanner);

            } else if (courseFileColumnNamesOrder.get(1).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            }

            // Parse the third column
            if (courseFileColumnNamesOrder.get(2).compareToIgnoreCase("course_name") == 0) {
                // Parse string for course name.
                courseName = parseString(scanner);
            } else if (courseFileColumnNamesOrder.get(2).compareToIgnoreCase("course_id") == 0) {
                // Parse Integer for course id.
                courseID = parseInteger(scanner);

            } else if (courseFileColumnNamesOrder.get(2).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            }

            LogCourseLine(courseID, courseName, state);
            Course course = new Course(courseID, courseName, state);
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
            Integer userID = -1;
            String userName = "";
            Integer courseID = -1;
            String state = "";
            
            // Parse the first column
            if (studentFileColumnNamesOrder.get(0).compareToIgnoreCase("user_name") == 0) {
                // Parse string for user name.
                userName = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(0).compareToIgnoreCase("user_id") == 0) {
                // Parse Integer for user id.
                userID = parseInteger(scanner);
            } else if (studentFileColumnNamesOrder.get(0).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(0).compareToIgnoreCase("course_id") == 0) {
                // Parse string for course id.
                courseID = parseInteger(scanner);
            }
            
            // Parse the second column
            if (studentFileColumnNamesOrder.get(1).compareToIgnoreCase("user_name") == 0) {
                // Parse string for user name.
                userName = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(1).compareToIgnoreCase("user_id") == 0) {
                // Parse Integer for user id.
                userID = parseInteger(scanner);
            } else if (studentFileColumnNamesOrder.get(1).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(1).compareToIgnoreCase("course_id") == 0) {
                // Parse string for course id.
                courseID = parseInteger(scanner);
            }
            
            // Parse the third column
            if (studentFileColumnNamesOrder.get(2).compareToIgnoreCase("user_name") == 0) {
                // Parse string for user name.
                userName = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(2).compareToIgnoreCase("user_id") == 0) {
                // Parse Integer for user id.
                userID = parseInteger(scanner);
            } else if (studentFileColumnNamesOrder.get(2).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(2).compareToIgnoreCase("course_id") == 0) {
                // Parse string for course id.
                courseID = parseInteger(scanner);
            }
            
            // Parse the fourth column
            if (studentFileColumnNamesOrder.get(3).compareToIgnoreCase("user_name") == 0) {
                // Parse string for user name.
                userName = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(3).compareToIgnoreCase("user_id") == 0) {
                // Parse Integer for user id.
                userID = parseInteger(scanner);
            } else if (studentFileColumnNamesOrder.get(3).compareToIgnoreCase("state") == 0) {
                // Parse string for state.
                state = parseString(scanner);
            } else if (studentFileColumnNamesOrder.get(3).compareToIgnoreCase("course_id") == 0) {
                // Parse string for course id.
                courseID = parseInteger(scanner);
            }

            LogStudentLine(userID, userName, courseID, state);

            Student student = new Student(new Integer(userID), userName, new Integer(courseID), state);
            _enrollment.addStudentToCourse(student);

        } else {
            log("Empty or invalid line. Unable to process.");
        }
    }
    
        /**
     * Helper method to get a Integer from a string.
     *
     * @param scanner
     * @return
     * @throws FailedToParseFileLineException
     */
    private Integer parseInteger(Scanner scanner) throws FailedToParseFileLineException {
        Integer integerValue = null;
        try {
            String strCourseId = scanner.next().trim();
            integerValue = Integer.parseInt(strCourseId);
        } catch (java.util.NoSuchElementException ex) {
            log("Could not parse integer column");
            throw new FailedToParseFileLineException("Could not find integer column.");
        } catch (NumberFormatException ex) {
            log("Invalid integer value");
            throw new FailedToParseFileLineException("Invalid integer value");
        }

        return integerValue;
    }

    /**
     * Helper method to get a string variable.
     *
     * @param scanner
     * @return
     * @throws FailedToParseFileLineException
     */
    private String parseString(Scanner scanner) throws FailedToParseFileLineException {
        String stringValue = null;
        try {
            stringValue = scanner.next().trim().replace("\"", "");
        } catch (java.util.NoSuchElementException ex) {
            log("Could not parse string column");
            throw new FailedToParseFileLineException("Could not parse string column");
        }
        return stringValue;
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
