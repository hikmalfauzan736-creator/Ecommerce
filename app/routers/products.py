from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from .. import models, schemas, auth
from ..database import get_db

router = APIRouter(prefix="/api/products", tags=["Products"])

@router.get("/", response_model=List[schemas.ProductOut])
def get_products(
    category: Optional[str] = None,
    search: Optional[str] = None,
    db: Session = Depends(get_db)
):
    query = db.query(models.Product)
    if category:
        query = query.filter(models.Product.category == category)
    if search:
        query = query.filter(models.Product.name.ilike(f"%{search}%"))
    return query.all()

@router.get("/{product_id}", response_model=schemas.ProductOut)
def get_product(product_id: int, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Produk tidak ditemukan")
    return product

@router.post("/", response_model=schemas.ProductOut)
def create_product(
    product: schemas.ProductCreate,
    db: Session = Depends(get_db),
    current_user=Depends(auth.get_current_user)
):
    new_product = models.Product(**product.dict())
    db.add(new_product)
    db.commit()
    db.refresh(new_product)
    return new_product
@router.post("/seed")
def seed_products(db: Session = Depends(get_db)):
    products = [
        models.Product(
            name="Laptop Gaming",
            description="Laptop gaming RTX",
            price=15000000,
            category="Elektronik",
            image="https://via.placeholder.com/300"
        ),
        models.Product(
            name="Keyboard Mechanical",
            description="Keyboard RGB",
            price=750000,
            category="Aksesoris",
            image="https://via.placeholder.com/300"
        ),
        models.Product(
            name="Headset Gaming",
            description="Audio jernih",
            price=500000,
            category="Audio",
            image="https://via.placeholder.com/300"
        )
    ]

    db.add_all(products)
    db.commit()

    return {"message": "Produk berhasil ditambahkan"}
@router.delete("/{product_id}")
def delete_product(
    product_id: int, 
    db: Session = Depends(get_db),
    current_user=Depends(auth.get_current_user)
):
    product_query = db.query(models.Product).filter(models.Product.id == product_id)
    product = product_query.first()

    if not product:
        raise HTTPException(status_code=404, detail="Produk tidak ditemukan")

    product_query.delete(synchronize_session=False)
    db.commit()

    return {"message": "Produk berhasil dihapus"}