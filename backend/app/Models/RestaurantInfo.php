<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class RestaurantInfo extends Model
{

    protected $table = 'restaurant_info';

    protected $fillable = [
        'name',
        'description',
        'address',
        'phone',
        'email',
        'website',
        'opening_hours',
        ];
}
