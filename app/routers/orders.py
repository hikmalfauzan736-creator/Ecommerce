from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .. import models, schemas, auth
from ..database import get_db

router = APIRouter(prefix="/api/orders", tags=["Orders"])

@router.post("/", response_model=schemas.OrderOut)
def create_order(
    order_data: schemas.OrderCreate,
    db: Session = Depends(get_db),
    current_user=Depends(auth.get_current_user)
):
    total = 0
    order_items = []
    for item in order_data.items:
        product = db.query(models.Product).filter(models.Product.id == item.product_id).first()
        if not product:
            raise HTTPException(status_code=404, detail=f"Produk ID {item.product_id} tidak ditemukan")
        if product.stock < item.quantity:
            raise HTTPException(status_code=400, detail=f"Stok {product.name} tidak cukup")
        total += product.price * item.quantity
        product.stock -= item.quantity
        order_items.append({"product": product, "quantity": item.quantity, "price": product.price})

    new_order = models.Order(user_id=current_user.id, total_price=total)
    db.add(new_order)
    db.commit()
    db.refresh(new_order)

    for item in order_items:
        db.add(models.OrderItem(
            order_id=new_order.id,
            product_id=item["product"].id,
            quantity=item["quantity"],
            price=item["price"]
        ))
    db.commit()
    db.refresh(new_order)
    return new_order

@router.get("/my", response_model=List[schemas.OrderOut])
def my_orders(db: Session = Depends(get_db), current_user=Depends(auth.get_current_user)):
    return db.query(models.Order).filter(models.Order.user_id == current_user.id).all()