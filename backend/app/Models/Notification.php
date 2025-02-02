<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    protected $fillable = [
        "user_id",
        "title",
        "body",
        "read"
    ];

    public function users()
    {
        return $this->belongsToMany(User::class);
    }

}
