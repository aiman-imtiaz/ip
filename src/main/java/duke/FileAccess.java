package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileAccess {
    final static String FILE_NAME = "ip/data/duke.txt";

    private static int boolToInt(boolean b) {
        return Boolean.compare(b, false);
    }

    private static void writeToFile(String filePath, String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }

    private static void readFromFile(String filePath) throws FileNotFoundException {
        File f = new File(filePath); // create a File for the given file path
        Scanner s = new Scanner(f); // create a Scanner using the File as the source
        while (s.hasNext()) {
            Task task = lineToTask(s.nextLine());
            Duke.taskList.add(task);
        }
    }

    private static Task lineToTask(String line){
        Task task = null;
        int isDoneInt = 0;
        char indicator = line.charAt(0);
        switch (indicator) {
        case 'D':
            int byIndex = line.indexOf("|", 4);
            String deadlineDescription = line.substring(8, byIndex - 2);
            String by = line.substring(byIndex + 2);
            task = new Deadline(deadlineDescription, by);
            isDoneInt = Integer.parseInt(line.substring(4));
            if (isDoneInt != 0) task.setDone(true);
            break;
        case 'E':
            int atIndex = line.indexOf("|", 4);
            String eventDescription = line.substring(8, atIndex - 2);
            String at = line.substring(atIndex + 2);
            task = new Event(eventDescription, at);
            isDoneInt = Integer.parseInt(line.substring(4));
            if (isDoneInt != 0) task.setDone(true);
            break;
        case 'T':
            String todoDescription = line.substring(8);
            task = new Todo(todoDescription);
            isDoneInt = Integer.parseInt(line.substring(4));
            if (isDoneInt != 0) task.setDone(true);
            break;
        }
        return task;
    }

    private static String taskToLine(Task task) {
        String line = "";
        switch (task.taskKind) {
        case "[D]":
            Deadline deadline = (Deadline) task;
            line = "D" + " | " +
                    boolToInt(deadline.isDone()) +
                    " | " + deadline.description +
                    " | " + deadline.getBy();
            break;
        case "E":
            Event event = (Event) task;
            line = "E" + " | " +
                    boolToInt(event.isDone()) +
                    " | " + event.description +
                    " | " + event.getAt();
            break;
        case "[T]":
            Todo todo = (Todo) task;
            line = "T" + " | " +
                    boolToInt(todo.isDone()) +
                    " | " + todo.description;
            break;
        default:
            // code block
        }
        return line;
    }

    public static void saveToFile() throws IOException {
        String line;
        try {
            for (Task task: Duke.taskList){
                line = taskToLine(task);
                writeToFile(FILE_NAME, line + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public static void loadFromFile(){
        try {
            readFromFile(FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }




}
