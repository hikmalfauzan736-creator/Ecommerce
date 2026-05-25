export default function ProductCard({ product, onAddToCart }) {
  return (
    <div style={styles.card}>
      <img src={product.image_url || 'https://via.placeholder.com/200'}
        alt={product.name} style={styles.img} />
      <h3 style={{ margin:'8px 0 4px' }}>{product.name}</h3>
      <p style={{ color:'#666', fontSize:13 }}>{product.category}</p>
      <p style={{ fontWeight:'bold', color:'#2e7d32' }}>
        Rp {product.price.toLocaleString('id-ID')}
      </p>
      <p style={{ fontSize:12, color: product.stock > 0 ? 'green' : 'red' }}>
        {product.stock > 0 ? `Stok: ${product.stock}` : 'Habis'}
      </p>
      <button
        onClick={() => onAddToCart(product)}
        disabled={product.stock === 0}
        style={styles.btn}
      >
        + Keranjang
      </button>
    </div>
  );
}

const styles = {
  card: { border:'1px solid #eee', borderRadius:8, padding:12, width:200, boxShadow:'0 2px 4px rgba(0,0,0,0.1)' },
  img: { width:'100%', height:150, objectFit:'cover', borderRadius:4 },
  btn: { width:'100%', padding:8, background:'#1976d2', color:'#fff', border:'none', borderRadius:4, cursor:'pointer', marginTop:8 }
};