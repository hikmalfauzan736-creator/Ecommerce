from pydantic import BaseModel, EmailStr
from typing import Optional, List
from datetime import datetime

# --- AUTH ---
class UserRegister(BaseModel):
    name: str
    email: EmailStr
    password: str

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class UserOut(BaseModel):
    id: int
    name: str
    email: str
    created_at: datetime
    class Config:
        from_attributes = True

class Token(BaseModel):
    access_token: str
    token_type: str

# --- PRODUCT ---
class ProductCreate(BaseModel):
    name: str
    description: Optional[str]
    price: float
    stock: int
    category: Optional[str]
    image_url: Optional[str]

class ProductOut(ProductCreate):
    id: int
    created_at: datetime
    class Config:
        from_attributes = True

# --- ORDER ---
class OrderItemCreate(BaseModel):
    product_id: int
    quantity: int

class OrderCreate(BaseModel):
    items: List[OrderItemCreate]

class OrderItemOut(BaseModel):
    product_id: int
    quantity: int
    price: float
    class Config:
        from_attributes = True

class OrderOut(BaseModel):
    id: int
    total_price: float
    status: str
    created_at: datetime
    items: List[OrderItemOut]
    class Config:
        from_attributes = True