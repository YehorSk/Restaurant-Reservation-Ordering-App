<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Favorite extends Model
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

}
