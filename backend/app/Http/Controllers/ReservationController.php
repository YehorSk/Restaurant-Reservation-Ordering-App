<?php

namespace App\Http\Controllers;

use App\Models\Order;
use App\Models\Reservation;
use App\Models\RestaurantInfo;
use App\Models\Table;
use App\Models\TimeSlot;
use App\Models\User;
use App\Traits\FCMNotificationTrait;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;
use Illuminate\Support\Carbon;

class ReservationController extends Controller
{

    use FCMNotificationTrait;
    use HttpResponses;

    public function getStats($year){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $data_today = Reservation::whereDate('created_at', now()->toDateString())->get();
            $data_all = Reservation::all();
            $data_stats = Reservation::selectRaw('COUNT(*) AS amount, MONTHNAME(created_at) AS month_name, MONTH(created_at) AS month_number')
                ->whereYear('created_at', $year)
                ->groupByRaw('MONTHNAME(created_at), MONTH(created_at)')
                ->orderBy('month_number', 'ASC')
                ->pluck('amount');
            $years = Order::selectRaw('YEAR(created_at) as year')
                ->groupBy('year')
                ->orderByRaw('year ASC')
                ->pluck('year');
            $months = Order::selectRaw('MONTHNAME(created_at) as month, MONTH(created_at) as month_number')
                ->whereYear('created_at', $year)
                ->groupByRaw('MONTHNAME(created_at), MONTH(created_at)')
                ->orderBy('month_number', 'ASC')
                ->pluck('month');
            return $this->success([
                'data_today' => $data_today->count(),
                'data_all_count' => $data_all->count(),
                'data_stats' => $data_stats,
                'years' => $years,
                'months' => $months,
            ]);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getAllReservations(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $search = $request->input('search');
            $items = Reservation::with('table', 'timeSlot')
                ->when($search, function ($query, $search) {
                    return $query->where(function ($query) use ($search) {
                        $query->where('code', 'LIKE', "%{$search}%");
                    });
                })
                ->paginate(10);
            $items->each(function ($item) {
                $item->table_number = $item->table->number ?? null;
                $item->start_time = $item->timeSlot->start_time ?? null;

                unset($item->table);
                unset($item->timeSlot);
            });
            return $this->success($items);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function adminDeleteReservation($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $reservation = Reservation::find($id);
            $reservation->delete();
            return $this->success(data: $user, message: __("messages.reservation_deleted_successfully"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getAvailableTimeSlots(Request $request)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $date = $request->input('reservation_date', '');
            $partySize = $request->input('party_size', '');

            if (!$date || !$partySize) {
                return $this->error('', 'Reservation date and party size are required', 400);
            }

            $currentTime = now()->setTimezone('Europe/Bratislava')->format('H:i:s');

            $openingHoursJson = RestaurantInfo::first()->opening_hours;
            $openingHours = json_decode($openingHoursJson, true);
            $weekday = Carbon::parse($date)->format('l');
            $todaySchedule = $openingHours[$weekday];

            [$startRaw, $endRaw] = explode('-', $todaySchedule['hours']);

            $startTime = Carbon::createFromFormat('H:i', $startRaw)->format('H:i:s');
            $endTime = Carbon::createFromFormat('H:i', $endRaw)->format('H:i:s');

            // Fetch time slots
            $timeSlots = TimeSlot::query()
                ->where('start_time', '>=', $date === now()->format('Y-m-d') ? $currentTime : $startTime)
                ->where('end_time', '<=', $endTime)
                ->orderBy('start_time')
                ->get();

            $timeSlots = $timeSlots->map(function ($timeSlot) use ($date, $partySize) {
                $availableTablesCount = Table::query()
                    ->whereDoesntHave('reservations', function ($query) use ($date, $timeSlot) {
                        $startTime = \Carbon\Carbon::createFromFormat('H:i:s', $timeSlot->start_time);
                        $adjustedStartTime = $startTime->copy()->subMinutes(75);
                        $adjustedEndTime = $startTime->copy()->addMinutes(120);

                        $query->where('date', $date)
                            ->where('status', '!=', 'Cancelled')
                            ->whereHas('timeSlot', function ($timeSlotQuery) use ($adjustedStartTime, $adjustedEndTime) {
                                $timeSlotQuery
                                    ->whereBetween('start_time', [
                                        $adjustedStartTime->format('H:i:s'),
                                        $adjustedEndTime->format('H:i:s'),
                                    ]);
                            });
                    })
                    ->where('capacity', '>=', $partySize)
                    ->count();

                $timeSlot->available_tables = $availableTablesCount;

                return $timeSlot;
            });

            return $this->success(data: $timeSlots, message: "");
        }

        return $this->error('', __('messages.no_user'), 401);
    }

    public function createReservation(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $date = $request->input('reservation_date', '');
            $size = $request->input('party_size', '');
            $timeSlotId = $request->input('time_slot_id', '');
            $withOrder = $request->input('with_order',false);

            $timeSlot = TimeSlot::find($timeSlotId);

            if (!$date || !$size || !$timeSlotId) {
                return $this->error('', 'Reservation date, party size, and time slot are required', 400);
            }

            $availableTable = Table::query()
                ->whereDoesntHave('reservations', function ($query) use ($date, $timeSlot) {
                    $startTime = \Carbon\Carbon::createFromFormat('H:i:s', $timeSlot->start_time);
                    $adjustedStartTime = $startTime->copy()->subMinutes(75);
                    $adjustedEndTime = $startTime->copy()->addMinutes(120);

                    $query->where('date', $date)
                        ->where('status', '!=', 'Cancelled')
                        ->whereHas('timeSlot', function ($timeSlotQuery) use ($adjustedStartTime, $adjustedEndTime) {
                            $timeSlotQuery
                                ->whereBetween('start_time', [
                                    $adjustedStartTime->format('H:i:s'),
                                    $adjustedEndTime->format('H:i:s'),
                                ]);
                        });
                })
                ->where('capacity', '>=', $size)->first();

            if (!$availableTable) {
                return $this->error('', __("messages.tables_not_available"), 409);
            }

            $reservation = Reservation::create([
                "client_id" => $user->id,
                "table_id" => $availableTable->id,
                "time_slot_id" => $timeSlotId,
                "party_size" => $size,
                "date" => $date,
                "status" => "Pending",
                "special_request" => $request->input('special_request', ''),
                "phone" => $request->input('full_phone', ''),
                "code" => $this->generate_code()
            ]);

            $item = $user->reservations()->with('table')->with('timeSlot')->find($reservation->id);

            $item->table_number = $item->table->number ?? null;
            $item->start_time = $item->timeSlot->start_time ?? null;
            unset($item->table);
            unset($item->timeSlot);
            if($withOrder){
                $total_price = $user->menuItems()->get()->sum('pivot.price');
                $order = new Order([
                    'price' => $total_price,
                    'special_request' => $request->input('order_form.special_request', ''),
                    'order_type' =>  "2",
                    'client_id' => $user->id,
                    'reservation_id' => $reservation->id,
                    'code' => $this->generate_code(),
                    'status' => 'Pending',
                    'start_time' =>  $timeSlot->start_time,
                    'end_time' =>  $timeSlot->end_time,
                    'date' => $date
                ]);
                $order->save();
                $items = $user->menuItems()->get();
                foreach ($items as $menu_item) {
                    $order->orderItems()->attach($menu_item->id, [
                        'quantity' => $menu_item->pivot->quantity,
                        'price' => $menu_item->pivot->price,
                    ]);
                    $user->menuItems()->detach($menu_item->id);
                }
            }
            return $this->success(data: [$item], message: __("messages.reservation_was_created_successfully"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getReservations(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            if ($user->role === 'user') {
                $items = $user->reservations()->with('table', 'timeSlot','orders')->get();

                $items->each(function ($item) {
                    $item->table_number = $item->table->number ?? null;
                    $item->start_time = $item->timeSlot->start_time ?? null;
                    $item->order_code = $item->orders->isNotEmpty() ? $item->orders->first()->code : '';

                    unset($item->table);
                    unset($item->timeSlot);
                    unset($item->orders);
                });
                return $this->success(data: $items, message: "");
            }elseif (in_array($user->role, ['waiter', 'chef', 'admin'])) {
                $items = Reservation::with('table', 'timeSlot')->get();

                $items->each(function ($item) {
                    $item->table_number = $item->table->number ?? null;
                    $item->start_time = $item->timeSlot->start_time ?? null;
                    $item->order_code = $item->orders->isNotEmpty() ? $item->orders->first()->code : null;

                    unset($item->table);
                    unset($item->timeSlot);
                    unset($item->orders);
                });
                return $this->success(data: $items, message: "");
            }else{
                return $this->error('', 'Invalid role', 403);
            }

        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getReservationsDetails($id){
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            if ($user->role === 'user') {
                $item = $user->reservations()->with('table','timeSlot','orders')->find($id);

                $item->table_number = $item->table->number ?? null;
                $item->start_time = $item->timeSlot->start_time ?? null;
                $item->order_code = $item->orders->isNotEmpty() ? $item->orders->first()->code : null;

                unset($item->table);
                unset($item->timeSlot);
                unset($item->orders);
                return $this->success(data: [$item], message: "");
            }elseif (in_array($user->role, ['waiter', 'chef', 'admin'])) {
                $item = Reservation::with('table')->with('timeSlot')->find($id);
                $item->order_code = $item->orders->isNotEmpty() ? $item->orders->first()->code : null;

                $item->table_number = $item->table->number ?? null;
                $item->start_time = $item->timeSlot->start_time ?? null;
                unset($item->table);
                unset($item->timeSlot);
                unset($item->orders);
                return $this->success(data: [$item], message: "");
            } else {
                return $this->error('', 'Invalid role', 403);
            }


        }

        return $this->error('', __('messages.no_user'), 401);
    }

    public function makeUserReservationOrder(Request $request, $reservationId)
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $order = new Order([
                'price' => $total_price,
                'special_request' => $request->input('special_request', ''),
                'order_type' => 4,
                'client_id' => $user->id,
                'reservation_id' => $reservationId,
                'code' => $this->generate_code(),
                'status' => 'Pending',
                'start_time' =>  $request->input('start_time'),
                'end_time' =>  $request->input('end_time'),
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
            return $this->success(
                data: [$order],
                message: __('messages.pickup_order_was_created')
            );
        }
        return $this->error('', __('messages.no_user'), 401);
    }


    public function cancelReservation($id)
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            if ($user->role === 'user') {
                $item = $user->reservations()->with('table')->with('timeSlot')->find($id);
            } else {
                $item = Reservation::with('table')->with('timeSlot')->find($id);
            }
            if ($item) {
                if ($item->status == "Pending") {
                    $item->update(['status' => 'Cancelled']);
                    $item->orders()->update(['status' => 'Cancelled']);
                    $item->table_number = $item->table->number ?? null;
                    $item->start_time = $item->timeSlot->start_time ?? null;
                    unset($item->table);
                    unset($item->timeSlot);
                    return $this->success(
                        data: [$item],
                        message: __('messages.reservation_cancelled_successfully')
                    );
                } else {
                    $item->table_number = $item->table->number ?? null;
                    $item->start_time = $item->timeSlot->start_time ?? null;
                    unset($item->table);
                    unset($item->timeSlot);
                    return $this->error(
                        [$item],
                        __('messages.reservation_cannot_be_canceled'),
                        200
                    );
                }
            }
            return $this->error('', __('messages.reservation_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }


    public function adminUpdateReservation(Request $request, $id)
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = Reservation::with('table')->with('timeSlot')->find($id);
            $data = $request->validate([
                "status" => "required",
            ]);
            $item->update($data);
            $item->table_number = $item->table->number ?? null;
            $item->start_time = $item->timeSlot->start_time ?? null;
            if($item->client_id != null){
                $owner = User::find($item->client_id);
                $token = $owner->devices->pluck('device_token')->first();
                $this->sendFCMNotification($token, "PLATEA", $this->getReservationStatusMessage($request->input("status"),$item->code,$owner->language));
            }
            unset($item->table);
            unset($item->timeSlot);
            return $this->success(
                data: [$item],
                message: __('messages.reservation_updated_successfully')
            );
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    function getReservationStatusMessage($status, $reservationId, $lang = 'en')
    {
        switch ($status) {
            case 'Pending':
                $statusKey = 'messages.reservation_pending';
                break;

            case 'Confirmed':
                $statusKey = 'messages.reservation_confirmed';
                break;

            case 'Cancelled':
                $statusKey = 'messages.reservation_cancelled';
                break;

            default:
                $statusKey = 'messages.reservation_unknown';
                break;
        }
        $message = __($statusKey, ['reservationId' => $reservationId], $lang);

        return $message;
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

        return "#" . $code;
    }
}
