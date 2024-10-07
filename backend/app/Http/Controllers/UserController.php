<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class UserController extends Controller
{
    use HttpResponses;
    public function index(Request $request){

    }

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
                ->wherePivot('note', $request->input('note')?? '')
                ->first();
            if($exists){
                $user->menuItems()->updateExistingPivot($request->input('menu_item_id'), [
                    'quantity' => $exists->pivot->quantity + $request->input('quantity'),
                    'price' => $exists->pivot->price + $request->input('price'),
                    'note' => $request->input('note') ?? '',
                ]);
                return response()->json("Item in Cart was updated");
            }
            $user->menuItems()->attach($request->input('menu_item_id'), [
                'quantity' => $request->input('quantity'),
                'price' => $request->input('price'),
                'note' => $request->input('note') ?? '',
            ]);
            return response()->json("Item added to cart");
        }
        return $this->error('', 'No user', 401);
    }

    public function deleteUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $exists = $user->menuItems()
                ->where('menu_item_id', $request->input('menu_item_id'))
                ->wherePivot('note', $request->input('note')?? '')
                ->wherePivot('price', $request->input('price'))
                ->wherePivot('quantity', $request->input('quantity'))
                ->first();
            if($exists){
                $user->menuItems()->detach($exists);
                return response()->json("Item in Cart was deleted");
            }
            return response()->json("Item was not deleted");
        }
    }

    public function updateUserCartItem(Request $request){
        return response()->json("Item in Cart was updated");
    }


}
