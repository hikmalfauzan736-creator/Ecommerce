import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

export default function Navbar({ cart = [], onCheckout }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const totalItems = cart.reduce((sum, item) => sum + item.qty, 0);
  const totalPrice = cart.reduce((sum, item) => sum + item.price * item.qty, 0);

  return (
    <nav style={styles.nav}>
      <Link to="/" style={styles.brand}>🛒 TokoKu</Link>

      <div style={styles.menu}>
        <Link to="/" style={styles.link}>Produk</Link>
        <Link to="/orders" style={styles.link}>Pesanan Saya</Link>
      </div>

      <div style={styles.right}>
        {/* Cart Info */}
        <div style={styles.cartBox}>
          <span style={styles.cartCount}>{totalItems} item</span>
          <span style={styles.cartPrice}>Rp {totalPrice.toLocaleString('id-ID')}</span>
          <button
            onClick={onCheckout}
            disabled={totalItems === 0}
            style={{
              ...styles.checkoutBtn,
              opacity: totalItems === 0 ? 0.5 : 1,
              cursor: totalItems === 0 ? 'not-allowed' : 'pointer'
            }}
          >
            Checkout
          </button>
        </div>

        {/* User Info */}
        {user ? (
          <div style={styles.userBox}>
            <span style={styles.username}>👤 {user.name}</span>
            <button onClick={handleLogout} style={styles.logoutBtn}>Keluar</button>
          </div>
        ) : (
          <Link to="/login" style={styles.loginBtn}>Login</Link>
        )}
      </div>
    </nav>
  );
}

const styles = {
  nav: {
    display: 'flex', alignItems: 'center', justifyContent: 'space-between',
    padding: '12px 24px', backgroundColor: '#1976d2', color: '#fff',
    position: 'sticky', top: 0, zIndex: 100, flexWrap: 'wrap', gap: 8
  },
  brand: { color: '#fff', textDecoration: 'none', fontSize: 22, fontWeight: 'bold' },
  menu: { display: 'flex', gap: 20 },
  link: { color: '#fff', textDecoration: 'none', fontSize: 14 },
  right: { display: 'flex', alignItems: 'center', gap: 16 },
  cartBox: { display: 'flex', alignItems: 'center', gap: 8, background: 'rgba(255,255,255,0.15)', padding: '6px 12px', borderRadius: 8 },
  cartCount: { fontSize: 13, color: '#fff' },
  cartPrice: { fontSize: 13, fontWeight: 'bold', color: '#fff' },
  checkoutBtn: { padding: '6px 14px', background: '#ff9800', color: '#fff', border: 'none', borderRadius: 6, fontSize: 13 },
  userBox: { display: 'flex', alignItems: 'center', gap: 8 },
  username: { fontSize: 13, color: '#fff' },
  logoutBtn: { padding: '6px 12px', background: 'transparent', color: '#fff', border: '1px solid #fff', borderRadius: 6, cursor: 'pointer', fontSize: 13 },
  loginBtn: { color: '#fff', textDecoration: 'none', border: '1px solid #fff', padding: '6px 14px', borderRadius: 6, fontSize: 13 }
};