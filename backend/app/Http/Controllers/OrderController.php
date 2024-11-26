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
            $orders = $user->orders()->get();
            return $this->success(data: $orders, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function makeUserPickUpOrder(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $total_price = $user->menuItems()->get()->sum('pivot.price');
            $order = new Order();
            $order->price = $total_price;
            $order->special_request = $request->input("special_request");
            $order->pickup = true;
            $order->client_id = $user->id;
            $order->status = "new";
            $order->save();
            $items = $user->menuItems()->get();
            foreach ($items as $item) {
                $order->orderItems()->attach($item->id, [
                    'quantity' => $item->pivot->quantity,
                    'price' => $item->pivot->price,
                ]);
                $user->menuItems()->detach($item->id);
            }
            return $this->success(data: $order, message: "");
        }
        return $this->error('', 'No user', 401);
    }
}
