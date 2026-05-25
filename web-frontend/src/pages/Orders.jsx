import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';
import Navbar from '../components/Navbar';
import { useAuth } from '../context/AuthContext';

const STATUS_COLOR = {
  pending: { bg: '#fff3e0', text: '#e65100', label: '⏳ Menunggu' },
  paid:    { bg: '#e8f5e9', text: '#2e7d32', label: '✅ Dibayar' },
  shipped: { bg: '#e3f2fd', text: '#1565c0', label: '🚚 Dikirim' },
  done:    { bg: '#f3e5f5', text: '#6a1b9a', label: '🎉 Selesai' },
};

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) { navigate('/login'); return; }
    API.get('/orders/my')
      .then(res => setOrders(res.data))
      .finally(() => setLoading(false));
  }, [user]);

  if (loading) return <div style={{ padding: 40, textAlign: 'center' }}>Memuat pesanan...</div>;

  return (
    <div>
      <Navbar cart={[]} />
      <div style={styles.container}>
        <h2 style={styles.title}>Pesanan Saya</h2>

        {orders.length === 0 ? (
          <div style={styles.empty}>
            <p>📦 Belum ada pesanan</p>
            <button onClick={() => navigate('/')} style={styles.shopBtn}>Mulai Belanja</button>
          </div>
        ) : (
          orders.map(order => {
            const status = STATUS_COLOR[order.status] || STATUS_COLOR.pending;
            return (
              <div key={order.id} style={styles.card}>
                {/* Header */}
                <div style={styles.cardHeader}>
                  <span style={styles.orderId}>Pesanan #{order.id}</span>
                  <span style={{ ...styles.statusBadge, background: status.bg, color: status.text }}>
                    {status.label}
                  </span>
                </div>

                {/* Items */}
                <div style={styles.itemsList}>
                  {order.items.map((item, idx) => (
                    <div key={idx} style={styles.itemRow}>
                      <span style={styles.itemName}>Produk ID: {item.product_id}</span>
                      <span style={styles.itemQty}>x{item.quantity}</span>
                      <span style={styles.itemPrice}>
                        Rp {(item.price * item.quantity).toLocaleString('id-ID')}
                      </span>
                    </div>
                  ))}
                </div>

                {/* Footer */}
                <div style={styles.cardFooter}>
                  <span style={styles.date}>
                    {new Date(order.created_at).toLocaleDateString('id-ID', {
                      day: 'numeric', month: 'long', year: 'numeric'
                    })}
                  </span>
                  <span style={styles.total}>
                    Total: <strong>Rp {order.total_price.toLocaleString('id-ID')}</strong>
                  </span>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

const styles = {
  container: { maxWidth: 800, margin: '30px auto', padding: '0 20px' },
  title: { fontSize: 24, marginBottom: 24 },
  empty: { textAlign: 'center', padding: 60, color: '#999' },
  shopBtn: { marginTop: 16, padding: '10px 24px', background: '#1976d2', color: '#fff', border: 'none', borderRadius: 8, cursor: 'pointer' },
  card: { background: '#fff', borderRadius: 10, marginBottom: 16, boxShadow: '0 2px 8px rgba(0,0,0,0.08)', overflow: 'hidden' },
  cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '14px 20px', borderBottom: '1px solid #f0f0f0' },
  orderId: { fontWeight: 'bold', fontSize: 15 },
  statusBadge: { padding: '4px 12px', borderRadius: 20, fontSize: 13, fontWeight: '600' },
  itemsList: { padding: '12px 20px' },
  itemRow: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '6px 0', borderBottom: '1px solid #fafafa' },
  itemName: { flex: 1, fontSize: 14, color: '#333' },
  itemQty: { fontSize: 13, color: '#666', margin: '0 16px' },
  itemPrice: { fontSize: 14, fontWeight: '600', color: '#1976d2' },
  cardFooter: { display: 'flex', justifyContent: 'space-between', padding: '12px 20px', background: '#fafafa', fontSize: 13 },
  date: { color: '#999' },
  total: { color: '#333', fontSize: 14 }
};