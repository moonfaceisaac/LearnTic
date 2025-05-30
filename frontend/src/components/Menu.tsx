import React from "react";

const menuItems = [
  {
    title: "MENU",
    items: [
      { icon: "/home.png", label: "Home", href: "/", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/teacher.png", label: "Teachers", href: "/list/teachers", visible: ["admin", "teacher"] },
      { icon: "/student.png", label: "Students", href: "/list/students", visible: ["admin", "teacher"] },
      { icon: "/parent.png", label: "Parents", href: "/list/parents", visible: ["admin", "teacher"] },
      { icon: "/subject.png", label: "Subjects", href: "/list/subjects", visible: ["admin"] },
      { icon: "/class.png", label: "Classes", href: "/list/classes", visible: ["admin", "teacher"] },
      { icon: "/lesson.png", label: "Lessons", href: "/list/lessons", visible: ["admin", "teacher"] },
      { icon: "/exam.png", label: "Exams", href: "/list/exams", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/assignment.png", label: "Assignments", href: "/list/assignments", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/result.png", label: "Results", href: "/list/results", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/attendance.png", label: "Attendance", href: "/list/attendance", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/calendar.png", label: "Events", href: "/list/events", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/message.png", label: "Messages", href: "/list/messages", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/announcement.png", label: "Announcements", href: "/list/announcements", visible: ["admin", "teacher", "student", "parent"] },
    ],
  },
  {
    title: "OTHER",
    items: [
      { icon: "/profile.png", label: "Profile", href: "/profile", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/setting.png", label: "Settings", href: "/settings", visible: ["admin", "teacher", "student", "parent"] },
      { icon: "/logout.png", label: "Logout", href: "/logout", visible: ["admin", "teacher", "student", "parent"] },
    ],
  },
];

interface MenuProps {
  userRole: string | null;
}

const Menu: React.FC<MenuProps> = ({ userRole }) => {
  if (!userRole) return <div>Loading menu...</div>;

  return (
    <div style={{ marginTop: 16, fontSize: 14 }}>
      {menuItems.map(section => {
        const visibleItems = section.items.filter(item => item.visible.includes(userRole));

        if (visibleItems.length === 0) return null;

        return (
          <div key={section.title} style={{ marginBottom: 16 }}>
            <div style={{ color: "#888", fontWeight: "lighter", marginBottom: 8, display: "none" /* simulasikan lg:hidden */ }}>
              {section.title}
            </div>
            {visibleItems.map(item => (
              <a
                href={item.href}
                key={item.label}
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: 12,
                  color: "#666",
                  padding: "8px 12px",
                  borderRadius: 6,
                  textDecoration: "none",
                  cursor: "pointer",
                }}
                onMouseEnter={e => (e.currentTarget.style.backgroundColor = "#dbeafe")}
                onMouseLeave={e => (e.currentTarget.style.backgroundColor = "transparent")}
              >
                <img src={item.icon} alt={`${item.label} icon`} width={20} height={20} />
                <span style={{ display: "none" /* simulasikan lg:block */ }}>{item.label}</span>
              </a>
            ))}
          </div>
        );
      })}
    </div>
  );
};

export default Menu;
