<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserDevice extends Model
{
    use HasFactory;

    protected $table = 'user_devices';

    protected $fillable = [
        'user_id',
        'device_token',
        'device_id',
        'device_type',
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

}
