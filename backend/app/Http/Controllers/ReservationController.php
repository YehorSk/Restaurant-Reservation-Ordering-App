<?php

namespace App\Http\Controllers;

use App\Models\Reservation;
use App\Models\Table;
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
            $date = $request->input('reservation_date', '');
            $size = $request->input('party_size', '');
            if (!$date || !$size) {
                return $this->error('', 'Reservation date and party size are required', 400);
            }

            $today = now()->format('Y-m-d');
            $currentTime = now()->setTimezone('Europe/Bratislava')->format('H:i:s');

            // Fetch available time slots
            $timeSlots = TimeSlot::query()
                ->when($date === $today, function ($query) use ($currentTime) {
                    $query->where('start_time', '>=', $currentTime);
                })
                ->whereDoesntHave('reservations', function ($query) use ($date, $size) {
                    $query->where('date', $date)
                        ->whereHas('table', function ($tableQuery) use ($size) {
                            $tableQuery->where('capacity', '>=', $size);
                        });
                })
                ->get();

            // Filter tables that meet the party size requirement and are available
            $availableTables = Table::query()
                ->where('capacity', '>=', $size)
                ->whereDoesntHave('reservations', function ($query) use ($date) {
                    $query->where('date', $date);
                })
                ->get();

            $data = [
                'time_slots' => $timeSlots,
                'available_tables' => $availableTables,
            ];

            return $this->success(data: $timeSlots, message: "");
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
