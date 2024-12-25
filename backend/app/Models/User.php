<?php

namespace App\Models;

// use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasApiTokens,HasFactory, Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'name',
        'email',
        'password',
        'role',
        'phone',
        'address'
    ];

    /**
     * The attributes that should be hidden for serialization.
     *
     * @var array<int, string>
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * Get the attributes that should be cast.
     *
     * @return array<string, string>
     */
    protected function casts(): array
    {
        return [
            'email_verified_at' => 'datetime',
            'password' => 'hashed',
        ];
    }

    public function menuItems()
    {
        return $this->belongsToMany(MenuItem::class)
            ->withPivot('id','quantity', 'price','menu_item_id')
            ->withTimestamps();
    }

    public function favoriteItems()
    {
        return $this->belongsToMany(MenuItem::class, 'user_favorite_items');
    }

    public function reservations()
    {
        return $this->hasMany(Reservation::class, 'client_id');
    }

    public function clientOrders()
    {
        return $this->hasMany(Order::class, 'client_id');
    }

    public function waiterOrders()
    {
        return $this->hasMany(Order::class, 'waiter_id');
    }
}
