<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TimeSlot extends Model
{
    use HasFactory;

    protected $table = 'time_slot';

    protected $fillable = [
        "start_time",
        "end_time",
    ];

    public function reservations()
    {
        return $this->hasMany(Reservation::class, 'time_slot_id');
    }
}
