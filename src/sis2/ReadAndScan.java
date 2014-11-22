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
import com.sun.xml.internal.ws.util.StringUtils;
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
    // The column names we expect to see in the student file.
    private final ArrayList<String> _validStudentFileColumnNames = new ArrayList<>(Arrays.asList("user_id", "user_name", "course_id", "state"));
    // The column names we expect to see in the course file.
    private final ArrayList<String> _validCourseFileColumnNames = new ArrayList<>(Arrays.asList("course_id", "course_name", "state"));
    // Will hold the order in which the columns are coming in.
    private ArrayList<String> _courseFileColumnNamesOrder = new ArrayList<>();
    private ArrayList<String> _studentFileColumnNamesOrder = new ArrayList<>();
    // The number of columns we expect in each of the file types.
    private final int COURSE_FILE_COLUMN_COUNT = 3;
    private final int STUDENT_FILE_COLUMN_COUNT = 4;
    private boolean _processingError = false;

    /**
     * Constructor.
     *
     * @param coursesFileName
     * @param studentsFileName
     * @param enrollment
     * @throws IOException
     * @throws InvalidFileTypeException
     * @throws FailedToParseFileLineException
     */
    public ReadAndScan(String coursesFileName, String studentsFileName, IEnrollment enrollment)
            throws IOException, InvalidFileTypeException, FailedToParseFileLineException {

        try {
            _enrollment = enrollment;
            String current = new java.io.File(".").getCanonicalPath();
            System.out.println("Current dir:" + current);
            this.processLineByLine(coursesFileName);
            this.processLineByLine(studentsFileName);
            
            if (_processingError) {
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("   There were errors while processing the data files.");
                System.out.println("   Some student, class or enrollment information might be incorrect.");
                System.out.println("-----------------------------------------------------------------------");
            }
        } catch (IOException ex) {
            System.out.println("Could not read file");
        }
    }

    /**
     * Process each line in the file.
     *
     * @param fileName
     * @throws IOException
     * @throws InvalidFileTypeException
     * @throws sis2.FailedToParseFileLineException
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

            // We don't care in what order the columns came in.  As long as
            // all the columns exits.
            // Check to see if we might have a course file.
            if (_validCourseFileColumnNames.contains(firstColumn.trim().toLowerCase())
                    && _validCourseFileColumnNames.contains(secondColumn.trim().toLowerCase())
                    && _validCourseFileColumnNames.contains(thirdColumn.trim().toLowerCase())) {

                fileType = InputFileType.COURSE;

                // Store the order in which the columns existed in the file.
                _courseFileColumnNamesOrder.add(firstColumn.trim().toLowerCase());
                _courseFileColumnNamesOrder.add(secondColumn.trim().toLowerCase());
                _courseFileColumnNamesOrder.add(thirdColumn.trim().toLowerCase());

            } // Check to see if it might be of student file type.
            else if (_validStudentFileColumnNames.contains(firstColumn.trim().toLowerCase())
                    && _validStudentFileColumnNames.contains(secondColumn.trim().toLowerCase())
                    && _validStudentFileColumnNames.contains(thirdColumn.trim().toLowerCase())) {

                // If so then there should be a fourth column.  
                // Check to make sure.
                fourthColumn = scanner.next().trim();
                if (_validStudentFileColumnNames.contains(fourthColumn.trim().toLowerCase())) {

                    fileType = InputFileType.STUDENT;

                    // Store the order in which the columns existed in the file.
                    _studentFileColumnNamesOrder.add(firstColumn.trim().toLowerCase());
                    _studentFileColumnNamesOrder.add(secondColumn.trim().toLowerCase());
                    _studentFileColumnNamesOrder.add(thirdColumn.trim().toLowerCase());
                    _studentFileColumnNamesOrder.add(fourthColumn.trim().toLowerCase());
                }
            }
        }

        return fileType;
    }

    /**
     * Get course info from the line. Since the columns were already checked,
     * and they all exist, here we use the order in which they came in and it
     * was stored in courseFileColumnNamesOrder.
     *
     * We obtain the column value we need by using the index stored in
     * courseFileColumnNamesOrder.
     *
     * @param aLine
     * @throws sis2.FailedToParseFileLineException
     */
    protected void processCourseLine(String aLine) throws FailedToParseFileLineException {

        String[] parts = aLine.trim().split(",");
        try {
            if (parts.length <= STUDENT_FILE_COLUMN_COUNT) {
                Integer courseID = Integer.parseInt(parts[_courseFileColumnNamesOrder.indexOf("course_id")].trim());
                String courseName = parts[_courseFileColumnNamesOrder.indexOf("course_name")].trim().replace("\"", "");
                String courseState = parts[_courseFileColumnNamesOrder.indexOf("state")].trim().replace("\"", "");

                LogCourseLine(courseID, courseName, courseState);
                Course course = new Course(courseID, courseName, courseState);
                _enrollment.addCourse(course);

            } else {
                // Try to prevent major error.
                log(" * Line is missing columns in course file. " + Arrays.toString(parts));
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException | java.lang.NumberFormatException ex) {
            // Handle major error.
            _processingError = true;
            log("** Unable to process line in course file. " + Arrays.toString(parts));
        }
    }

    /**
     * Get student info from the line. Since the columns were already checked,
     * and they all exist, here we use the order in which they came in and it
     * was stored in courseFileColumnNamesOrder.
     *
     * We obtain the column value we need by using the index stored in
     * studentFileColumnNamesOrder.
     *
     * @param aLine
     * @throws sis2.FailedToParseFileLineException
     */
    protected void processStudentLine(String aLine) throws FailedToParseFileLineException {
        String[] parts = aLine.trim().split(",");

        try {
            if (parts.length <= STUDENT_FILE_COLUMN_COUNT) {
                Integer courseID = Integer.parseInt(parts[_studentFileColumnNamesOrder.indexOf("course_id")].trim());
                Integer userID = Integer.parseInt(parts[_studentFileColumnNamesOrder.indexOf("user_id")].trim());
                String userName = parts[_studentFileColumnNamesOrder.indexOf("user_name")].trim().replace("\"", "");
                String studentState = parts[_studentFileColumnNamesOrder.indexOf("state")].trim().replace("\"", "");

                LogStudentLine(userID, userName, courseID, studentState);
                Student student = new Student(userID, userName, courseID, studentState);
                _enrollment.addStudentToCourse(student);

            } else {
                log(" * Line is missing columns in student file. " + Arrays.toString(parts));
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException | java.lang.NumberFormatException ex) {
            // Handle major error.
            _processingError = true;
            log("** Unable to process line in student file. " + Arrays.toString(parts));            
        }
    }

    /**
     * Helper method to examine what is going on as we process files.
     *
     * @param userId
     * @param userName
     * @param courseId
     * @param state
     */
    private void LogStudentLine(int userId, String userName, int courseId,
            String state) {
        log("Student ID: " + quote(Integer.toString(userId)) + ", Name : " + quote(userName)
                + ", courseID : " + quote(Integer.toString(courseId))
                + ", state : " + quote(state));
    }

    /**
     * Helper method to examine what is going on as we process files.
     *
     * @param courseId
     * @param courseName
     * @param state
     */
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
