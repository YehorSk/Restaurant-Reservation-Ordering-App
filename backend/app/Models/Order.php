<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    use HasFactory;

    protected $fillable = [
        'client_id',
        'code',
        'table_id',
        'waiter_id',
        'reservation_id',
        'price',
        'status',
        'special_request',
        'order_type'
    ];

    public function users()
    {
        return $this->belongsToMany(User::class);
    }

    public function orderItems()
    {
        return $this->belongsToMany(MenuItem::class, 'menu_items_orders')
            ->withPivot('id','order_id','quantity', 'price','menu_item_id')
            ->withTimestamps();
    }

}
