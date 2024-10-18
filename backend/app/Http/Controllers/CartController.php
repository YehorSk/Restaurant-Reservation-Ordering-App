<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class CartController extends Controller
{
    use HttpResponses;

    public function getUserCartItems(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->menuItems()->get();
            return response()->json($items);
        }
        return $this->error('', 'No user', 401);
    }

    public function addUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $exists = $user->menuItems()
                ->where('menu_item_id', $request->input('menu_item_id'))
                ->first();
            if($exists){
                $user->menuItems()->updateExistingPivot($request->input('menu_item_id'), [
                    'quantity' => $exists->pivot->quantity + $request->input('quantity'),
                    'price' => $exists->pivot->price + $request->input('price')
                ]);
                return $this->success(data: $exists, message: "Item in Cart was updated");
            }
            $user->menuItems()->attach($request->input('menu_item_id'), [
                'quantity' => $request->input('quantity'),
                'price' => $request->input('price')
            ]);
            $newItem = $user->menuItems()->where('menu_item_id', $request->input('menu_item_id'))
                ->wherePivot('price', $request->input('price'))
                ->wherePivot('quantity', $request->input('quantity'))
                ->first();
            return $this->success(data: $newItem, message: "Item added to cart");
        }
        return $this->error('', 'No user', 401);
    }

    public function deleteUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $exists = $user->menuItems()
                ->wherePivot('id', $request->input('pivot_id'))
                ->first();
            if($exists){
                $user->menuItems()->detach($exists);
                return $this->success(data: $exists, message: "Item in Cart was deleted");
            }
            return $this->error('', "Item was not deleted", 400);
        }
        return $this->error('', 'No user', 401);
    }

    public function updateUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $exists = $user->menuItems()
                ->wherePivot('id', $request->input('pivot_id'))
                ->first();
            if($exists){
                $user->menuItems()->updateExistingPivot($exists->id, [
                    'price' => $request->input('price'),
                    'quantity' => $request->input('quantity'),
                ]);
                $item = $user->menuItems()
                    ->wherePivot('id', $request->input('pivot_id'))
                    ->first();
                return $this->success(data: $item, message: "Item in Cart was updated");
            }
            return $this->error('', "Item in Cart was not updated", 400);
        }
        return $this->error('', 'No user', 401);
    }
}
