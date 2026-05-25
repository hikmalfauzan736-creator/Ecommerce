export default function Cart({ cart, onRemove, onCheckout }) {
  const total = cart.reduce((sum, item) => sum + item.price * item.qty, 0);

  if (cart.length === 0) {
    return (
      <div style={styles.empty}>
        <p>🛒 Keranjang kosong</p>
      </div>
    );
  }

  return (
    <div style={styles.container}>
      <h3 style={{ marginBottom: 16 }}>Keranjang Belanja</h3>

      {cart.map(item => (
        <div key={item.id} style={styles.row}>
          <img
            src={item.image_url || 'https://via.placeholder.com/60'}
            alt={item.name}
            style={styles.img}
          />
          <div style={styles.info}>
            <p style={styles.name}>{item.name}</p>
            <p style={styles.price}>
              Rp {item.price.toLocaleString('id-ID')} × {item.qty}
            </p>
          </div>
          <div style={styles.rightCol}>
            <p style={styles.subtotal}>
              Rp {(item.price * item.qty).toLocaleString('id-ID')}
            </p>
            <button onClick={() => onRemove(item.id)} style={styles.removeBtn}>✕</button>
          </div>
        </div>
      ))}

      <div style={styles.totalRow}>
        <span style={styles.totalLabel}>Total</span>
        <span style={styles.totalValue}>Rp {total.toLocaleString('id-ID')}</span>
      </div>

      <button onClick={onCheckout} style={styles.checkoutBtn}>
        Checkout Sekarang
      </button>
    </div>
  );
}

const styles = {
  container: { padding: 20, background: '#fff', borderRadius: 8, boxShadow: '0 2px 8px rgba(0,0,0,0.1)' },
  empty: { textAlign: 'center', padding: 40, color: '#999' },
  row: { display: 'flex', alignItems: 'center', gap: 12, marginBottom: 12, paddingBottom: 12, borderBottom: '1px solid #f0f0f0' },
  img: { width: 60, height: 60, objectFit: 'cover', borderRadius: 6 },
  info: { flex: 1 },
  name: { margin: 0, fontWeight: '600', fontSize: 14 },
  price: { margin: '4px 0 0', color: '#666', fontSize: 13 },
  rightCol: { display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 6 },
  subtotal: { margin: 0, fontWeight: 'bold', color: '#2e7d32', fontSize: 14 },
  removeBtn: { background: 'none', border: 'none', color: '#e53935', cursor: 'pointer', fontSize: 16 },
  totalRow: { display: 'flex', justifyContent: 'space-between', padding: '12px 0', borderTop: '2px solid #eee', marginTop: 8 },
  totalLabel: { fontWeight: 'bold', fontSize: 16 },
  totalValue: { fontWeight: 'bold', fontSize: 16, color: '#1976d2' },
  checkoutBtn: { width: '100%', padding: 12, background: '#1976d2', color: '#fff', border: 'none', borderRadius: 8, cursor: 'pointer', fontSize: 15, marginTop: 12 }
};