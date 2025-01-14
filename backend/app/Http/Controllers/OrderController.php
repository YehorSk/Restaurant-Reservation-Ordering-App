<?php

namespace App\Http\Controllers;

use App\Models\Order;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;
use Illuminate\Validation\Validator;

class OrderController extends Controller
{
    use HttpResponses;

    public function getStats($year){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $data_today = Order::whereDay('created_at', now()->day)->get();
            $data_all = Order::all();
            $data_stats = Order::selectRaw('SUM(price) as total_price')
                ->whereYear('created_at', $year)
                ->groupByRaw('MONTHNAME(created_at)')
                ->orderByRaw('MONTH(created_at) DESC')
                ->pluck('total_price');
            $years = Order::selectRaw('YEAR(created_at) as year')
                ->groupBy('year')
                ->orderByRaw('year ASC')
                ->pluck('year');
            $months = Order::selectRaw('MONTHNAME(created_at) as month')
                ->whereYear('created_at', $year)
                ->groupBy('month')
                ->orderByRaw('month DESC')
                ->pluck('month');
            return $this->success([[
                'data_today' => $data_today->count(),
                'data_all_count' => $data_all->count(),
                'years' => $years,
                'months' => $months,
                'data_stats' => $data_stats
            ]]);
        }
        return $this->error('', 'No user', 401);
    }

    public function getAllOrders(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $search = $request->input('search');
            $items = Order::with('orderItems')->when($search, function ($query, $search) {
                    return $query->where(function ($query) use ($search) {
                        $query->where('code', 'LIKE', "%{$search}%");
                    });
                })
                ->paginate(10);
            return $this->success($items);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function adminUpdateOrder(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = Order::all()->find($id);
            $data = $request->validate([
                "status" => "required",
            ]);
            $item->update($data);
            return $this->success(data: [$item], message: __('messages.order_updated_successfully'));
        }
        return $this->error('', __('messages.no_user'), 401);
    }


    public function adminDeleteOrder($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $reservation = Order::find($id);
            $reservation->delete();
            return $this->success(data: $user, message: __('messages.order_deleted_successfully'));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function index(){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $data = Order::all();
            return $this->success($data);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getUserCartItems(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->menuItems()->get()->map(function ($item) use ($user) {
                $item->isFavorite = (bool)$user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                return $item;
            });
            return $this->success(data: $items, message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getUserOrders(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            if ($user->role === 'user') {
                $orders = $user->clientOrders()->with('orderItems')->get();
            } elseif (in_array($user->role, ['waiter', 'chef', 'admin'])) {
//                $orders = $user->waiterOrders()->with('orderItems')->get();
                $orders = Order::with('orderItems')->get();
            } else {
                return $this->error('', 'Invalid role', 403);
            }
            return $this->success(data: $orders, message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function getUserOrderDetails($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            if ($user->role === 'user') {
                $order = $user->clientOrders()->with('orderItems')->find($id);
            } elseif (in_array($user->role, ['waiter', 'chef', 'admin'])) {
                $order = Order::with('orderItems')->find($id);
            } else {
                return $this->error('', 'Invalid role', 403);
            }
            if ($order) {
                return $this->success(data: [$order], message: __('messages.order_retrieved_successfully'));
            }
            return $this->error('', 'Order not found', 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function makeUserPickUpOrder(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $order = new Order([
                'price' => $total_price,
                'special_request' => $request->input('special_request', ''),
                'order_type' =>  $request->input('order_type'),
                'client_id' => $user->id,
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
            return $this->success(data: [$order], message: __('messages.pickup_order_was_created'));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function makeWaiterOrder(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "waiter"){
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $table = $request->input('table_id');
            $order = new Order([
                'price' => $total_price,
                'special_request' => $request->input('special_request', ''),
                'order_type' =>  $request->input('order_type'),
                'code' => $this->generate_code(),
                'status' => 'Confirmed',
                'table_id' => $table['id'],
                'waiter_id' => $user->id
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
            return $this->success(data: [$order], message: __('messages.order_was_created'));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function makeUserDeliveryOrder(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $order = new Order([
                'price' => $total_price,
                'special_request' => $request->input('special_request', ''),
                'order_type' =>  $request->input('order_type'),
                'address' =>  $request->input('address'),
                'instructions' =>  $request->input('instructions'),
                'client_id' => $user->id,
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
            return $this->success(data: [$order], message: __('messages.delivery_order_was_created'));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function userCancelOrder($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $order = $user->clientOrders()->with('orderItems')->find($id);
            if ($order) {
                if($order->status == 'Pending'){
                    $order->update(['status' => 'Cancelled']);
                    $order = $user->clientOrders()->with('orderItems')->find($id);
                    return $this->success(data: [$order], message: __('messages.order_canceled_successfully'));
                }else{
                    return $this->error('', __('messages.order_cannot_be_canceled'), 409);
                }
            }
            return $this->error('', __('messages.order_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function repeatOrder($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $order = $user->clientOrders()->with('orderItems')->find($id);
            foreach ($order->orderItems as $item){
                $exists = $user->menuItems()
                    ->where('menu_item_id', $item->id)
                    ->first();
                if($exists) {
                    $user->menuItems()->updateExistingPivot($item->id, [
                        'quantity' => $exists->pivot->quantity + $item->pivot->quantity,
                        'price' => $exists->pivot->price + $item->pivot->price
                    ]);
                }else{
                    $user->menuItems()->attach($item->id, [
                        'quantity' => $item->pivot->quantity,
                        'price' => $item->pivot->price,
                    ]);
                }
            }
            return $this->success(data: [$order], message: __('messages.items_added_to_cart_successfully'));
        }
        return $this->error('', 'No user', 401);
    }

    public function markOrderAsCancelled($id){
        $user = auth('sanctum')->user();
        if($user instanceof User && in_array($user->role, ['waiter', 'chef', 'admin'])){
            $order = Order::with('orderItems')->find($id);
            if ($order) {
                $order->update(['status' => 'Cancelled']);
                $order = Order::with('orderItems')->find($id);
                return $this->success(data: [$order], message: __('messages.order_canceled_successfully'));
            }
            return $this->error('', __('messages.order_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function markOrderAsConfirmed($id){
        $user = auth('sanctum')->user();
        if($user instanceof User && in_array($user->role, ['waiter', 'chef', 'admin'])){
            $order = Order::with('orderItems')->find($id);
            if ($order) {
                $order->update(['status' => 'Confirmed']);
                $order = Order::with('orderItems')->find($id);
                return $this->success(data: [$order], message: __('messages.order_confirmed_successfully'));
            }
            return $this->error('', __('messages.order_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function markOrderAsCompleted($id){
        $user = auth('sanctum')->user();
        if($user instanceof User && in_array($user->role, ['waiter', 'chef', 'admin'])){
            $order = Order::with('orderItems')->find($id);
            if ($order) {
                $order->update(['status' => 'Completed']);
                $order = Order::with('orderItems')->find($id);
                return $this->success(data: [$order], message: __('messages.order_completed_successfully'));
            }
            return $this->error('', __('messages.order_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function markOrderAsPreparing($id){
        $user = auth('sanctum')->user();
        if($user instanceof User && in_array($user->role, ['waiter', 'chef', 'admin'])){
            $order = Order::with('orderItems')->find($id);
            if ($order) {
                $order->update(['status' => 'Preparing']);
                $order = Order::with('orderItems')->find($id);
                return $this->success(data: [$order], message: __('messages.order_marked_as_preparing'));
            }
            return $this->error('', __('messages.order_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function markOrderAsReadyForPickup($id){
        $user = auth('sanctum')->user();
        if($user instanceof User && in_array($user->role, ['waiter', 'chef', 'admin'])){
            $order = Order::with('orderItems')->find($id);
            if ($order) {
                $order->update(['status' => 'Ready for Pickup']);
                $order = Order::with('orderItems')->find($id);
                return $this->success(data: [$order], message: __('messages.order_ready_for_pickup'));
            }
            return $this->error('', __('messages.order_not_found'), 404);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    private function generate_code(): String
    {
        $chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        do {
            $code='';
            for($i=0;$i<6;$i++){
                $code .= $chars[rand(0, 35)];
            }
        } while (Order::where('code', $code)->exists());

        return $code;
    }

}
