<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Reservation extends Model
{
    use HasFactory;

    protected $table = 'reservations';

    protected $fillable = [
        'client_id',
        'table_id',
        'time_slot_id',
        'status',
        'code',
        'party_size',
        'date',
        'special_request',
        'phone'
    ];

    public function timeSlot()
    {
        return $this->belongsTo(TimeSlot::class, 'time_slot_id');
    }

    public function table()
    {
        return $this->belongsTo(Table::class, 'table_id');
    }
}
