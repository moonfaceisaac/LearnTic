import { useEffect, useState } from "react";

const UserCard = ({ type }: { type: "admin" | "teacher" | "student" | "parent" }) => {
  const [count, setCount] = useState<number | null>(null);

  useEffect(() => {
    const fetchCount = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/${type}/count`);
        const data = await res.json();
        setCount(data.count);
      } catch (err) {
        console.error("Failed to fetch count", err);
        setCount(0); // fallback jika gagal
      }
    };

    fetchCount();
  }, [type]);

  return (
    <div className="rounded-2xl odd:bg-lamaPurple even:bg-lamaYellow p-4 flex-1 min-w-[130px]">
      <div className="flex justify-between items-center">
        <span className="text-[10px] bg-white px-2 py-1 rounded-full text-green-600">
          2024/25
        </span>
        <img src="/more.png" alt="More icon" width={20} height={20} />
      </div>
      <h1 className="text-2xl font-semibold my-4">{count ?? "..."}</h1>
      <h2 className="capitalize text-sm font-medium text-gray-500">{type}s</h2>
    </div>
  );
};

export default UserCard;
