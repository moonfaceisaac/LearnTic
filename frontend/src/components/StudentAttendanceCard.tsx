"use client";

import { useEffect, useState } from "react";

const StudentAttendanceCard = ({ id }: { id: string }) => {
  const [percentage, setPercentage] = useState<number | null>(null);

  useEffect(() => {
    const fetchAttendance = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/attendance/student/${id}`);
        const data = await res.json();

        const totalDays = data.totalDays ?? 0;
        const presentDays = data.presentDays ?? 0;

        const calcPercentage = totalDays > 0 ? (presentDays / totalDays) * 100 : 0;
        setPercentage(calcPercentage);
      } catch (error) {
        console.error("Error fetching attendance:", error);
        setPercentage(null);
      }
    };

    fetchAttendance();
  }, [id]);

  return (
    <div>
      <h1 className="text-xl font-semibold">
        {percentage !== null ? `${percentage.toFixed(0)}%` : "-"}
      </h1>
      <span className="text-sm text-gray-400">Attendance</span>
    </div>
  );
};

export default StudentAttendanceCard;
