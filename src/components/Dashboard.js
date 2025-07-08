import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../images/argano-logo.png"; // ✅ Make sure this path is correct

const styles = {
  topBar: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "12px 30px",
    backgroundColor: "#fff",
    boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
    borderBottom: "1px solid #ddd"
  },
  logo: {
    height: "40px"
  },
  rightSection: {
    display: "flex",
    alignItems: "center",
    gap: "20px"
  },
  empId: {
    fontWeight: "bold",
    color: "#2b3e6c"
  },
  logoutButton: {
    padding: "8px 16px",
    backgroundColor: "#2b3e6c",
    color: "#fff",
    border: "none",
    borderRadius: 6,
    cursor: "pointer",
    fontWeight: "bold"
  },
  container: {
    maxWidth: "1000px",
    margin: "40px auto",
    backgroundColor: "#f0f8ff",
    padding: 30,
    borderRadius: 12,
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    fontFamily: "Segoe UI, sans-serif",
    minHeight: "60vh"
  },
  centerContent: {
    textAlign: "center",
    marginTop: "100px"
  },
  welcome: {
    fontSize: "24px",
    color: "#2b3e6c",
    marginBottom: "10px"
  },
  subtext: {
    fontSize: "16px",
    color: "#444"
  }
};

export default function Dashboard() {
  const navigate = useNavigate();
  const [empId, setEmpId] = useState("");

  useEffect(() => {
    const id = localStorage.getItem("loggedInEmpId");
    if (!id) {
      navigate("/");
    } else {
      setEmpId(id);
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("loggedInEmpId");
    navigate("/");
  };

  return (
    <>
      {/* ✅ Top bar with logo and user info */}
      <div style={styles.topBar}>
        <img src={logo} alt="Argano Logo" style={styles.logo} />
        <div style={styles.rightSection}>
          <span style={styles.empId}>Emp ID: {empId}</span>
          <button onClick={handleLogout} style={styles.logoutButton}>Logout</button>
        </div>
      </div>

      {/* ✅ Dashboard content */}
      <div style={styles.container}>
        <div style={styles.centerContent}>
          <h2 style={styles.welcome}>👋 Welcome to the Ex-Employees Portal</h2>
          <p style={styles.subtext}>We’re glad to have you here!</p>
        </div>
      </div>
    </>
  );
}
