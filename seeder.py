import requests

BASE_URL = "http://localhost:8000/api"

# 1. Register admin
requests.post(f"{BASE_URL}/auth/register", json={
    "name": "Admin",
    "email": "admin@gmail.com",
    "password": "123456"
})

# 2. Login
res = requests.post(f"{BASE_URL}/auth/login", json={
    "email": "admin@gmail.com",
    "password": "123456"
})
token = res.json()["access_token"]
headers = {"Authorization": f"Bearer {token}"}

# 3. Tambah produk
products = [
    {
        "name": "Sepatu Nike air max ",
        "description": "Sepatu olahraga nyaman dan stylish",
        "price": 900000,
        "stock": 10,
        "category": "Sepatu",
        "image_url": "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/u_126ab356-44d8-4a06-89b4-fcdcc8df0245,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/689bf39a-6e96-4a55-adbc-0cbc8fe387d5/WMNS+AIR+JORDAN+1+MID.png"
    }
]

for p in products:
    res = requests.post(f"{BASE_URL}/products/", json=p, headers=headers)
    print(f"✅ Produk ditambah: {p['name']} — Status: {res.status_code}")

print("\n🎉 Seeder selesai! Cek http://localhost:3000")