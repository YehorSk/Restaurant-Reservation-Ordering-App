<?php

namespace App\Http\Controllers;

use App\Models\Order;
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

            // Fetch tables that can accommodate the party size and are not already reserved for the requested date
            $availableTables = Table::query()
                ->where('capacity', '>=', $size)
                ->whereDoesntHave('reservations', function ($query) use ($date) {
                    $query->where('date', $date);
                })
                ->get();

            if ($availableTables->isEmpty()) {
                // No tables available that meet the party size requirement
                return $this->success(data: [], message: "No available tables for the requested party size");
            }

            // Fetch available time slots based on the availability of tables
            $timeSlots = TimeSlot::query()
                ->when($date === now()->format('Y-m-d'), function ($query) use ($currentTime) {
                    $query->where('start_time', '>=', $currentTime);
                })
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
            $date = $request->input('reservation_date', '');
            $size = $request->input('party_size', '');
            $timeSlotId = $request->input('time_slot_id', '');

            if (!$date || !$size || !$timeSlotId) {
                return $this->error('', 'Reservation date, party size, and time slot are required', 400);
            }

            $availableTable = Table::query()
                ->where('capacity', '>=', $size)
                ->whereDoesntHave('reservations', function ($query) use ($timeSlotId, $date) {
                    $query->where('time_slot_id', $timeSlotId)
                        ->where('date', $date);
                })
                ->first();

            if (!$availableTable) {
                return $this->error('', 'No available tables for the selected time slot', 400);
            }

            $reservation = Reservation::create([
                "client_id" => $user->id,
                "table_id" => $availableTable->id,
                "time_slot_id" => $timeSlotId,
                "party_size" => $size,
                "date" => $date,
                "status" => "pending",
            ]);

            return $this->success(data: [$reservation] , message: "Reservation was created successfully");
        }
        return $this->error('', 'No user', 401);
    }

    public function makeUserReservationOrder(Request $request, $reservationId){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $order = new Order([
                'price' => $total_price,
                'special_request' => $request->input('special_request', ''),
                'order_type' =>  4,
                'client_id' => $user->id,
                'reservation_id' => $reservationId,
                'code' => $this->generate_code(),
                'status' => 'Pending'
            ]);
            $order->save();
            $items = $user->menuItems()->get();
            foreach ($items as $item) {
                $order->orderItems()->attach($item->id, [
                    'quantity' => $item->pivot->quantity,
                    'price' => $item->pivot->price,
                ]);
                $user->menuItems()->detach($item->id);
            }
            $order = Order::with('orderItems')->find($order->id);
            return $this->success(data: [$order], message: "Pickup order was created");
        }
        return $this->error('', 'No user', 401);
    }
}
