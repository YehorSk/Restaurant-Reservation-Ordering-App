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

    public function getUserCartItems(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->menuItems()->get()->map(function ($item) use ($user) {
                $item->isFavorite = (bool)$user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                return $item;
            });
            return $this->success(data: $items, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function getUserOrders(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            if ($user->role === 'user') {
                $orders = $user->clientOrders()->with('orderItems')->get();
            } elseif ($user->role === 'waiter') {
                $orders = $user->waiterOrders()->with('orderItems')->get();
            } else {
                return $this->error('', 'Invalid role', 403);
            }
            return $this->success(data: $orders, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function getUserOrderDetails($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            if ($user->role === 'user') {
                $order = $user->clientOrders()->with('orderItems')->find($id);
            } elseif ($user->role === 'waiter') {
                $order = $user->waiterOrders()->with('orderItems')->find($id);
            } else {
                return $this->error('', 'Invalid role', 403);
            }
//            $order = $user->clientOrders()->with('orderItems')->find($id);
            if ($order) {
                return $this->success(data: [$order], message: "Order retrieved successfully.");
            }
            return $this->error('', 'Order not found', 404);
        }
        return $this->error('', 'No user', 401);
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
            return $this->success(data: [$order], message: "Pickup order was created");
        }
        return $this->error('', 'No user', 401);
    }

    public function makeWaiterOrder(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "waiter"){
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $order = new Order([
                'price' => $total_price,
                'special_request' => $request->input('special_request', ''),
                'order_type' =>  $request->input('order_type'),
                'code' => $this->generate_code(),
                'status' => 'Pending',
                'table_id' => $request->input('selected_table'),
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
            return $this->success(data: [$order], message: "Order was created");
        }
        return $this->error('', 'No user', 401);
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
            return $this->success(data: [$order], message: "Delivery order was created");
        }
        return $this->error('', 'No user', 401);
    }

    public function cancelOrder($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $order = $user->clientOrders()->with('orderItems')->find($id);
            if ($order) {
                if($order->status == 'Pending'){
                    $order->update(['status' => 'Canceled']);
                    $order = $user->clientOrders()->with('orderItems')->find($id);
                    return $this->success(data: [$order], message: "Order canceled successfully.");
                }else{
                    return $this->error('', 'Order cannot be canceled.', 409);
                }
            }
            return $this->error('', 'Order not found', 404);
        }
        return $this->error('', 'No user', 401);
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
            return $this->success(data: [$order], message: "Items added to cart successfully.");
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
        } while (Order::where('code', $code)->exists());

        return $code;
    }

}
