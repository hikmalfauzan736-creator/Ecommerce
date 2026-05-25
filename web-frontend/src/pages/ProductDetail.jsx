import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api/axios';
import Navbar from '../components/Navbar';

export default function ProductDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [qty, setQty] = useState(1);
  const [cart, setCart] = useState([]);
  const [added, setAdded] = useState(false);

  useEffect(() => {
    API.get(`/products/${id}`).then(res => setProduct(res.data));
  }, [id]);

  const handleAddToCart = () => {
    setCart(prev => {
      const ex = prev.find(i => i.id === product.id);
      if (ex) return prev.map(i => i.id === product.id ? { ...i, qty: i.qty + qty } : i);
      return [...prev, { ...product, qty }];
    });
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  if (!product) return <div style={{ padding: 40, textAlign: 'center' }}>Memuat...</div>;

  return (
    <div>
      <Navbar cart={cart} />
      <div style={styles.container}>
        <button onClick={() => navigate(-1)} style={styles.backBtn}>← Kembali</button>

        <div style={styles.card}>
          <img
            src={product.image_url || 'https://via.placeholder.com/400x300'}
            alt={product.name}
            style={styles.img}
          />

          <div style={styles.info}>
            <span style={styles.category}>{product.category}</span>
            <h1 style={styles.name}>{product.name}</h1>
            <p style={styles.price}>Rp {product.price.toLocaleString('id-ID')}</p>
            <p style={styles.stock}>
              {product.stock > 0
                ? `✅ Stok tersedia: ${product.stock} pcs`
                : '❌ Stok habis'}
            </p>
            <p style={styles.desc}>{product.description || 'Tidak ada deskripsi.'}</p>

            {/* Quantity Picker */}
            <div style={styles.qtyRow}>
              <span style={styles.qtyLabel}>Jumlah:</span>
              <button
                onClick={() => setQty(q => Math.max(1, q - 1))}
                style={styles.qtyBtn}
              >−</button>
              <span style={styles.qtyVal}>{qty}</span>
              <button
                onClick={() => setQty(q => Math.min(product.stock, q + 1))}
                style={styles.qtyBtn}
              >+</button>
            </div>

            <button
              onClick={handleAddToCart}
              disabled={product.stock === 0}
              style={{
                ...styles.addBtn,
                background: added ? '#4caf50' : '#1976d2',
                opacity: product.stock === 0 ? 0.5 : 1
              }}
            >
              {added ? '✓ Ditambahkan!' : '+ Tambah ke Keranjang'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: { maxWidth: 900, margin: '30px auto', padding: '0 20px' },
  backBtn: { background: 'none', border: 'none', color: '#1976d2', cursor: 'pointer', fontSize: 15, marginBottom: 20 },
  card: { display: 'flex', gap: 32, background: '#fff', borderRadius: 12, padding: 24, boxShadow: '0 2px 12px rgba(0,0,0,0.1)', flexWrap: 'wrap' },
  img: { width: 380, height: 300, objectFit: 'cover', borderRadius: 8 },
  info: { flex: 1, minWidth: 240 },
  category: { background: '#e3f2fd', color: '#1976d2', padding: '4px 10px', borderRadius: 20, fontSize: 12, fontWeight: '600' },
  name: { margin: '12px 0 8px', fontSize: 26 },
  price: { fontSize: 24, fontWeight: 'bold', color: '#2e7d32', margin: '0 0 8px' },
  stock: { fontSize: 14, marginBottom: 12, color: '#555' },
  desc: { fontSize: 14, color: '#666', lineHeight: 1.6, marginBottom: 20 },
  qtyRow: { display: 'flex', alignItems: 'center', gap: 12, marginBottom: 20 },
  qtyLabel: { fontSize: 15 },
  qtyBtn: { width: 32, height: 32, border: '1px solid #ddd', background: '#f5f5f5', borderRadius: 6, cursor: 'pointer', fontSize: 18 },
  qtyVal: { fontSize: 16, fontWeight: 'bold', minWidth: 24, textAlign: 'center' },
  addBtn: { width: '100%', padding: 14, color: '#fff', border: 'none', borderRadius: 8, cursor: 'pointer', fontSize: 15, transition: 'background 0.3s' }
};