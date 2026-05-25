import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import API from '../api/axios';

export default function Register() {
  const [form, setForm] = useState({ name: '', email: '', password: '', confirm: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (form.password !== form.confirm) {
      return setError('Password tidak cocok!');
    }
    if (form.password.length < 6) {
      return setError('Password minimal 6 karakter');
    }

    try {
      await API.post('/auth/register', {
        name: form.name,
        email: form.email,
        password: form.password
      });
      setSuccess('Registrasi berhasil! Silakan login.');
      setTimeout(() => navigate('/login'), 1500);
    } catch (e) {
      setError(e.response?.data?.detail || 'Registrasi gagal');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={{ textAlign: 'center', marginBottom: 24 }}>Daftar Akun</h2>

        {error && <div style={styles.errorBox}>{error}</div>}
        {success && <div style={styles.successBox}>{success}</div>}

        <form onSubmit={handleSubmit}>
          <label style={styles.label}>Nama Lengkap</label>
          <input
            style={styles.input} name="name" placeholder="Masukkan nama"
            value={form.name} onChange={handleChange} required
          />

          <label style={styles.label}>Email</label>
          <input
            style={styles.input} type="email" name="email" placeholder="Masukkan email"
            value={form.email} onChange={handleChange} required
          />

          <label style={styles.label}>Password</label>
          <input
            style={styles.input} type="password" name="password" placeholder="Min. 6 karakter"
            value={form.password} onChange={handleChange} required
          />

          <label style={styles.label}>Konfirmasi Password</label>
          <input
            style={styles.input} type="password" name="confirm" placeholder="Ulangi password"
            value={form.confirm} onChange={handleChange} required
          />

          <button type="submit" style={styles.button}>Daftar</button>
        </form>

        <p style={{ textAlign: 'center', marginTop: 16, fontSize: 14 }}>
          Sudah punya akun? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f5f5f5' },
  card: { padding: 32, background: '#fff', borderRadius: 12, boxShadow: '0 4px 12px rgba(0,0,0,0.1)', width: 360 },
  label: { display: 'block', fontSize: 13, fontWeight: '600', marginBottom: 4, color: '#333' },
  input: { width: '100%', padding: 10, marginBottom: 14, borderRadius: 6, border: '1px solid #ccc', boxSizing: 'border-box', fontSize: 14 },
  button: { width: '100%', padding: 12, background: '#1976d2', color: '#fff', border: 'none', borderRadius: 8, cursor: 'pointer', fontSize: 15 },
  errorBox: { background: '#ffebee', color: '#c62828', padding: '10px 14px', borderRadius: 6, marginBottom: 14, fontSize: 13 },
  successBox: { background: '#e8f5e9', color: '#2e7d32', padding: '10px 14px', borderRadius: 6, marginBottom: 14, fontSize: 13 }
};