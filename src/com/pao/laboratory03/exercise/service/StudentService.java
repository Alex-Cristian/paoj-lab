package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static StudentService instance;

    private final List<Student> students;

    private StudentService() {
        this.students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' exista deja.");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost gasit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti inregistrati.");
            return;
        }

        int index = 1;
        for (Student student : students) {
            System.out.println(index++ + ". " + student);
            for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                System.out.printf("   %s = %.2f%n", entry.getKey().name(), entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti inregistrati.");
            return;
        }

        List<Student> sortedStudents = new ArrayList<>(students);
        sortedStudents.sort(Comparator.comparingDouble(Student::getAverage).reversed()
                .thenComparing(Student::getName));

        System.out.println("=== Top studenti ===");
        int index = 1;
        for (Student student : sortedStudents) {
            System.out.printf("%d. %s - media: %.2f%n", index++, student.getName(), student.getAverage());
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> averages = new HashMap<>();

        for (Subject subject : Subject.values()) {
            double sum = 0;
            int count = 0;

            for (Student student : students) {
                Double grade = student.getGrades().get(subject);
                if (grade != null) {
                    sum += grade;
                    count++;
                }
            }

            if (count > 0) {
                averages.put(subject, sum / count);
            }
        }

        return averages;
    }
}
