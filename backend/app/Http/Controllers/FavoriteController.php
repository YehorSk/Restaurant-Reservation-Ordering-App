<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class FavoriteController extends Controller
{
    use HttpResponses;

    public function getUserFavoriteItems(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->favoriteItems()->get();
            return response()->json($items);
        }
        return $this->error('', 'No user', 401);
    }

    public function addUserFavoriteItem(Request $request){
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


}
