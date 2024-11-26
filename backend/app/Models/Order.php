<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    use HasFactory;

    protected $fillable = [
        'client_id',
        'table_id',
        'waiter_id',
        'reservation_id',
        'price',
        'status',
        'special_request',
        'pickup',
        'home_delivery',
        'dine_in',
    ];

    public function users()
    {
        return $this->belongsToMany(User::class);
    }

    public function orderItems()
    {
        return $this->belongsToMany(MenuItem::class, 'menu_items_orders')
            ->withPivot('order_id','quantity', 'price','menu_item_id')
            ->withTimestamps();
    }

}
