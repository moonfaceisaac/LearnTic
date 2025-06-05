import React, { useState, useEffect, lazy, Suspense } from "react";
import { toast } from "react-toastify";

// Import your delete actions, sesuaikan dengan cara kamu implementasi API
import {
  deleteClass,
  deleteExam,
  deleteStudent,
  deleteSubject,
  deleteTeacher,
} from "../lib/actions";

const TeacherForm = lazy(() => import("./forms/TeacherForm"));
const StudentForm = lazy(() => import("./forms/StudentForm"));
const SubjectForm = lazy(() => import("./forms/SubjectForm"));
const ClassForm = lazy(() => import("./forms/ClassForm"));
const ExamForm = lazy(() => import("./forms/ExamForm"));

const deleteActionMap: Record<string, (id: string) => Promise<void>> = {
  subject: deleteSubject,
  class: deleteClass,
  teacher: deleteTeacher,
  student: deleteStudent,
  exam: deleteExam,
  // TODO: Add other delete actions here
  parent: deleteSubject,
  lesson: deleteSubject,
  assignment: deleteSubject,
  result: deleteSubject,
  attendance: deleteSubject,
  event: deleteSubject,
  announcement: deleteSubject,
};

const forms: Record<
  string,
  (
    setOpen: React.Dispatch<React.SetStateAction<boolean>>,
    type: "create" | "update",
    data?: any,
    relatedData?: any
  ) => JSX.Element
> = {
  subject: (setOpen, type, data, relatedData) => (
    <SubjectForm type={type} data={data} setOpen={setOpen} relatedData={relatedData} />
  ),
  class: (setOpen, type, data, relatedData) => (
    <ClassForm type={type} data={data} setOpen={setOpen} relatedData={relatedData} />
  ),
  teacher: (setOpen, type, data, relatedData) => (
    <TeacherForm type={type} data={data} setOpen={setOpen} relatedData={relatedData} />
  ),
  student: (setOpen, type, data, relatedData) => (
    <StudentForm type={type} data={data} setOpen={setOpen} relatedData={relatedData} />
  ),
  exam: (setOpen, type, data, relatedData) => (
    <ExamForm type={type} data={data} setOpen={setOpen} relatedData={relatedData} />
  ),
};

type FormContainerProps = {
  table: string;
  type: "create" | "update" | "delete";
  data?: any;
  id?: string;
  relatedData?: any;
  onRefresh?: () => void; // Optional callback to refresh parent data after delete
};

const FormModal: React.FC<FormContainerProps> = ({
  table,
  type,
  data,
  id,
  relatedData,
  onRefresh,
}) => {
  const size = type === "create" ? "w-8 h-8" : "w-7 h-7";
  const bgColor =
    type === "create"
      ? "bg-lamaYellow"
      : type === "update"
      ? "bg-lamaSky"
      : "bg-lamaPurple";

  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleDelete = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!id) return;

    setLoading(true);
    try {
      const deleteAction = deleteActionMap[table];
      if (!deleteAction) throw new Error("Delete action not found");

      await deleteAction(id);
      toast.success(`${table} has been deleted!`);
      setOpen(false);
      onRefresh?.(); // Refresh parent if callback provided
    } catch (error: any) {
      toast.error(error.message || "Delete failed");
    } finally {
      setLoading(false);
    }
  };

  const Form = () => {
    if (type === "delete" && id) {
      return (
        <form onSubmit={handleDelete} className="p-4 flex flex-col gap-4">
          <input type="hidden" name="id" value={id} />
          <span className="text-center font-medium">
            All data will be lost. Are you sure you want to delete this {table}?
          </span>
          <button
            disabled={loading}
            className="bg-red-700 text-white py-2 px-4 rounded-md border-none w-max self-center"
          >
            {loading ? "Deleting..." : "Delete"}
          </button>
        </form>
      );
    }

    if (type === "create" || type === "update") {
      const FormComponent = forms[table];
      if (!FormComponent) return <p>Form not found!</p>;
      return (
        <Suspense fallback={<h1>Loading...</h1>}>
          {FormComponent(setOpen, type, data, relatedData)}
        </Suspense>
      );
    }

    return <p>Form not found!</p>;
  };

  return (
    <>
      <button
        className={`${size} flex items-center justify-center rounded-full ${bgColor}`}
        onClick={() => setOpen(true)}
      >
        <img src={`/${type}.png`} alt="" width={16} height={16} />
      </button>

      {open && (
        <div className="w-screen h-screen fixed left-0 top-0 bg-black bg-opacity-60 z-50 flex items-center justify-center">
          <div className="bg-white p-4 rounded-md relative w-[90%] md:w-[70%] lg:w-[60%] xl:w-[50%] 2xl:w-[40%]">
            <Form />
            <img
              src="/close.png"
              alt="close"
              width={14}
              height={14}
              style={{ cursor: "pointer" }}
              onClick={() => setOpen(false)}
              className="absolute top-4 right-4"
            />
          </div>
        </div>
      )}
    </>
  );
};

export default FormModal;
