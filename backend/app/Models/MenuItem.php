<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class MenuItem extends Model
{
    use HasFactory;

    protected $fillable = [
      'name',
      'long_description',
      'short_description',
      'recipe',
      'picture',
      'price',
      'menu_id'
    ];

    public function menu()
    {
        return $this->belongsTo(Menu::class);
    }

    public function users()
    {
        return $this->belongsToMany(User::class)
            ->withPivot('quantity', 'price', 'menu_item_id')
            ->withTimestamps();
    }

    public function orders()
    {
        return $this->belongsToMany(Order::class, 'menu_items_orders')
            ->withPivot('quantity', 'price', 'order_id')
            ->withTimestamps();
    }

    public function favoritedByUsers()
    {
        return $this->belongsToMany(User::class, 'user_favorite_items');
    }

}
