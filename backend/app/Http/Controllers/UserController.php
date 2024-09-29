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
            $user->menuItems()->attach($request->input('menu_item_id'), [
                'quantity' => $request->input('quantity'),
                'price' => $request->input('price'),
                'note' => $request->input('note') ?? '',
            ]);
            return response()->json("Item added to cart");
        }
        return $this->error('', 'No user', 401);
    }
}
