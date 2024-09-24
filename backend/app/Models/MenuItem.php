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
        return $this->belongsToMany(User::class);
    }

}
