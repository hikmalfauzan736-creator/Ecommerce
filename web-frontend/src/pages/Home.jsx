import { useEffect, useState } from 'react';
import API from '../api/axios';
import ProductCard from '../components/ProductCard';
import Navbar from '../components/Navbar';

export default function Home() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState('');
  const [cart, setCart] = useState([]);

  useEffect(() => {
    API.get('/products/').then(res => setProducts(res.data));
  }, []);

  const filtered = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase())
  );

  const addToCart = (product) => {
    setCart(prev => {
      const existing = prev.find(i => i.id === product.id);
      if (existing) return prev.map(i => i.id === product.id ? {...i, qty: i.qty+1} : i);
      return [...prev, {...product, qty: 1}];
    });
  };

  const checkout = async () => {
    if (cart.length === 0) return alert('Keranjang kosong!');
    try {
      await API.post('/orders/', {
        items: cart.map(i => ({ product_id: i.id, quantity: i.qty }))
      });
      alert('Pesanan berhasil dibuat!');
      setCart([]);
    } catch (e) {
      alert('Gagal checkout: ' + (e.response?.data?.detail || 'Error'));
    }
  };

  return (
    <div>
      <Navbar cart={cart} onCheckout={checkout} />
      <div style={{ padding: 20 }}>
        <input
          placeholder="Cari produk..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          style={{ padding: 8, width: '100%', marginBottom: 20, borderRadius: 4, border: '1px solid #ccc' }}
        />
        <div style={{ display:'flex', flexWrap:'wrap', gap: 16 }}>
          {filtered.map(p => (
            <ProductCard key={p.id} product={p} onAddToCart={addToCart} />
          ))}
        </div>
      </div>
    </div>
  );
}