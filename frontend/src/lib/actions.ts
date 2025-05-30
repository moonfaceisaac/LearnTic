// action.ts

type CurrentState = { success: boolean; error: boolean };

interface SubjectSchema {
  id?: string;
  name: string;
  code: string;
}

interface ClassSchema {
  id?: string;
  name: string;
  subjectId: string;
}

interface TeacherSchema {
  id?: string;
  name: string;
  email: string;
}

interface StudentSchema {
  id?: string;
  name: string;
  email: string;
  classId: string;
}

interface ExamSchema {
  id?: number;       // id bisa optional saat create
  title: string;
  startTime: string; // ISO string datetime, contoh: "2025-05-31T10:00:00Z"
  endTime: string;
  lessonId: number;
}

const BASE_URL = "http://localhost:8080/api";

export const createSubject = async (
  currentState: CurrentState,
  data: SubjectSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/subjects`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to create subject");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const updateSubject = async (
  currentState: CurrentState,
  data: SubjectSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/subjects/${data.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to update subject");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const deleteSubject = async (
  currentState: CurrentState,
  data: FormData
) => {
  const id = data.get("id") as string;
  try {
    const res = await fetch(`${BASE_URL}/subjects/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) throw new Error("Failed to delete subject");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const createClass = async (
  currentState: CurrentState,
  data: ClassSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/classes`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to create class");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const updateClass = async (
  currentState: CurrentState,
  data: ClassSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/classes/${data.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to update class");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const deleteClass = async (
  currentState: CurrentState,
  data: FormData
) => {
  const id = data.get("id") as string;
  try {
    const res = await fetch(`${BASE_URL}/classes/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) throw new Error("Failed to delete class");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const createTeacher = async (
  currentState: CurrentState,
  data: TeacherSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/teachers`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to create teacher");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const updateTeacher = async (
  currentState: CurrentState,
  data: TeacherSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/teachers/${data.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to update teacher");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const deleteTeacher = async (
  currentState: CurrentState,
  data: FormData
) => {
  const id = data.get("id") as string;
  try {
    const res = await fetch(`${BASE_URL}/teachers/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) throw new Error("Failed to delete teacher");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const createStudent = async (
  currentState: CurrentState,
  data: StudentSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/students`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to create student");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const updateStudent = async (
  currentState: CurrentState,
  data: StudentSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/students/${data.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Failed to update student");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const deleteStudent = async (
  currentState: CurrentState,
  data: FormData
) => {
  const id = data.get("id") as string;
  try {
    const res = await fetch(`${BASE_URL}/students/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) throw new Error("Failed to delete student");
    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};


export const createExam = async (
  currentState: CurrentState,
  data: ExamSchema
) => {
  try {
    const res = await fetch(`${BASE_URL}/exams`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: data.title,
        startTime: data.startTime,
        endTime: data.endTime,
        lessonId: data.lessonId,
      }),
    });

    if (!res.ok) throw new Error("Failed to create exam");

    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const updateExam = async (
  currentState: CurrentState,
  data: ExamSchema
) => {
  try {
    if (!data.id) throw new Error("Exam ID is required for update");

    const res = await fetch(`${BASE_URL}/exams/${data.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: data.title,
        startTime: data.startTime,
        endTime: data.endTime,
        lessonId: data.lessonId,
      }),
    });

    if (!res.ok) throw new Error("Failed to update exam");

    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};

export const deleteExam = async (
  currentState: CurrentState,
  data: FormData
) => {
  const id = data.get("id") as string;

  try {
    if (!id) throw new Error("Exam ID is required for deletion");

    const res = await fetch(`${BASE_URL}/exams/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) throw new Error("Failed to delete exam");

    return { success: true, error: false };
  } catch (err) {
    console.error(err);
    return { success: false, error: true };
  }
};