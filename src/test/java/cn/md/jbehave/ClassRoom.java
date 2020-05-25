package cn.md.jbehave;

import java.util.List;

public class ClassRoom {
	List<Student> students;

	public List<Student> getStudents() {
		return students;
	}
	public Student getStudent(String studentName) {
		for (Student student : students) {
			if(student.getName().equals(studentName))return student;
		}
		return null;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
	public void addStudent(Student student) {
		students.add(student);
	}

}
