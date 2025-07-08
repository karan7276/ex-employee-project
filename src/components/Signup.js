import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header"; // 🔁 Import Header

export default function Signup() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    email: "",
    empId: "",
    password: ""
  });

  const [visibleFields, setVisibleFields] = useState([]);
  const [showOtp, setShowOtp] = useState(false);
  const [otp, setOtp] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const timers = [];
    for (let i = 0; i < 6; i++) {
      timers.push(setTimeout(() => setVisibleFields(prev => [...prev, i]), i * 150));
    }
    return () => timers.forEach(clearTimeout);
  }, []);

  const handleChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSignup = e => {
    e.preventDefault();
    console.log("Captured Signup Data:", formData);
    setShowOtp(true);
    setMessage("📨 OTP has been sent to your email");
  };

  const handleOtpSubmit = e => {
    e.preventDefault();
    if (otp.trim() !== "") {
      setSubmitted(true);
      setMessage("✅ Signup complete.");
    } else {
      setMessage("❌ Please enter the OTP.");
    }
  };

  const fields = [
    { name: "firstName", label: "First Name", type: "text", placeholder: "Enter First Name" },
    { name: "lastName", label: "Last Name", type: "text", placeholder: "Enter Last Name" },
    { name: "phone", label: "Phone Number", type: "text", placeholder: "Enter Phone Number" },
    { name: "email", label: "Email", type: "email", placeholder: "Enter Email" },
    { name: "empId", label: "Emp ID (Argano)", type: "text", placeholder: "Enter Emp ID" },
    { name: "password", label: "Password", type: "password", placeholder: "Enter Password" }
  ];

  return (
    <>
      <Header />
      <div style={styles.container}>
        <h2 style={styles.title}>Signup</h2>

        {!submitted ? (
          <>
            {!showOtp ? (
              <>
                <form onSubmit={handleSignup} style={styles.form}>
                  {fields.map((field, index) =>
                    visibleFields.includes(index) ? (
                      <div style={styles.field} key={field.name}>
                        <label style={styles.label}>{field.label}</label>
                        <input
                          name={field.name}
                          type={field.type}
                          placeholder={field.placeholder}
                          value={formData[field.name]}
                          onChange={handleChange}
                          required
                          style={styles.input}
                        />
                      </div>
                    ) : null
                  )}
                  <button type="submit" style={styles.button}>Sign Up</button>
                </form>

                <p style={styles.text}>
                  Already have an account?{" "}
                  <button type="button" onClick={() => navigate("/")} style={styles.link}>
                    Login
                  </button>
                </p>
              </>
            ) : (
              <form onSubmit={handleOtpSubmit} style={{ textAlign: "center", marginTop: 20 }}>
                <label style={styles.label}>Enter OTP</label>
                <input
                  type="text"
                  placeholder="Enter OTP"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  required
                  style={styles.input}
                />
                <br /><br />
                <button type="submit" style={styles.button}>Verify OTP</button>
                <p style={styles.text}>
                  <button type="button" onClick={() => navigate("/")} style={styles.link}>
                    Login
                  </button>
                </p>
              </form>
            )}
          </>
        ) : (
          <div style={{ textAlign: "center", marginTop: 20 }}>
            <p style={styles.successMsg}>{message}</p>
            <button onClick={() => navigate("/")} style={styles.button}>
              Proceed to Login
            </button>
          </div>
        )}

        {message && !submitted && <p style={styles.infoMsg}>{message}</p>}
      </div>
    </>
  );
}

const styles = {
  container: {
    maxWidth: 450,
    margin: "60px auto",
    backgroundColor: "#f0f8ff",
    padding: 30,
    borderRadius: 12,
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    fontFamily: "Segoe UI, sans-serif"
  },
  title: {
    textAlign: "center",
    color: "#2b3e6c",
    marginBottom: 30
  },
  form: {
    display: "flex",
    flexDirection: "column"
  },
  field: {
    marginBottom: 20
  },
  label: {
    display: "block",
    marginBottom: 6,
    color: "#2b3e6c",
    fontWeight: 600
  },
  input: {
    width: "100%",
    padding: "10px 12px",
    fontSize: "15px",
    border: "1px solid #ccc",
    borderRadius: 6
  },
  button: {
    marginTop: 10,
    padding: "10px",
    backgroundColor: "#2b3e6c",
    color: "white",
    fontWeight: "bold",
    border: "none",
    borderRadius: 6,
    cursor: "pointer"
  },
  text: {
    textAlign: "center",
    marginTop: 20
  },
  link: {
    background: "none",
    color: "#2b3e6c",
    border: "none",
    cursor: "pointer",
    fontWeight: "bold",
    textDecoration: "underline"
  },
  successMsg: {
    fontSize: "16px",
    color: "green",
    marginBottom: 20
  },
  infoMsg: {
    textAlign: "center",
    marginTop: 15,
    color: "#444"
  }
};
