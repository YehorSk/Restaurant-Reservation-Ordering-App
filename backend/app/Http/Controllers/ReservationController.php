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

    public function getAllReservations(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $items = Reservation::with('table', 'timeSlot')->get();

            $items->each(function ($item) {
                $item->table_number = $item->table->number ?? null;
                $item->start_time = $item->timeSlot->start_time ?? null;

                unset($item->table);
                unset($item->timeSlot);
            });
            return $this->success($items);
        }
        return $this->error('', 'No user', 401);
    }

    public function adminUpdateReservation(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $reservation = Reservation::find($id);
            $data = $request->validate([
                "status" => "required",
            ]);
            $reservation->update($data);
            return $this->success(data: $user, message: "Reservation updated successfully");
        }
        return $this->error('', 'No user', 401);
    }

    public function adminDeleteReservation($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $reservation = Reservation::find($id);
            $reservation->delete();
            return $this->success(data: $user, message: "Reservation deleted successfully");
        }
        return $this->error('', 'No user', 401);
    }

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

            $availableTables = Table::query()
                ->where('capacity', '>=', $size)
                ->whereDoesntHave('reservations', function ($query) use ($date) {
                    $query->where('date', $date);
                })
                ->get();

            if ($availableTables->isEmpty()) {
                return $this->success(data: [], message: "No available tables for the requested party size");
            }

            $timeSlots = TimeSlot::query()
                ->when($date === now()->format('Y-m-d'), function ($query) use ($currentTime) {
                    $query->where('start_time', '>=', $currentTime);
                })
                ->whereDoesntHave('reservations', function ($query) use ($date) {
                    $query->where('date', $date);
                })
                ->get();

            return $this->success(data: $timeSlots, message: "");
        }
        return $this->error('', 'No user', 401);
    }


    public function getReservations(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->reservations()->with('table', 'timeSlot')->get();

            $items->each(function ($item) {
                $item->table_number = $item->table->number ?? null;
                $item->start_time = $item->timeSlot->start_time ?? null;

                unset($item->table);
                unset($item->timeSlot);
            });
            return $this->success(data: $items, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function getReservationsDetails($id){
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $item = $user->reservations()->with('table')->with('timeSlot')->find($id);

            if ($item) {
                $item->table_number = $item->table->number ?? null;
                $item->start_time = $item->timeSlot->start_time ?? null;
                unset($item->table);
                unset($item->timeSlot);
                return $this->success(data: [$item], message: "");
            }

            return $this->error('', 'Reservation not found', 404);
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
                "status" => "Pending",
                "code" => $this->generate_code()
            ]);

            $item = $user->reservations()->with('table')->with('timeSlot')->find($reservation->id);

            $item->table_number = $item->table->number ?? null;
            $item->start_time = $item->timeSlot->start_time ?? null;
            unset($item->table);
            unset($item->timeSlot);
            return $this->success(data: [$item], message: "Reservation was created successfully");

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

    public function cancelReservation($id)
    {
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $item = $user->reservations()->with('table')->with('timeSlot')->find($id);

            if ($item) {
                if($item->status == "Pending"){
                    $item->update(['status' => 'Cancelled']);
                    $item->table_number = $item->table->number ?? null;
                    $item->start_time = $item->timeSlot->start_time ?? null;
                    unset($item->table);
                    unset($item->timeSlot);
                    return $this->success(data: [$item], message: "Reservation cancelled successfully");
                }else{
                    $item->table_number = $item->table->number ?? null;
                    $item->start_time = $item->timeSlot->start_time ?? null;
                    unset($item->table);
                    unset($item->timeSlot);
                    return $this->error([$item], 'Reservation cannot be canceled.', 200);
                }
            }
            return $this->error('', 'Reservation not found', 404);
        }
        return $this->error('', 'No user', 401);
    }

    private function generate_code(): String
    {
        $chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        do {
            $code='';
            for($i=0;$i<6;$i++){
                $code .= $chars[rand(0, 35)];
            }
        } while (Reservation::where('code', $code)->exists());

        return $code;
    }
}
