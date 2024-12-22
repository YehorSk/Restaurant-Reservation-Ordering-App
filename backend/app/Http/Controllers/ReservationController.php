<?php

namespace App\Http\Controllers;

use App\Models\Reservation;
use App\Models\TimeSlot;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class ReservationController extends Controller
{

    use HttpResponses;

    public function getAvailableTimeSlots(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = TimeSlot::all();
            return $this->success(data: $items, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function getReservations(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){

        }
        return $this->error('', 'No user', 401);
    }

    public function createReservation(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $reservation = new Reservation([
                "client_id" =>  $user->id,
                "table_id" => $request->input('table_id', ''),
                "time_slot_id" => $request->input('table_id', ''),
                "party_size" => $request->input('party_size', ''),
                "date" => $request->input('reservation_date', ''),
                "status" => "pending",
            ]);
        }
        return $this->error('', 'No user', 401);
    }
}
