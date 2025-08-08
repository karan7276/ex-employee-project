import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../images/argano-logo.png";

export default function Dashboard() {
  const navigate = useNavigate();
  const [empId, setEmpId] = useState("");
  const [fullName, setFullName] = useState("");
  const [designation, setDesignation] = useState("");
  const [joinDate, setJoinDate] = useState("");
  const [lastWorkingDay, setLastWorkingDay] = useState("");

  useEffect(() => {
    const id = localStorage.getItem("loggedInEmpId");
    const firstName = localStorage.getItem("firstName") || "";
    const lastName = localStorage.getItem("lastName") || "";
    const desig = localStorage.getItem("designation") || "";
    const join = localStorage.getItem("joinDate") || "";
    const lastDay = localStorage.getItem("lastWorkingDay") || "";

    if (!id) {
      navigate("/");
    } else {
      setEmpId(id);
      setFullName(`${firstName} ${lastName}`); // already stored in uppercase
      setDesignation(desig);
      setJoinDate(join);
      setLastWorkingDay(lastDay);
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const styles = {
    topBar: {
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "10px 20px",
      backgroundColor: "#f0f8ff",
      borderBottom: "1px solid #ddd"
    },
    logo: { height: 40, cursor: "pointer" },
    rightSection: { display: "flex", alignItems: "center", gap: "15px" },
    empId: { fontWeight: "bold", color: "#2b3e6c" },
    logoutButton: {
      backgroundColor: "#d9534f",
      color: "#fff",
      border: "none",
      borderRadius: "5px",
      padding: "5px 10px",
      cursor: "pointer",
      fontWeight: "bold"
    },
    container: { padding: 30, fontFamily: "Segoe UI, sans-serif" },
    centerContent: { textAlign: "center", marginTop: 50 },
    welcome: { fontSize: "24px", color: "#2b3e6c" },
    subtext: { fontSize: "16px", color: "#555" },
    details: {
      marginTop: 30,
      maxWidth: 500,
      marginLeft: "auto",
      marginRight: "auto",
      textAlign: "left",
      backgroundColor: "#fff",
      padding: 20,
      borderRadius: 8,
      boxShadow: "0 4px 12px rgba(0,0,0,0.1)"
    },
    detailItem: { marginBottom: 10, fontSize: "15px" },
    label: { fontWeight: "bold", color: "#2b3e6c" }
  };

  return (
    <>
      <div style={styles.topBar}>
        <img
          src={logo}
          alt="Argano Logo"
          style={styles.logo}
          onClick={() => navigate("/dashboard")}
        />
        <div style={styles.rightSection}>
          <span style={styles.empId}>{fullName} (Emp ID: {empId})</span>
          <button onClick={handleLogout} style={styles.logoutButton}>
            Logout
          </button>
        </div>
      </div>

      <div style={styles.container}>
        <div style={styles.centerContent}>
          <h2 style={styles.welcome}>👋 Welcome to the ConnectBack Portal</h2>
          <p style={styles.subtext}>We’re glad to have you here!</p>
        </div>

        <div style={styles.details}>
          <div style={styles.detailItem}>
            <span style={styles.label}>Designation: </span> {designation}
          </div>
          <div style={styles.detailItem}>
            <span style={styles.label}>Join Date: </span> {joinDate}
          </div>
          <div style={styles.detailItem}>
            <span style={styles.label}>Last Working Day: </span> {lastWorkingDay}
          </div>
        </div>
      </div>
    </>
  );
}
