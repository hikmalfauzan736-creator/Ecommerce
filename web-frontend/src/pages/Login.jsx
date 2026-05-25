import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(email, password);
      navigate('/');
    } catch {
      setError('Email atau password salah');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2>Login</h2>
        {error && <p style={styles.error}>{error}</p>}
        <form onSubmit={handleSubmit}>
          <input style={styles.input} type="email" placeholder="Email"
            value={email} onChange={e => setEmail(e.target.value)} required />
          <input style={styles.input} type="password" placeholder="Password"
            value={password} onChange={e => setPassword(e.target.value)} required />
          <button style={styles.button} type="submit">Masuk</button>
        </form>
        <p>Belum punya akun? <Link to="/register">Daftar</Link></p>
      </div>
    </div>
  );
}

const styles = {
  container: { display:'flex', justifyContent:'center', alignItems:'center', height:'100vh' },
  card: { padding:32, border:'1px solid #ddd', borderRadius:8, width:320 },
  input: { width:'100%', padding:10, marginBottom:12, borderRadius:4, border:'1px solid #ccc', boxSizing:'border-box' },
  button: { width:'100%', padding:10, background:'#4CAF50', color:'#fff', border:'none', borderRadius:4, cursor:'pointer' },
  error: { color:'red', marginBottom:8 }
};